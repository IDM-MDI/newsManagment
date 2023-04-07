package ru.clevertec.newsmanagement.newsservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Pageable")
public class PageFilter {
    @Min(0)
    @Schema(description = "page number")
    private int number = 0;
    @Min(1)
    @Schema(description = "page size")
    private int size = 5;
    @NotBlank
    @Length(min = 2)
    @Schema(description = "filter by column name")
    private String filter = "id";
    @Pattern(regexp = "asc|desc")
    @Schema(description = "asc or desc")
    private String direction = "asc";
}
