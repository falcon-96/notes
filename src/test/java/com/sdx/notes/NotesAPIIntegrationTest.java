package com.sdx.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sdx.notes.model.Note;
import com.sdx.notes.repository.NotesRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class NotesAPIIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    NotesRepository notesRepository;

    public static final String BASE_PATH = "/note";

    @Test
    void newNote_createApiCalled_getsCreated() throws Exception {
        String note = """
                {"title" : "New Test Note", "content": "Some blank content"}""";

        mvc.perform(post(BASE_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isCreated());
    }

    @Test
    void validId_getApiCalled_getsValidData() throws Exception {
        Note newNote = postNewNote();
        mvc.perform(get(BASE_PATH + "/{id}", newNote.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is(newNote.getTitle())));
    }

    @Test
    void modifiedNote_updateApiCalled_existingVersionGetsUpdated() throws Exception {
        Note newNote = postNewNote();
        String modifiedNote = """
                {"id": "%d", "title" : "%s", "content": "%s"}""".formatted(newNote.getId(), newNote.getTitle(), newNote.getContent());
        mvc.perform(put(BASE_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedNote))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastModifiedTimestamp", Matchers.not(newNote.getLastModifiedTimestamp())));
    }

    @Test
    void validId_deleteApiCalled_isDeletedSuccessfully() throws Exception {
        Note newNote = postNewNote();
        mvc.perform(delete(BASE_PATH + "/{id}", newNote.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void invalidId_deleteApiCalled_exceptionThrown() throws Exception {
        notesRepository.deleteAll();
        mvc.perform(delete(BASE_PATH + "/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    private Note postNewNote() throws Exception {
        String note = """
                {"title" : "New Test Note", "content": "Some blank content"}""";

        var response = mvc.perform(post(BASE_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.readValue(response.getResponse().getContentAsByteArray(), Note.class);
    }

}
