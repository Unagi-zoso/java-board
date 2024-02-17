package com.nightdiver.javaboard.repository;

import com.nightdiver.javaboard.domain.ArticleComment;
import com.nightdiver.javaboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true); // 모든 필드 검색하는 기본 기능 비활성화
        bindings.including(root.content, root.createdAt, root.createdBy); // title, content, writer 필드에 대한 검색 기능 추가
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 대소문자 구분없이 검색 like '%${v}%'        bindings.bind(root.createdAt).first(DateTimeExpression::eq);        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
