package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByNameContainsIgnoreCase(String name);
    Collection<Student> findByAgeBetween(int min, int max);

    @Query(value = "SELECT COUNT(*) FROM студенты", nativeQuery = true)
    Integer getTotalCountOfStudents();

    @Query(value = "SELECT AVG(age) FROM студенты", nativeQuery = true)
    double getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM студенты ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getLastFiveStudents();


}
