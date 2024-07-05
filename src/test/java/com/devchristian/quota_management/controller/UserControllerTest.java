package com.devchristian.quota_management.controller;

import com.devchristian.quota_management.dto.UserRequestDto;
import com.devchristian.quota_management.dto.UserResponseDto;
import com.devchristian.quota_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private UserResponseDto userResponseDto;

    private static final String USER_JSON = """
            {
                "firstName": "Chris",
                "lastName": "Mendez"
            }""";

    private static final String USER_RESPONSE_JSON = """
            {
                "id": "1",
                "firstName": "Chris",
                "lastName": "Mendez"
            }""";

    @BeforeEach
    void setUp() {
        userResponseDto = new UserResponseDto("1", "Chris", "Mendez", 0, null);
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(USER_RESPONSE_JSON));

        verify(userService, times(1)).createUser(any(UserRequestDto.class));
    }

    @Test
    void getUser() throws Exception {
        String userId = "1";
        when(userService.getUser(userId)).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(USER_RESPONSE_JSON));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void updateUser() throws Exception {
        String userId = "1";
        when(userService.updateUser(eq(userId), any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(USER_RESPONSE_JSON));

        verify(userService, times(1)).updateUser(eq(userId), any(UserRequestDto.class));
    }

    @Test
    void deleteUser() throws Exception {
        String userId = "1";
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }
}