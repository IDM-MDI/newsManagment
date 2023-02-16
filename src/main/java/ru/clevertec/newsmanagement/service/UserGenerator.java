package ru.clevertec.newsmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

@Component
@RequiredArgsConstructor
public class UserGenerator {
    private static final String ADMIN = "admin";
    private static final String JOURNALIST = "journalist";
    private static final String SUBSCRIBER = "subscriber";

    private final UserService service;

    public void generateEntity() {
        generateUsers();
    }
    private void generateUsers() {
        service.saveUser(new User(ADMIN + 1,ADMIN + 1, Role.ADMIN));
        service.saveUser(new User(ADMIN + 2,ADMIN + 2, Role.ADMIN));
        service.saveUser(new User(JOURNALIST + 1,JOURNALIST + 1, Role.JOURNALIST));
        service.saveUser(new User(JOURNALIST + 2,JOURNALIST + 2, Role.JOURNALIST));
        service.saveUser(new User(JOURNALIST + 3,JOURNALIST + 3, Role.JOURNALIST));
        service.saveUser(new User(SUBSCRIBER + 1, SUBSCRIBER + 1, Role.SUBSCRIBER));
        service.saveUser(new User(SUBSCRIBER + 2, SUBSCRIBER + 2, Role.SUBSCRIBER));
        service.saveUser(new User(SUBSCRIBER + 3, SUBSCRIBER + 3, Role.SUBSCRIBER));
        service.saveUser(new User(SUBSCRIBER + 4, SUBSCRIBER + 4, Role.SUBSCRIBER));
        service.saveUser(new User(SUBSCRIBER + 5, SUBSCRIBER + 5, Role.SUBSCRIBER));
    }
}
