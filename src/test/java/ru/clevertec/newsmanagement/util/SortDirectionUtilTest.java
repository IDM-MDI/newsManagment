package ru.clevertec.newsmanagement.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SortDirectionUtilTest {

    @Test
    void getDirectionShouldReturnDescending() {
        // given
        Sort sort = Sort.by("name");
        String direction = "desc";

        // when
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // then
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getDirectionShouldReturnAscending() {
        // given
        Sort sort = Sort.by("name");
        String direction = "asc";

        // when
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // then
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void getDirectionCaseWhenInsensitiveShouldReturnDescending() {
        // given
        Sort sort = Sort.by("name");
        String direction = "DeSc";

        // when
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // then
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getDirectionWithNullSortShouldThrowException() {
        // given
        String direction = "desc";

        // when
        assertThatThrownBy(() -> SortDirectionUtil.getDirection(null, direction))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getDirectionWithNullDirectionShouldReturnAscending() {
        // given
        Sort sort = Sort.by("name");
        String direction = null;

        // when
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // then
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }
}