CREATE TABLE book (
    id              BIGINT  PRIMARY KEY AUTO_INCREMENT,
    title           VARCHAR(255),
    author          VARCHAR(255),
    image           VARCHAR(255),
    publisher       VARCHAR(255),
    pubdate         DATE,
    isbn            VARCHAR(255),
    description     VARCHAR(1000),
    total_count     INT,
    available_count INT,
    reg_date        DATE
);

CREATE TABLE user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255),
    user_id         VARCHAR(255),
    role            VARCHAR(255)
);

CREATE TABLE reservation (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT,
    book_id         BIGINT,
    reserve_date    DATE,
    reserve_time    TIME,
    return_date     DATE,
    status          VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);
