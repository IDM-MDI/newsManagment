CREATE TABLE users
(
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE TABLE news
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title        VARCHAR(255)                            NOT NULL,
    text         TEXT                                    NOT NULL,
    username     VARCHAR(255)                            NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_news PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text     VARCHAR(255)                            NOT NULL,
    username VARCHAR(255)                            NOT NULL,
    news_id  BIGINT                                  NOT NULL,
    created_date     TIMESTAMP WITHOUT TIME ZONE      NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);
ALTER TABLE news
    ADD CONSTRAINT FK_NEWS_ON_USERNAME FOREIGN KEY (username) REFERENCES users (username);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_NEWS FOREIGN KEY (news_id) REFERENCES news (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USERNAME FOREIGN KEY (username) REFERENCES users (username);