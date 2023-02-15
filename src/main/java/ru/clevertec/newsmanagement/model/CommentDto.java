package ru.clevertec.newsmanagement.model;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    private String text;
    private String username;
    private Date createdDate;
}
