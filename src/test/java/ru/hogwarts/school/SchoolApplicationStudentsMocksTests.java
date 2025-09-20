package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class SchoolApplicationStudentsMocksTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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

        when(studentService.getStudentByID(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Виктор Крумов"))
                .andExpect(jsonPath("$.age").value(14));
    }

    @Test
    public void testGetStudentInfoNotFound() throws Exception {
        when(studentService.getStudentByID(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindStudentByName() throws Exception {
        Student student = createTestStudent();

        when(studentService.findByName("Виктор Крумов")).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/byName")
                        .param("name", "Виктор Крумов")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Виктор Крумов"))
                .andExpect(jsonPath("$.age").value(14));
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
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", "Виктор Крумов");
        studentObject.put("age", 14);

        Student student = createTestStudent();

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Виктор Крумов"))
                .andExpect(jsonPath("$.age").value(14));
    }

    @Test
    public void testEditStudent() throws Exception {
        JSONObject studentObject = new JSONObject();
        studentObject.put("id", 1L);
        studentObject.put("name", "Гарри Джеймс Поттер");
        studentObject.put("age", 18);

        Student updatedStudent = createTestStudent();
        updatedStudent.setName("Гарри Джеймс Поттер");
        updatedStudent.setAge(18);

        when(studentService.updateStudent(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/1")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гарри Джеймс Поттер"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isNoContent());
    }
}
