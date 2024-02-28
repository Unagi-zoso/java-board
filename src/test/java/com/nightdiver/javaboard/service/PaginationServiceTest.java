package com.nightdiver.javaboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest(webEnvironment = NONE, classes = {PaginationService.class})
class PaginationServiceTest {
    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다.")
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0}, {1} -> {2}")
    void givenCurrentPageNumberAndTotalPages_whenRequesting_thenReturnsPaginationBarNumbers(int totalPages, int currentPageNumber, List<Integer> expectedPaginationBarNumbers) {
        // Given

        // When
        List<Integer> actualPaginationBarNumbers = sut.getPaginationBarNumbers(totalPages, currentPageNumber);

        // Then
        assertThat(actualPaginationBarNumbers).isEqualTo(expectedPaginationBarNumbers);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenRequesting_thenReturnsPaginationBarNumbers() {
        return Stream.of(
                Arguments.of(13, 0, List.of(0, 1, 2, 3, 4)),
                Arguments.of(13, 1, List.of(0, 1, 2, 3, 4)),
                Arguments.of(13, 2, List.of(0, 1, 2, 3, 4)),
                Arguments.of(13, 3, List.of(1, 2, 3, 4, 5)),
                Arguments.of(13, 4, List.of(2, 3, 4, 5, 6)),
                Arguments.of(13, 5, List.of(3, 4, 5, 6, 7)),
                Arguments.of(13, 6, List.of(4, 5, 6, 7, 8)),
                Arguments.of(13, 10, List.of(8, 9, 10, 11, 12)),
                Arguments.of(13, 11, List.of(9, 10, 11, 12)),
                Arguments.of(13, 12, List.of(10, 11, 12))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void givenNothing_whenCalling_thenReturnsCurrentBarLength() {
        // Given

        // When
        int actualCurrentBarLength = sut.currentBarLength();

        // Then
        assertThat(actualCurrentBarLength).isEqualTo(5);
    }
}
