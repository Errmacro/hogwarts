package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

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
}
