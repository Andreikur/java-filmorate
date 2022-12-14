# java-filmorate
Template repository for Filmorate project.

Ссылка на структуру базы данных для приложения Filmorate:
\description\structure_Filmorate_SQL.jpg

![Ссылка на структуру базы данных для приложения Filmorate:](/description/structure_Filmorate_SQL.jpg)

# Примеры запросов

#### Получить всех пользователей
http://localhost:8080/users

#### Получить пользователя с id 1
http://localhost:8080/users/1

#### Получить общих друзей пользователя с id 1 и с id 2
http://localhost:8080/users/1/friends/common/2

#### Получить все фильмы
http://localhost:8080/films

#### Получить фильм с id 1
http://localhost:8080/films/1

#### Возрат списка первых по количеству лайков N фильмов
http://localhost:8080/films/popular

# Примеры запросов SQL
#### добавление записи в таблицу USERS:
insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)

#### добавление записи в таблицу USER_FRIENDS:
insert into USER_FRIENDS (USER_ID, FRIEND_ID, FRIENDSHIP_CONFIRMED) values (?, ?, ?)

#### добавление записи в таблицу FILMS:
insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION) values (?, ?, ?, ?)

#### добавление записи в таблицу FILM_MPA:
insert into FILM_MPA (FILM_ID, MPA_ID) VALUES (?, ?)

#### добавление записи в таблицу FILM_GENRE:
insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)

#### добавление записи в таблицу USER_LIKED_FILM:
insert into USER_LIKED_FILM (FILM_ID, USER_ID) VALUES (?, ?)

#### обновление данных в таблице USERS:
update USERS set EMAIL=?, LOGIN=?, USER_NAME=?, BIRTHDAY=? where USER_ID=?

#### обновление данных в таблице USER_FRIENDS:
update USER_FRIENDS set FRIENDSHIP_CONFIRMED=? 
where USER_ID=? and FRIEND_ID=? and FRIEND_ID<>USER_ID

#### обновление данных в таблице FILMS:
update FILMS set FILM_NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?

#### обновление данных в таблице FILM_MPA:
update FILM_MPA set MPA_ID=? where FILM_ID=?

#### пример выборки данных из таблиц USERS и USER_FRIENDS:
select USERS.USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY 
from USERS inner join USER_FRIENDS as UF on UF.FRIEND_ID = USERS.USER_ID
where UF.USER_ID=?


### Ссылки на взаимное ревью (промежуточное задание спринта 11)

https://github.com/Andreikur/java-filmorate/commit/3f4ad932b3accb0ca0b01f8fad0ebd1078789bb0?diff=unified

https://github.com/brawo1994/java-filmorate/pull/3






