create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER not null
        primary key
        unique,
    GENRE_NAME CHARACTER VARYING(20)
);

create table  IF NOT EXISTS MPA
(
    MPA_ID INTEGER not null
        primary key
        unique,
    MPA_NAME CHARACTER VARYING(10)
);

create table  IF NOT EXISTS FILMS
(
    FILM_ID         INTEGER auto_increment
        primary key
        unique,
    FILM_NAME       CHARACTER VARYING(200) not null,
    DESCRIPTION     CHARACTER VARYING(200),
    RELEASE_DATE    DATE not null,
    DURATION        INTEGER not null
);

create table  IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment
        unique,
    EMAIL     CHARACTER VARYING(255) not null,
    LOGIN     CHARACTER VARYING(50)  not null,
    USER_NAME CHARACTER VARYING(50)  not null,
    BIRTHDAY  DATE check (BIRTHDAY < NOW()),
    constraint USER_PK
        primary key (USER_ID)
);

create table  IF NOT EXISTS USER_FRIENDS
(
    USER_ID              INTEGER,
    FRIEND_ID            INTEGER,
    FRIENDSHIP_CONFIRMED BOOLEAN default FALSE,
    constraint USER_FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint USER_FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create table  IF NOT EXISTS USER_LIKED_FILM
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint USER_LIKED_FILM_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint USER_LIKED_FILM_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FILM_MPA
(
    FILM_ID INTEGER,
    MPA_ID  INTEGER,
    constraint FILM_MPA_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_MPA_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

/* таблица для хранения отзывов к фильмам */
CREATE TABLE IF NOT EXISTS reviews (
    review_id INTEGER NOT NULL AUTO_INCREMENT,
    content VARCHAR NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,       /* пользователь - автор отзыва */
    film_id INTEGER NOT NULL,
    useful INTEGER NOT NULL DEFAULT 0,        /* рейтинг отзыва */
    CONSTRAINT reviews_pk PRIMARY KEY (review_id),
    CONSTRAINT reviews_fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT reviews_fk2_film FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

/* таблица для хранения информации о лайках/дизлайках отзывов и для расчета рейтинга отзыва */
/* также служит для предотвращения накрутки рейтинга отзыва: один пользователь сможет поставить одному отзыву только одну оценку */
CREATE TABLE IF NOT EXISTS reviews_likes (
       review_id INTEGER NOT NULL,
       user_id INTEGER NOT NULL,    /* пользователь, поставивший оценку отзыву */
       like_value TINYINT NOT NULL, /* будет +1 для положительной оценки отзыва и -1 для отрицательной */
       CONSTRAINT reviews_likes_pk PRIMARY KEY (review_id, user_id),
       CONSTRAINT reviews_likes_fk1_review FOREIGN KEY (review_id) REFERENCES reviews(review_id) ON DELETE CASCADE,
       CONSTRAINT reviews_likes_fk2_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

create table IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID         INTEGER auto_increment
        primary key
        unique,
    DIRECTOR_NAME       CHARACTER VARYING(20) not null
);

create table IF NOT EXISTS FILM_DIRECTORS
(
    FILM_ID     INTEGER,
    DIRECTOR_ID INTEGER,
    constraint FILM_DIRECTORS_DIRECTORS_DIRECTOR_ID_FK
        foreign key (DIRECTOR_ID) references DIRECTORS,
    constraint FILM_DIRECTORS_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
);

