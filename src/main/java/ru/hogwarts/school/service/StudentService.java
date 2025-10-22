package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    private static final Lock lock = new ReentrantLock();

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student getStudentByID(Long studentId) {
        logger.info("Was invoked method for getting student");
        return studentRepository.findById(studentId).orElse(null);
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for updating student");
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long studentId) {
        logger.info("Was invoked method for remove student from roster");
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            studentRepository.deleteById(studentId);
        }
        return student;
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get student by age");
        if (age <= 0) {
            return Collections.emptyList();
        }
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Student findByName(String name){
        logger.info("Was invoked method for get student by name");
        return studentRepository.findByNameContainsIgnoreCase(name);
    }

    public Collection<Student> findByAgeBetween (int min, int max){
        logger.info("Was invoked method for get students by age interval");
        return studentRepository.findByAgeBetween (min, max);
    }

    public Integer getTotalCountOfStudents() {
        logger.info("Was invoked method for get total count of students");
        return studentRepository.getTotalCountOfStudents();
    }

    public double getAverageAgeOfStudents() {
        logger.info("Was invoked method for get students by average age");
        return studentRepository.getAverageAgeOfStudents();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }

    public Collection<String> getNamesStartingWithG() {
        Collection<Student> allStudents = studentRepository.findAll();
        return allStudents.stream()
                .map(Student::getName)
                .filter(name -> name.toLowerCase().startsWith("г"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAge() {
        Collection<Student> students = studentRepository.findAll();
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    public void printParallel(List<Student> students) {

        List<String> names = students.stream()
                .map(Student::getName)
                .toList();

        System.out.println("Главный поток: " + names.get(0));

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            System.out.println("Параллельный поток 1: " + names.get(2));
            System.out.println("Параллельный поток 1: " + names.get(3));
        });

        executorService.submit(() -> {
            System.out.println("Параллельный поток 2: " + names.get(4));
            System.out.println("Параллельный поток 2: " + names.get(5));
        });

        executorService.shutdown();

        System.out.println("Главный поток: " + names.get(1));
    }

    public void synchronizedPrint(String name) {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + ": " + name);
        } finally {
            lock.unlock();
        }
    }

    public void printSynchronized(List<Student> students) {

        List<String> names = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        synchronizedPrint(names.get(0));


        Thread thread1 = new Thread(() -> {
            synchronizedPrint(names.get(2));
            synchronizedPrint(names.get(3));
        }, "Поток-1");
        thread1.start();

        Thread thread2 = new Thread(() -> {
            synchronizedPrint(names.get(4));
            synchronizedPrint(names.get(5));
        }, "Поток-2");
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronizedPrint(names.get(1));
    }
}
