package ru.clevertec.newsmanagement.util;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;


/**
 * Utility class for sorting direction.
 * @author Dayanch
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortDirectionUtil {
    private static final String DESC = "desc";

    /**
     * Returns a sorted object based on the specified sorting direction.
     * @param sort the object to sort
     * @param direction the sorting direction
     * @return the sorted object
     */
    public static Sort getDirection(@NotNull Sort sort, String direction) {
        return DESC.equalsIgnoreCase(direction) ? sort.descending() : sort.ascending();
    }
}
