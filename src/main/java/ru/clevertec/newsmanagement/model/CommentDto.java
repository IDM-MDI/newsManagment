package ru.clevertec.newsmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@Schema(description = "User comments")
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Comment ID")
    private Long id;
    @NotBlank
    @Length(min = 1,max = 250,message = "Text must have from ${min} to ${max} symbol")
    @Schema(description = "Comment text/message(min:1/max:250)")
    private String text;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "The name of the commenter who created this one")
    private String username;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "The date when comment was created")
    private Date createdDate;
}
