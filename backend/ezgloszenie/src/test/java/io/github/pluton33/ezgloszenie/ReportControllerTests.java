package io.github.pluton33.ezgloszenie;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pluton33.ezgloszenie.controller.ReportController;
import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;
import io.github.pluton33.ezgloszenie.data.User;
import io.github.pluton33.ezgloszenie.data.UserRole;
import io.github.pluton33.ezgloszenie.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@AutoConfigureJsonTesters
public class ReportControllerTests {
    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    ReportService reportService;

    @Test
    void shouldReturnAllReports() throws Exception {
        User user = new User(0L, UserRole.USER, "k.nowak@gmail.com", "Kamil", "Nowak");
        List<Report> list = List.of(
                new Report(1L, "report1", "content1", user),
                new Report(2L, "report2", "content2", user)
        );
        ReportsResponse fakeResponse = new ReportsResponse(list);
        when(reportService.getReports()).thenReturn(fakeResponse);
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(content().json((objectMapper.writeValueAsString(fakeResponse))));
    }

    @Test
    void shouldReturnReportById() throws Exception {
        User user = new User(0L, UserRole.USER, "k.nowak@gmail.com", "Kamil", "Nowak");
        List<Report> list = List.of(
                new Report(1L, "report1", "content1", user),
                new Report(2L, "report2", "content2", user)
        );
        when(reportService.getReportById(2)).thenReturn(list.get(1));
        mockMvc.perform(get("/reports/2"))
                .andExpect(status().isOk())
                .andExpect(content().json((objectMapper.writeValueAsString(list.get(1)))));
    }

    @Test
    void shouldAddReport() throws Exception {
        User user = new User(0L, UserRole.USER, "k.nowak@gmail.com", "Kamil", "Nowak");
        Report report = new Report(null, "title1", "content1", user);
        Report returnedReport = new Report(1L, "title1", "content1", user);

        when(reportService.addReport(any(Report.class), eq("k.nowak@gmail.com"))).thenReturn(returnedReport);
        mockMvc.perform(post("/addReport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(returnedReport)));
    }

    @Test
    void shouldEditReport() throws Exception {
        User user = new User(0L, UserRole.USER, "k.nowak@gmail.com", "Kamil", "Nowak");
        Report report = new Report(1L, "title1", "content1", user);
        Report returnedReport = new Report(1L, "title1", "content1", user);

        when(reportService.editReport(anyInt(), any(Report.class), eq("k.nowak@gmail.com"))).thenReturn(returnedReport);
        mockMvc.perform(put("/reports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(returnedReport)));
    }
}
