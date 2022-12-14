package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.assertj.core.api.AssertionsForClassTypes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTests {

    private final UserDbStorage userDbStorage;

    @Test
    void addUserTest() throws ValidationException {
        User user = User.builder()
                .email("mail1@yandex.ru")
                .login("login1")
                .name("User1")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.addUser(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }

    @Test
    void updateUserTest() throws ValidationException {
        User user = User.builder()
                .email("email2@yandex.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.addUser(user);
        user.setName("updatedName2");
        user.setLogin("updatedLogin2");
        user.setEmail("email2@yandex.ru");
        userDbStorage.updateUser(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUser(user.getId()))
                .hasFieldOrPropertyWithValue("name", "updatedName2")
                .hasFieldOrPropertyWithValue("login", "updatedLogin2")
                .hasFieldOrPropertyWithValue("email", "email2@yandex.ru");
    }

    @Test
    public void updateUserNotFoundTest() {
        User user = User.builder()
                .id(9999)
                .login("name3")
                .email("email3@yandex.ru")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        Assertions.assertThatThrownBy(() -> userDbStorage.updateUser(user))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllUsersTest() throws ValidationException {
        User user = User.builder()
                .email("mail4@yandex.ru")
                .login("login4")
                .name("name4")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.addUser(user);
        Collection<User> users = userDbStorage.getAllUsers();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }

    @Test
    void getUserTest() throws ValidationException {
        User user = User.builder()
                .email("mail5@yandex.ru")
                .login("login5")
                .name("name5")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();

        userDbStorage.addUser(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUser(user.getId())).hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    void addUserFiendsTest() throws ValidationException {
        User user = User.builder()
                .email("mail6@yandex.ru")
                .login("login6")
                .name("name6")
                .birthday(LocalDate.of(1981, 11, 16))
                .build();
        User friend = User.builder()
                .email("mail7@yandex.ru")
                .login("login7")
                .name("name7")
                .birthday(LocalDate.of(1983, 9, 13))
                .build();

        userDbStorage.addUser(user);
        userDbStorage.addUser(friend);
        assertThat(userDbStorage.findFriends(user.getId()).isEmpty());
        userDbStorage.addUserFiends(user.getId(), friend.getId());
        assertThat(userDbStorage.findFriends(user.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.findFriends(user.getId()).size() == 2);
    }

    @Test
    void removeFriendTest() throws ValidationException {
        User user = User.builder()
                .email("mail8@yandex.ru")
                .login("login8")
                .name("name8")
                .birthday(LocalDate.of(1981, 11, 16))
                .build();
        User friend = User.builder()
                .email("mail9@yandex.ru")
                .login("login9")
                .name("name9")
                .birthday(LocalDate.of(1983, 9, 13))
                .build();

        userDbStorage.addUser(user);
        userDbStorage.addUser(friend);
        assertThat(userDbStorage.findFriends(user.getId()).isEmpty());
        userDbStorage.addUserFiends(user.getId(), friend.getId());
        assertThat(userDbStorage.findFriends(user.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.findFriends(user.getId()).size() == 2);
        userDbStorage.removeFriend(user.getId(), friend.getId());
        Assertions.assertThat(userDbStorage.findFriends(user.getId()).size() == 1);
    }

    @Test
    void findFriendsTest() throws ValidationException {
        User user = User.builder()
                .email("mail10@yandex.ru")
                .login("login10")
                .name("name10")
                .birthday(LocalDate.of(1981, 11, 16))
                .build();
        User friend = User.builder()
                .email("mail11@yandex.ru")
                .login("login11")
                .name("name11")
                .birthday(LocalDate.of(1983, 9, 13))
                .build();
        userDbStorage.addUser(user);
        userDbStorage.addUser(friend);
        assertThat(userDbStorage.findFriends(user.getId()).isEmpty());
        userDbStorage.addUserFiends(user.getId(), friend.getId());
        Assertions.assertThat(userDbStorage.findFriends(user.getId()).size() == 2);
    }

    @Test
    void mutualFriendsTest() throws ValidationException {
        User user = User.builder()
                .email("mail12@yandex.ru")
                .login("login12")
                .name("name12")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        User friend1 = User.builder()
                .email("mail13@yandex.ru")
                .login("login13")
                .name("name13")
                .birthday(LocalDate.of(2000, 10, 20))
                .build();
        User friend2 = User.builder()
                .email("mail14@yandex.ru")
                .login("login14")
                .name("name14")
                .birthday(LocalDate.of(2000, 11, 21))
                .build();

        userDbStorage.addUser(user);
        userDbStorage.addUser(friend1);
        userDbStorage.addUser(friend2);
        userDbStorage.addUserFiends(user.getId(), friend2.getId());
        userDbStorage.addUserFiends(friend1.getId(), friend2.getId());
        Assertions.assertThat(userDbStorage.mutualFriends(user.getId(), friend1.getId()).size() == 1);
    }
}