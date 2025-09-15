package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findByNameContainsIgnoreCase(String name);
    Faculty findByColorContainsIgnoreCase(String color);
}
