package io.github.pluton33.ezgloszenie;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pluton33.ezgloszenie.controller.OffenseController;
import io.github.pluton33.ezgloszenie.data.Offense;
import io.github.pluton33.ezgloszenie.data.OffensesResponse;
import io.github.pluton33.ezgloszenie.service.OffenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OffenseController.class)
@AutoConfigureJsonTesters
public class OffenseControllerTests {
    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    OffenseService offenseService;

    @Test
    void shouldReturnAllOffenses() throws Exception {
        List<Offense> list = List.of(
                new Offense(1, "offense1", "content1"),
                new Offense(2, "offense2", "content2")
        );
        OffensesResponse fakeResponse = new OffensesResponse(list);
        when(offenseService.getOffenses()).thenReturn(fakeResponse);
        mockMvc.perform(get("/offenses"))
                .andExpect(status().isOk())
                .andExpect(content().json((objectMapper.writeValueAsString(fakeResponse))));
    }

    @Test
    void shouldReturnOffenseById() throws Exception {
        List<Offense> list = List.of(
                new Offense(1, "offense1", "content1"),
                new Offense(2, "offense2", "content2")
        );
        when(offenseService.getOffenseById(2)).thenReturn(list.get(1));
        mockMvc.perform(get("/offenses/2"))
                .andExpect(status().isOk())
                .andExpect(content().json((objectMapper.writeValueAsString(list.get(1)))));
    }

    @Test
    void shouldAddOffense() throws Exception {
        Offense offense = new Offense(null, "title1", "content1");
        Offense returnedOffense = new Offense(1, "title1", "content1");

        when(offenseService.addOffense(any(Offense.class))).thenReturn(returnedOffense);
        mockMvc.perform(post("/addOffense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offense)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(returnedOffense)));
    }

    @Test
    void shouldEditOffense() throws Exception {
        Offense offense = new Offense(1, "title1", "content1");
        Offense returnedOffense = new Offense(1, "title1", "content1");

        when(offenseService.editOffense(anyInt(), any(Offense.class))).thenReturn(returnedOffense);
        mockMvc.perform(put("/offenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offense)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(returnedOffense)));
    }
}
