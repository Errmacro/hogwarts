package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class SchoolApplicationRestTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;
    @Autowired
    private FacultyController facultyController;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void testGetStudentByName() throws Exception {
        Student student = new Student();
        student.setName("Виктор Крумов");
        student.setAge(14);

        this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);

        Assertions
                .assertThat(this.restTemplate.getForObject(
                        "http://localhost:" + port + "/student/byName?name=Виктор Крумов",
                        String.class
                ))
                .isNotNull()
                .contains("Виктор Крумов");
    }

    @Test
    void testPostStudent() throws Exception {
        Student student = new Student();
        student.setName("Виктор Крумов");
        student.setAge(14);
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Виктор Крумов");
        student.setAge(14);

        Student createdStudent = this.restTemplate.postForObject(
                "http://localhost:" + port + "/student", student, Student.class);

        Assertions.assertThat(createdStudent).isNotNull();
        Assertions.assertThat(createdStudent.getId()).isNotNull();

        this.restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
    }

    @Test
    void testEditStudent() throws Exception {
        Student student = new Student();
        student.setName("Виктор Крумов");
        student.setAge(14);

        Student createdStudent = this.restTemplate.postForObject(
                "http://localhost:" + port + "/student", student, Student.class);

        Assertions.assertThat(createdStudent).isNotNull();

        createdStudent.setName("Виктор Обновленный");
        createdStudent.setAge(15);

        Assertions
                .assertThat(this.restTemplate.exchange(
                        "http://localhost:" + port + "/student/" + createdStudent.getId(),
                        HttpMethod.PUT,
                        new HttpEntity<>(createdStudent),
                        Student.class
                ).getBody())
                .isNotNull()
                .extracting(Student::getName, Student::getAge)
                .containsExactly("Виктор Обновленный", 15);
    }

    @Test
    void testPostFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        Faculty createdFaculty = this.restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Assertions.assertThat(createdFaculty).isNotNull();
        Assertions.assertThat(createdFaculty.getId()).isNotNull();

        this.restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
    }

    @Test
    void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        Faculty createdFaculty = this.restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Assertions.assertThat(createdFaculty).isNotNull();

        createdFaculty.setName("Слизерин");
        createdFaculty.setColor("зеленый");

        Assertions
                .assertThat(this.restTemplate.exchange(
                        "http://localhost:" + port + "/faculty/" + createdFaculty.getId(),
                        HttpMethod.PUT,
                        new HttpEntity<>(createdFaculty),
                        Faculty.class
                ).getBody())
                .isNotNull()
                .extracting(Faculty::getName, Faculty::getColor)
                .containsExactly("Слизерин", "зеленый");
    }

    @Test
    void testGetFacultyByColor() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        // Ищем факультет по цвету
        Assertions
                .assertThat(this.restTemplate.getForObject(
                        "http://localhost:" + port + "/faculty/find?color=красный",
                        String.class
                ))
                .isNotNull()
                .contains("красный");
    }
}



