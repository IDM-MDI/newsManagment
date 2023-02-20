package ru.clevertec.newsmanagement.persistence;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsmanagement.entity.User;

import java.util.Optional;


/**
 * Repository interface for User entities.
 * @author Dayanch
 */
@Repository
public interface UserRepository extends JpaRepository<User,String> {
    /**
     * Finds a user by their username.
     * @param username the username of the user to be found
     * @return an Optional containing the User if found, or empty if not
     */
    @Cacheable(cacheNames = "userCache",key = "#username.hashCode()")
    Optional<User> findUserByUsername(String username);

    /**
     * Saves a User entity and evicts the cached User with the same username.
     * @param entity the User entity to be saved
     * @param <S> the type of the User entity
     * @return the saved User entity
     */
    @Override
    @CacheEvict(cacheNames = "userCache",key = "#entity.username.hashCode()",allEntries = true)
    <S extends User> S save(S entity);
}