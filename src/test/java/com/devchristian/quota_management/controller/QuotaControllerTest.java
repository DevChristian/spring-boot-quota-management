package com.devchristian.quota_management.controller;

import com.devchristian.quota_management.dto.UserQuotaResponseDto;
import com.devchristian.quota_management.service.QuotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(QuotaController.class)
@AutoConfigureMockMvc
class QuotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuotaService quotaService;

    private UserQuotaResponseDto userQuotaResponseDto;

    private static final String USER_ID = "1";

    private static final String USER_QUOTA_RESPONSE_JSON = """
            [{
                    "id": "1",
                    "firstName": "Chris",
                    "lastName": "Mendez",
                    "quota": 0,
                    "lastLoginTimeUtc": null
                }]""";

    @BeforeEach
    void setUp() {
        userQuotaResponseDto = new UserQuotaResponseDto(USER_ID, "Chris",
                "Mendez", 0, null);
    }

    @Test
    void consumeQuota() throws Exception {
        doNothing().when(quotaService).consumeQuota(USER_ID);

        mockMvc.perform(post("/quotas/consume/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Quota consumed for user " + USER_ID));

        verify(quotaService, times(1)).consumeQuota(USER_ID);
    }

    @Test
    void getUsersQuota() throws Exception {
        when(quotaService.getUsersQuota()).thenReturn(Collections.singletonList(userQuotaResponseDto));

        mockMvc.perform(get("/quotas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(USER_QUOTA_RESPONSE_JSON));

        verify(quotaService, times(1)).getUsersQuota();
    }
}