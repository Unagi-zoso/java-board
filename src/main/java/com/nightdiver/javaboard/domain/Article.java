package com.nightdiver.javaboard.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter // 모든 필드 접근 위해
@ToString(callSuper = true) // audit 필드까지 출력하기 위해
@Table(indexes = {
    @Index(name = "idx_article_title", columnList = "title"),
    @Index(name = "idx_article_hashtag", columnList = "hashtag"),
    @Index(name = "idx_article_createdBy", columnList = "createdBy"),
    @Index(name = "idx_article_createdAt", columnList = "createdAt"),
})
@EntityListeners(AuditingEntityListener.class) // audit 기능을 사용하기 위해
@Entity
// embedded 대시 상속으로 구현. JPA에서 MappedSuperclass를 제공. embedded 필드 하나 둬 둠으로써 한 단계 더 들어가는 번거로움 제거
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)

    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag; // 해시태그

    @Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    protected Article() { // JPA에서 사용하기 위해 기본 생성자 필요
    }

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
