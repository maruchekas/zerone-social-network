CREATE TYPE block_action AS ENUM ('UNBLOCK', 'BLOCK');

CREATE TABLE block_history
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    action          block_action                            NOT NULL,
    post_id         BIGINT,
    post_comment_id BIGINT,
    person_id       BIGINT                                  NOT NULL,
    CONSTRAINT pk_block_history PRIMARY KEY (id)
);

CREATE TYPE status_name AS ENUM ('REQUEST', 'FRIEND', 'BLOCKED', 'DECLINED', 'SUBSCRIBED');

CREATE TABLE friendship_statuses
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time TIMESTAMP with time zone                 NOT NULL,
    code BIGINT,
    name status_name                              NOT NULL,
    CONSTRAINT pk_friendship_statuses PRIMARY KEY (id)
);

CREATE TABLE friendship
(
    id            INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    status_id     INTEGER                               NOT NULL,
    src_person_id INT8                                  NOT NULL,
    dst_person_id INT8                                  NOT NULL,
    CONSTRAINT pk_friendship PRIMARY KEY (id)
);

CREATE TYPE message_status AS ENUM ('SENT', 'READ');

CREATE TABLE messages
(
    id           INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time         TIMESTAMP WITHOUT TIME ZONE,
    message_text VARCHAR(2048)                         NOT NULL,
    read_status  message_status                        NOT NULL,
    author_id    INT8                                  NOT NULL,
    recipient_id INT8                                  NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TYPE notification_name AS ENUM ('POST', 'POST_COMMENT', 'COMMENT_COMMENT', 'FRIEND_REQUEST', 'MESSAGE', 'FRIEND_BIRTHDAY');

CREATE TABLE notification_type
(
    id              INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    person_id       BIGINT,
    post            BOOLEAN,
    post_comment    BOOLEAN,
    comment_comment BOOLEAN,
    friends_request BOOLEAN,
    message         BOOLEAN,
    friends_birthday BOOLEAN,
    CONSTRAINT pk_notification_type PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sent_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    contact   VARCHAR(255)                            NOT NULL,
    person_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TYPE message_permissions AS ENUM ('ALL', 'FRIENDS', 'NOBODY');

CREATE TYPE user_role AS ENUM ('USER', 'MODERATOR', 'ADMIN');

CREATE TABLE persons
(
    id                  INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name          VARCHAR(255)                          NOT NULL,
    last_name           VARCHAR(255)                          NOT NULL,
    reg_date            TIMESTAMP with time zone              NOT NULL,
    birth_date          TIMESTAMP with time zone,
    email               VARCHAR(255)                          NOT NULL,
    phone               VARCHAR(255),
    password            VARCHAR(255)                          NOT NULL,
    photo               VARCHAR(255),
    about               VARCHAR(2048),
    town                VARCHAR(255),
    country             VARCHAR(255),
    confirmation_code   VARCHAR(255)                          NOT NULL,
    is_approved         INTEGER                               NOT NULL,
    messages_permission message_permissions                   NOT NULL,
    user_type           user_role,
    last_online_time    TIMESTAMP with time zone              NOT NULL,
    is_blocked          INTEGER                               NOT NULL,
    CONSTRAINT pk_persons PRIMARY KEY (id)
);

CREATE TABLE post_comments
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    parent_id    BIGINT,
    comment_text VARCHAR(2048)                           NOT NULL,
    block_id     BIGINT,
    post_id      BIGINT,
    author_id    BIGINT                                  NOT NULL,
    is_blocked   INTEGER                                 NOT NULL,
    CONSTRAINT pk_post_comments PRIMARY KEY (id)
);

CREATE TABLE post_files
(
    id      INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)                          NOT NULL,
    path    VARCHAR(255)                          NOT NULL,
    post_id INT8                                  NOT NULL,
    CONSTRAINT pk_post_files PRIMARY KEY (id)
);

CREATE TABLE post_likes
(
    id        INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time      TIMESTAMP with time zone              NOT NULL,
    person_id INT8                                  NOT NULL,
    post_id   INT8                                  NOT NULL,
    CONSTRAINT pk_post_likes PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id         INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time       TIMESTAMP with time zone              NOT NULL,
    title      VARCHAR(255)                          NOT NULL,
    post_text  VARCHAR(2048)                         NOT NULL,
    is_blocked INTEGER                               NOT NULL,
    block_id   INT8,
    author_id  INT8                                  NOT NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE tags
(
    id  INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    tag VARCHAR(255)                          NOT NULL,
    CONSTRAINT pk_tag PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       INT8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name     VARCHAR(255)                          NOT NULL,
    email    VARCHAR(255)                          NOT NULL,
    password VARCHAR(255)                          NOT NULL,
    type     user_role                             NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE post2tag
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    post_id INT8                                    not null,
    tag_id  INT8                                    not null,
    CONSTRAINT pk_post2tag PRIMARY KEY (id)
);