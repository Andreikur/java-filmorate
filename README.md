# java-filmorate
Template repository for Filmorate project.

Ссылка на структуру базы данных для приложения Filmorate:
\description\structure_Filmorate_SQL.jpg

![Ссылка на структуру базы данных для приложения Filmorate:](\description\structure_Filmorate_SQL.jpg)

Ссылка на lucid.app:

https://lucid.app/lucidchart/a25eba5f-6d98-49b7-b732-9e83992d96cf/edit?viewport_loc=472%2C238%2C1765%2C902%2C0_0&invitationId=inv_9dc035e4-713d-4759-af0c-ca3018a7c042

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