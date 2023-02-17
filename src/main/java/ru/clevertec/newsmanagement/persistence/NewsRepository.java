package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newsmanagement.entity.News;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News,Long> {
    @Override
    @Cacheable(cacheNames = "newsCache",key = "#pageable")
    Page<News> findAll(Pageable pageable);

    @Override
    @Cacheable(cacheNames = "newsCache",key = "#id")
    Optional<News> findById(Long id);

    @Override
    @CacheEvict(cacheNames = "newsCache", key = "#entity.id", allEntries = true)
    <S extends News> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "newsCache", key = "#entity.id", allEntries = true)
    void deleteById(Long id);
}
