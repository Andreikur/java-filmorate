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

create table FILM_MPA
(
    FILM_ID INTEGER,
    MPA_ID  INTEGER,
    constraint FILM_MPA_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_MPA_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);
