package dev.juancastro.digitaldiary.entries;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EntryController.class,
properties = "api-endpoint=/api/v1")
@AutoConfigureMockMvc(addFilters = false)
class EntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EntryService entryService;

    private EntryDto sampleDto(Long id) {
        return new EntryDto(
            id,
            "content",
            LocalDateTime.of(2025, 4, 19, 12, 0),
            "HAPPY",
            "5",
            Set.of(1L, 2L),
            1L
        );
    }

    @Test
    @DisplayName("POST /entry - Success")
    void createEntrySuccess() throws Exception {
        EntryDto input = sampleDto(null);
        EntryDto created = sampleDto(42L);
        Principal principal = () -> "john";
        given(entryService.createEntry(any(EntryDto.class), eq("john"))).willReturn(created);

        mockMvc.perform(post("/api/v1/entry")
            .principal(principal)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(42))
            .andExpect(jsonPath("$.content").value("content"))
            .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("GET /entry/{id} - Found")
    void getEntryByIdFound() throws Exception {
        EntryDto dto = sampleDto(5L);
        given(entryService.getEntryById(5L)).willReturn(dto);

        mockMvc.perform(get("/api/v1/entry/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    @DisplayName("GET /entry - Success")
    void getAllEntries() throws Exception {
        EntryDto dto = sampleDto(1L);
        given(entryService.getAllEntries()).willReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/entry"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].content").value("content"));
    }

    @Test
    @DisplayName("PUT /entry/{id} - Success")
    void updateEntrySuccess() throws Exception {
        EntryDto input = sampleDto(null);
        EntryDto updated = sampleDto(7L);
        given(entryService.updateEntry(eq(7L), any(EntryDto.class))).willReturn(updated);

        mockMvc.perform(put("/api/v1/entry/7")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(7))
            .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    @DisplayName("DELETE /entry/{id} - Success")
    void deleteEntrySuccess() throws Exception {
        doNothing().when(entryService).deleteEntry(3L);

        mockMvc.perform(delete("/api/v1/entry/3"))
            .andExpect(status().isNoContent());
    }
}
