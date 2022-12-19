package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private User user;
    private User userNotUpdate;
    private User friend;
    private User friend1;

    @BeforeEach
    public void initEach(){
        user = User.builder()
                .email("mail1@yandex.ru")
                .login("login1")
                .name("User1")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();

        userNotUpdate = User.builder()
                .id(9999)
                .login("name3")
                .email("email3@yandex.ru")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();

        friend = User.builder()
                .email("mail7@yandex.ru")
                .login("login7")
                .name("name7")
                .birthday(LocalDate.of(1983, 9, 13))
                .build();

        friend1 = User.builder()
                .email("mail14@yandex.ru")
                .login("login14")
                .name("name14")
                .birthday(LocalDate.of(2000, 11, 21))
                .build();
    }

    @Test
    void addUserTest() throws ValidationException {
        userDbStorage.addUser(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }

    @Test
    void updateUserTest() throws ValidationException {
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
        Assertions.assertThatThrownBy(() -> userDbStorage.updateUser(userNotUpdate))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllUsersTest() throws ValidationException {
        userDbStorage.addUser(user);
        Collection<User> users = userDbStorage.getAllUsers();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }

    @Test
    void getUserTest() throws ValidationException {
        userDbStorage.addUser(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUser(user.getId())).hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    void addUserFiendsTest() throws ValidationException {
        userDbStorage.addUser(user);
        userDbStorage.addUser(friend);
        assertThat(userDbStorage.findFriends(user.getId()).isEmpty());
        userDbStorage.addUserFiends(user.getId(), friend.getId());
        assertThat(userDbStorage.findFriends(user.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.findFriends(user.getId()).size() == 2);
    }

    @Test
    void removeFriendTest() throws ValidationException {
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
        userDbStorage.addUser(user);
        userDbStorage.addUser(friend);
        assertThat(userDbStorage.findFriends(user.getId()).isEmpty());
        userDbStorage.addUserFiends(user.getId(), friend.getId());
        Assertions.assertThat(userDbStorage.findFriends(user.getId()).size() == 2);
    }

    @Test
    void mutualFriendsTest() throws ValidationException {
        userDbStorage.addUser(user);
        userDbStorage.addUser(friend);
        userDbStorage.addUser(friend1);
        userDbStorage.addUserFiends(user.getId(), friend1.getId());
        userDbStorage.addUserFiends(friend.getId(), friend1.getId());
        Assertions.assertThat(userDbStorage.mutualFriends(user.getId(), friend1.getId()).size() == 1);
    }
}