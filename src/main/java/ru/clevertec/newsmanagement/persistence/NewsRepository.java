package ru.clevertec.newsmanagement.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsmanagement.entity.News;


/**
 * Repository interface for performing CRUD operations on {@link News} entities.
 * Extends {@link JpaRepository} to inherit basic CRUD functionality.
 * @author Dayanch
 */
@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
}
