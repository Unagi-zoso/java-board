package com.nightdiver.javaboard.service;

import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PaginationService {
    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int totalPages, int currentPageNumber) {
        int startNumber = Math.max(0, currentPageNumber - (BAR_LENGTH / 2));
        int endNumber = Math.min(totalPages, startNumber + BAR_LENGTH);

        return IntStream.range(startNumber, endNumber)
                .boxed()
                .toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
