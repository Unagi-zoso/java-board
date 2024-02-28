package com.nightdiver.javaboard.repository;

import com.nightdiver.javaboard.domain.Article;
import com.nightdiver.javaboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String searchKeyword, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String searchKeyword, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String searchKeyword, Pageable pageable);
    Page<Article> findByHashtag(String searchKeyword, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true); // 모든 필드 검색하는 기본 기능 비활성화
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // title, content, writer 필드에 대한 검색 기능 추가
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 대소문자 구분없이 검색 like '%${v}%'        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 대소문자 구분없이 검색 like '%${v}%' %에 의해 인덱스 타기 힘들 수 있다.  like.. 쓸 시 % 지정해줘야한다.
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
