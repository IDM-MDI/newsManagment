package ru.clevertec.newsmanagement.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SortDirectionUtilTest {

    @Test
    void getDirectionShouldReturnDescending() {
        // Arrange
        Sort sort = Sort.by("name");
        String direction = "desc";

        // Act
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // Assert
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getDirectionShouldReturnAscending() {
        // Arrange
        Sort sort = Sort.by("name");
        String direction = "asc";

        // Act
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // Assert
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void getDirectionCaseWhenInsensitiveShouldReturnDescending() {
        // Arrange
        Sort sort = Sort.by("name");
        String direction = "DeSc";

        // Act
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // Assert
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getDirectionWithNullSortShouldThrowException() {
        // Arrange
        String direction = "desc";

        // Act
        assertThatThrownBy(() -> SortDirectionUtil.getDirection(null, direction))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getDirectionWithNullDirectionShouldReturnAscending() {
        // Arrange
        Sort sort = Sort.by("name");
        String direction = null;

        // Act
        Sort result = SortDirectionUtil.getDirection(sort, direction);

        // Assert
        assertThat(result.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }
}