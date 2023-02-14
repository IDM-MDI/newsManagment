package ru.clevertec.newsmanagement.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NewsDto {
    private long id;
    private String title;
    private String text;
    private List<CommentDto> comments;
    private Date createdDate;
}
