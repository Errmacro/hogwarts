package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty createFaculty(Faculty faculty) {
        logger.debug("Attempting to create new faculty: {}", faculty.getName());
        Faculty savedFaculty = facultyRepository.save(faculty);
        logger.debug("Successfully created new faculty with ID: {}", savedFaculty.getId());
        return savedFaculty;
    }

    public Faculty getFacultyByID (Long facultyId){
        logger.info("Was invoked method for get faculty by ID");
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty==null){
            logger.warn("faculty with ID {} not found", facultyId);}else{
            logger.debug("Successfully retrieved faculty: {}", faculty.getName());
        }
        return faculty;
    }

    public Faculty updateFaculty (Faculty faculty){
        logger.debug("Attempting to update new faculty: {}", faculty.getName());
        Faculty updatedFaculty = facultyRepository.save(faculty);
        logger.debug("Successfully updated faculty with ID: {}", updatedFaculty.getId());
        return updatedFaculty;
    }

    public Faculty deleteFaculty (Long facultyId){
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty != null) {
            facultyRepository.deleteById(facultyId);
            logger.debug("Successfully deleted faculty with ID: {}", facultyId);
        }else{logger.warn("Attempted to delete non-existent faculty with ID {}", facultyId);}
        return faculty;
    }

    public Faculty getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculty by colour");
        return facultyRepository.findByColorContainsIgnoreCase(color);
    }

    public Faculty getFacultiesByName(String name) {
        logger.info("Was invoked method for get faculty by name");
        return facultyRepository.findByNameContainsIgnoreCase(name);
    }
}
