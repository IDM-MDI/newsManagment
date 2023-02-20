package ru.clevertec.newsmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * This is an entity class for representing a comment on a news ID.
 * @author Dayanch
 */
@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    /**
     * The unique identifier of the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The text content of the comment.
     */
    @Column(name = "text",nullable = false)
    private String text;

    /**
     * The user who created the comment.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username",updatable = false, nullable = false)
    private User user;


    /**
     * The news ID that the comment belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", updatable = false, nullable = false)
    private News news;


    /**
     * The date and time that the comment was created.
     */
    @CreatedDate
    @Column(name = "created_date",nullable = false)
    private Date createdDate;
}
