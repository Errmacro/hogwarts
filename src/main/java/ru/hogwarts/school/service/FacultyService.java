package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private Map<Long, Faculty> faculties = new HashMap<>();
    private long count = 0;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(count++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFacultyByID (long facultyId){
        return faculties.get(facultyId);
    }

    public Faculty updateFaculty (Faculty faculty){
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty (long facultyId){
        return faculties.remove(facultyId);
    }

    public List<Faculty> getFacultiesByColor(String color) {
        if (color == null) {
            return Collections.emptyList();
        }
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());

    }
}
