package ru.clevertec.newsmanagement.newsservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PageFilter {
    @Min(0)
    private int number = 0;
    @Min(1)
    private int size = 5;
    @NotBlank
    @Length(min = 2)
    private String filter = "id";
    @Pattern(regexp = "asc|desc")
    private String direction = "asc";
}
