package com.nightdiver.javaboard.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter // 모든 필드 접근 위해
@ToString // 모든 필드 쉽게 출력하기 위해
@Table(indexes = {
        @Index(name = "idx_article_comment_content", columnList = "content"),
        @Index(name = "idx_article_comment_createdBy", columnList = "createdBy"),
        @Index(name = "idx_article_comment_createdAt", columnList = "createdAt"),
})
@EntityListeners(AuditingEntityListener.class) // audit 기능을 사용하기 위해
@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    // optional = false,  해당 엔티티가 반드시 연관 엔티티를 가져야 한다. 즉, 왜래키는 NUll 일 수 없다.
    @Setter @ManyToOne(optional = false) private Article article; // 게시글 id
    @Setter @Column(nullable = false, length = 500) private String content; // 본문

    @CreatedDate
    @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy
    @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate
    @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy
    @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

    protected ArticleComment() {
    }

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
