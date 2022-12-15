create table IF NOT EXISTS GENRE
(
    GENRE_ID   int auto_increment
        primary key,
    GENRE_NAME varchar(20)
);

create table  IF NOT EXISTS MPA
(
    MPA_ID int not null
        primary key
        unique,
    MPA_NAME varchar(10)
);

create table  IF NOT EXISTS FILMS
(
    FILM_ID         int auto_increment
        primary key
        unique,
    FILM_NAME       varchar(200) not null,
    DESCRIPTION     varchar(200),
    RELEASE_DATE    DATE not null,
    DURATION        INTEGER not null
);

create table IF NOT EXISTS FILM_MPA
(
    FILM_ID int,
    MPA_ID  int,
    constraint FILM_MPA_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_MPA_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  int,
    GENRE_ID int,
    constraint FILM_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create table  IF NOT EXISTS USERS
(
    USER_ID   int auto_increment
        primary key
        unique,
    EMAIL     varchar(255) not null,
    LOGIN     varchar(50)  not null,
    USER_NAME varchar(50)  not null,
    BIRTHDAY  DATE not null
);

create table  IF NOT EXISTS USER_FRIENDS
(
    USER_ID              int,
    FRIEND_ID            int,
    FRIENDSHIP_CONFIRMED BOOLEAN,
    constraint USER_FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint USER_FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create table  IF NOT EXISTS USER_LIKED_FILM
(
    FILM_ID int,
    USER_ID int,
    constraint USER_LIKED_FILM_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint USER_LIKED_FILM_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);