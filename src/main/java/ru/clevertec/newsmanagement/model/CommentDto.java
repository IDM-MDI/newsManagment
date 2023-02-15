package ru.clevertec.newsmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @NotBlank
    @Length(min = 1,max = 250,message = "Text must have from ${min} to ${max} symbol")
    private String text;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate;
}
