package ru.hogwarts.school.service;

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

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentByID(Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            studentRepository.deleteById(studentId);
        }
        return student;
    }

    public List<Student> getStudentsByAge(int age) {
        if (age <= 0) {
            return Collections.emptyList();
        }
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Student findByName(String name){
        return studentRepository.findByNameContainsIgnoreCase(name);
    }

    public Collection<Student> findByAgeBetween (int min, int max){
        return studentRepository.findByAgeBetween (min, max);
    }

    public Integer getTotalCountOfStudents() {
        return studentRepository.getTotalCountOfStudents();
    }
}
