package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {


    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyByID (Long facultyId){
        return facultyRepository.findById(facultyId).orElse(null);
    }

    public Faculty updateFaculty (Faculty faculty){
        if (!facultyRepository.existsById(faculty.getId())) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty (Long facultyId){
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty != null) {
            facultyRepository.deleteById(facultyId);
        }
        return faculty;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        if (color == null) {
            return Collections.emptyList();
        }
        return facultyRepository.findAll().stream()
                .filter(faculty -> color.equalsIgnoreCase(faculty.getColor()))
                .collect(Collectors.toList());
    }
}
