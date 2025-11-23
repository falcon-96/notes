package com.sdx.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdx.notes.model.User;
import com.sdx.notes.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserIntegrationTest {
    @Autowired
    MockMvc mvc;
    String createApi = "/user";

    @Test
    void validUser_create_isSuccess() throws Exception {
        String request = """
                {"firstName" : "Test", "middleName": "midTestName", "lastName": "lastTest"}""";
        mvc.perform(post(createApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void validUserId_get_returnsValidUserData() throws Exception {
        String api = "/user/{id}";
        String request = """
                {"firstName" : "Test", "middleName": "midTestName", "lastName": "lastTest"}""";
        //add a test user
        MockHttpServletResponse result = mvc.perform(post(createApi)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse();
        ObjectMapper mapper = new ObjectMapper();
        User testUser = mapper.readValue(result.getContentAsByteArray(), User.class);

        mvc.perform(get(api, testUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Test")));
    }

    @Test
    void changeInUserBody_userWantsToUpdate_detailsAreUpdated() throws Exception {
        String api = "/user";
        String request = """
                {"firstName" : "Test", "middleName": "midTestName", "lastName": "lastTest"}""";
        //add a test user
        MockHttpServletResponse result = mvc.perform(post(createApi)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse();
        ObjectMapper mapper = new ObjectMapper();
        User testUser = mapper.readValue(result.getContentAsString(), User.class);

        String updatedRequest = """
                {"id" : "%d", "firstName" : "Test2", "middleName": "midTestName2", "lastName": "lastTest2"}"""
                .formatted(testUser.getId());
        mvc.perform(put(api)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequest))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", Matchers.is("Test2")));
    }

    @Test
    void validId_userWantsToDelete_isDeleted() throws Exception {
        String api = "/user/{id}";
        String request = """
                {"firstName" : "Test", "middleName": "midTestName", "lastName": "lastTest"}""";
        //add a test user
        MockHttpServletResponse result = mvc.perform(post(createApi)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse();
        ObjectMapper mapper = new ObjectMapper();
        User testUser = mapper.readValue(result.getContentAsString(), User.class);

        mvc.perform(delete(api, testUser.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void invalidId_getIsCalled_throwsException() throws Exception {
        String api = "/user/{id}";
        userRepository.deleteAll();

        mvc.perform(get(api, 1000))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
