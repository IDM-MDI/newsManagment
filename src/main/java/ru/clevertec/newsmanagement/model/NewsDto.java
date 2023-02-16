package ru.clevertec.newsmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@Schema(description = "Journalist News")
public class NewsDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "News ID")
    private long id;
    @NotBlank
    @Length(min = 1,max = 100,message = "Title must have from ${min} to ${max} symbol")
    @Schema(description = "The title of news(min:1/max:100)")
    private String title;
    @NotBlank
    @Length(min = 1, message = "Text must have at least ${min} symbol")
    @Schema(description = "The main text of news(min:1/max:100)")
    private String text;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "The name of the news-maker who created this one")
    private String username;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "The date when news was created")
    private Date createdDate;
}
