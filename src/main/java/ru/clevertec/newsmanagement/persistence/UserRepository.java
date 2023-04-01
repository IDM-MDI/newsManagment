package ru.clevertec.newsmanagement.persistence;

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
    Optional<User> findUserByUsername(String username);
}