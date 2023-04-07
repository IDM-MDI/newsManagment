package ru.clevertec.newsmanagement.newsservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Represents a news article.
 * News have a title, a text content, a user who wrote the article and a creation date.
 * This class is mapped to the "news" table in the database.
 * @author Dayanch
 */
@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {

    /**
     * The unique identifier for the news.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * The title of the news.
     */
    @Column(name = "title", nullable = false)
    private String title;


    /**
     * The text content of the news.
     */
    @Column(name = "text", nullable = false)
    private String text;


    /**
     * The user who wrote the news.
     */

    @Column(name = "username",updatable = false, nullable = false)
    private String username;


    /**
     * The date when the news was created.
     */
    @CreatedDate
    @Column(name = "created_date",updatable = false, nullable = false)
    private Date createdDate;
}
