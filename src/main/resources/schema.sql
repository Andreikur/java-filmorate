create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER not null
        primary key
        unique,
    GENRE    CHARACTER VARYING(20)
);

create table  IF NOT EXISTS MPA
(
    MPA_ID INTEGER not null
        primary key
        unique,
    MPA    CHARACTER VARYING(10)
);

create table  IF NOT EXISTS FILMS
(
    FILM_ID         INTEGER auto_increment
        primary key
        unique,
    FILM_NAME       CHARACTER(200) not null,
    "DESCRIPTION "  CHARACTER(200),
    "RELEASE_DATE " DATE           not null,
    DURATION        INTEGER        not null,
    LIKE_USERS      INTEGER,
    GENRE           INTEGER        not null,
    RATING_MPA      INTEGER        not null,
    constraint FILMS_GENRE_GENRE_ID_FK
        foreign key (GENRE) references GENRE,
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (RATING_MPA) references MPA
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