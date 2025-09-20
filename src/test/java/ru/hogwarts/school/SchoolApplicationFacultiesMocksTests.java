package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class SchoolApplicationFacultiesMocksTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Дурмстранг");
        faculty.setColor("Кровавый");
        return faculty;
    }


    @Test
    public void testGetFacultyInfo() throws Exception {
        Faculty faculty = createTestFaculty();

        when(facultyService.getFacultyByID(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дурмстранг"))
                .andExpect(jsonPath("$.color").value("Кровавый"));
    }

    @Test
    public void testGetFacultyInfoNotFound() throws Exception {
        when(facultyService.getFacultyByID(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFaculty() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", "Дурмстранг");
        facultyObject.put("color", "Кровавый");

        Faculty faculty = createTestFaculty();

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дурмстранг"))
                .andExpect(jsonPath("$.color").value("Кровавый"));
    }

    @Test
    public void testEditFaculty() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", 1L);
        facultyObject.put("name", "Обновленный Дурмстранг");
        facultyObject.put("color", "Обновленный Кровавый");

        Faculty updatedFaculty = createTestFaculty();
        updatedFaculty.setName("Обновленный Дурмстранг");
        updatedFaculty.setColor("Обновленный Кровавый");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/1")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Обновленный Дурмстранг"))
                .andExpect(jsonPath("$.color").value("Обновленный Кровавый"));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindFacultiesByName() throws Exception {
        Faculty faculty = createTestFaculty();

        when(facultyService.getFacultiesByName("Дурмстранг")).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/find")
                        .param("name", "Дурмстранг")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дурмстранг"))
                .andExpect(jsonPath("$.color").value("Кровавый"));
    }


}
