package ru.clevertec.newsmanagement.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newsmanagement.entity.News;

public interface NewsRepository extends JpaRepository<News,Long> {
}
