package ru.clevertec.newsmanagement.newsservice.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Objects;

/**
 * This is an entity class for representing a comment on a news ID.
 * @author Dayanch
 */
@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
    @Column(name = "username",updatable = false, nullable = false)
    private String username;


    /**
     * The news ID that the comment belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", updatable = false, nullable = false)
    @ToString.Exclude
    private News news;


    /**
     * The date and time that the comment was created.
     */
    @CreatedDate
    @Column(name = "created_date",nullable = false)
    private Date createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id) && text.equals(comment.text) && username.equals(comment.username) && createdDate.equals(comment.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, username, createdDate);
    }
}
