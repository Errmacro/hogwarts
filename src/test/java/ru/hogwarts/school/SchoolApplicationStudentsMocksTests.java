package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@Import(StudentService.class)
public class SchoolApplicationStudentsMocksTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    private Student createTestStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Виктор Крумов");
        student.setAge(14);
        return student;
    }


    @Test
    public void testGetStudentInfo() throws Exception {
        Student student = createTestStudent();

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }


    @Test
    public void testGetStudentInfoNotFound() throws Exception {
        Long nonExistentId = 999L;
        when(studentRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindStudentByName() throws Exception {
        Student student = createTestStudent();

        when(studentRepository.findByNameContainsIgnoreCase(student.getName())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/byName")
                        .param("name", student.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    public void testFindStudentByNameBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/byName")
                        .param("name", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = createTestStudent();

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", student.getName());
        studentObject.put("age", student.getAge());

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    public void testEditStudent() throws Exception {
        Student existingStudent = createTestStudent();
        Student updatedStudent = createTestStudent();
        updatedStudent.setName("Обновленный " + existingStudent.getName());
        updatedStudent.setAge(18);

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", existingStudent.getId());
        studentObject.put("name", updatedStudent.getName());
        studentObject.put("age", updatedStudent.getAge());

        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/{id}", existingStudent.getId())
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedStudent.getId()))
                .andExpect(jsonPath("$.name").value(updatedStudent.getName()))
                .andExpect(jsonPath("$.age").value(updatedStudent.getAge()));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isNoContent());
    }
}
