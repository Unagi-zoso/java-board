package com.nightdiver.javaboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nightdiver.javaboard.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("컨트롤러 - 메인")
@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {
    private final MockMvc mockMvc;

    MainControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("루트 경로로 요청하면, 게시글 목록 페이지로 리다이렉트")
    @Test
    void givenRootPath_whenRequestingRootPage_thenRedirectsToArticlesPage() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles"));
    }

}
