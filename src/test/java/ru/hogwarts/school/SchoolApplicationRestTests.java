package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationRestTests {

	@LocalServerPort
	private int port;

	@Autowired
	private StudentController studentController;
	@Autowired
	private FacultyController facultyController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() throws Exception{
		Assertions.assertThat(studentController).isNotNull();
		Assertions.assertThat(facultyController).isNotNull();
	}

	@Test
	void testGetStudentByName() throws Exception{
		Assertions
				.assertThat(this.restTemplate.getForObject("http://localhost:"+port+"/byName",String.class))
				.isNotEmpty();
	}

	@Test
	void testPostFaculty() throws Exception{
		Faculty faculty = new Faculty();
		faculty.setName("Дурмстранг");
		faculty.setColor("Кровавый");
		Assertions
				.assertThat(this.restTemplate.postForObject("http://localhost:"+port+"/faculty",faculty,String.class))
				.isNotNull();
	}

}
