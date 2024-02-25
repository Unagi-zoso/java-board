package com.nightdiver.javaboard;

import static org.assertj.core.api.Assertions.assertThat;

import com.nightdiver.javaboard.config.JpaConfig;
import com.nightdiver.javaboard.domain.Article;
import com.nightdiver.javaboard.domain.UserAccount;
import com.nightdiver.javaboard.repository.ArticleCommentRepository;
import com.nightdiver.javaboard.repository.ArticleRepository;
import com.nightdiver.javaboard.repository.UserAccountRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

// junit5 보단 assertj 사용
// slice test : slice test란 특정 레이어에 특화된 테스트를 지원하는 애노테이션
//@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest // JPA 관련 설정만 로드하는 테스트, @ExtendWith(SpringExtension.class)에서 Autowired 지원 (JPA 5)
public class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSizeGreaterThan(0);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("test", "test", "test", "test", "test_memo"));

        // When
        articleRepository.save(Article.of(userAccount, "제목", "내용", "해시태그"));

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        Article updatedArticle = articleRepository.save(article);
        // DataJpaTest 의 트랜잭셔널에 의해 rollback 될 거기에 업데이트를 반영시키지 않는다. 그래서 flush 필수
        // 같은 논리면 데이터 생성에서 수정이 다 트랜잭션 범위 내에서만 일어나면 발생 쿼리가 하나도 없어야 하나?

        // Then
        assertThat(updatedArticle.getHashtag()).isEqualTo(updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        long previousArticleCount = articleRepository.count();
        long previousCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = articleRepository.findById(1L).orElseThrow().getArticleComments().size();

        // When
        articleRepository.delete(articleRepository.findById(1L).orElseThrow());

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousCommentCount - deletedCommentsSize);
    }
}
