package ru.clevertec.newsmanagement.userservice.entity;

/**
 * Enum to represent user roles in the system.
 * @author Dayanch
 */
public enum Role {

    /**
     * Admin user role(can use all CRUD operation).
     */
    ADMIN,

    /**
     * Journalist user role(can use CRUD operation for news(update and delete ony owned)).
     */
    JOURNALIST,

    /**
     * Subscriber user role(can use CRUD operation for comment(update and delete ony owned)).
     */
    SUBSCRIBER;
}
