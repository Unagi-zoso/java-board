package com.nightdiver.javaboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nightdiver.javaboard.config.SecurityConfig;
import com.nightdiver.javaboard.dto.ArticleCommentDto;
import com.nightdiver.javaboard.dto.ArticleWithCommentsDto;
import com.nightdiver.javaboard.dto.UserAccountDto;
import com.nightdiver.javaboard.service.ArticleService;
import com.nightdiver.javaboard.service.PaginationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class) // 이대로 두면 모든 컨트롤러를 읽이에 필요한 컨트롤러 빈만 인자로 전달 가능
class ArticleControllerTest {
    private final MockMvc mockMvc;

    @MockBean ArticleService articleService; // 하나는 MockBean, 하나는 Autowired .. 혼종이야..
    @MockBean PaginationService paginationService;
    public ArticleControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("[view][GET] 게시글 목록 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));
        // when & then
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 목록 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    public void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPageWithPagingAndSorting() throws Exception {
        // given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);
        given(articleService.searchArticles(eq(null), eq(null), eq(pageable))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(Page.empty().getTotalPages(), pageable.getPageNumber())).willReturn(paginationBarNumbers);
        // when & then
        mockMvc.perform(get("/articles")
                .param("page", String.valueOf(pageNumber))
                .param("size", String.valueOf(pageSize))
                .param("sort", sortName + "," + direction))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(Page.empty().getTotalPages(), pageable.getPageNumber());
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        // when & then
        mockMvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"));
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashTagSearchView_thenReturnsArticleHashTagSearchView() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search-hashtag"));
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(1L,
                "test_id",
                "test_pwd",
                "test_email",
                "test_nick",
                "test_memo",
                LocalDateTime.now(),
                "test_creator",
                LocalDateTime.now(),
                "test_modifier");
    }

    private ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.of(1L,
                1L,
                createUserAccountDto(),
                "test_content",
                LocalDateTime.now(),
                "test_creator",
                LocalDateTime.now(),
                "test_modifier");
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return new ArticleWithCommentsDto(1L,
                createUserAccountDto(),
                Set.of(createArticleCommentDto()),
                "test_title",
                "test_content",
                "test_hashtag",
                LocalDateTime.now(),
                "test_creator",
                LocalDateTime.now(),
                "test_modifier");
    }
}
