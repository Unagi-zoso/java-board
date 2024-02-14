package com.nightdiver.javaboard.repository;

import com.nightdiver.javaboard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
