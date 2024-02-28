package com.nightdiver.javaboard.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.nightdiver.javaboard.domain.type.SearchType;
import com.nightdiver.javaboard.dto.response.ArticleResponse;
import com.nightdiver.javaboard.dto.response.ArticleWithCommentsResponse;
import com.nightdiver.javaboard.service.ArticleService;
import com.nightdiver.javaboard.service.PaginationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false, name = "searchType") SearchType searchType,
            @RequestParam(required = false, name = "searchKeyword") String searchKeyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable,
            ModelMap modelMap
    ) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchKeyword, pageable).map(ArticleResponse::from);
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumbers(articles.getTotalPages(), pageable.getPageNumber());
        modelMap.addAttribute("articles", articles);
        modelMap.addAttribute("paginationBarNumbers", paginationBarNumbers);

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable("articleId") Long articleId,  ModelMap modelMap) {
        ArticleWithCommentsResponse articleWithCommentsResponse = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
        modelMap.addAttribute("article", articleWithCommentsResponse);
        modelMap.addAttribute("articleComments", articleWithCommentsResponse.articleCommentResponses());

        return "articles/detail";
    }
}
