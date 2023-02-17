package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsmanagement.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Cacheable(cacheNames = "userCache",key = "#username.hashCode()")
    Optional<User> findUserByUsername(String username);

    @Override
    @CacheEvict(cacheNames = "userCache",key = "#entity.username.hashCode()",allEntries = true)
    <S extends User> S save(S entity);
}
