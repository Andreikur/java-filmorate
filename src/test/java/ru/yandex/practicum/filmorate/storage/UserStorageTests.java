package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTests {

    private final UserDbStorage userDbStorage;

    @Test
    void addUserTest() throws ValidationException {
        User user = User.builder()
                .email("example@mail.mail")
                .login("login")
                .name("Doe")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.addUser(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }



    /*@Test
    void findUserByIdTest() {
        User user = User.builder()
                .email("example@mail.mail")
                .login("login")
                .name("Doe")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();

        userDbStorage.create(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getById(user.getId())).hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    void updateUserByIdTest() {
        User user = User.builder()
                .email("example@mail.mail")
                .login("login")
                .name("Doe")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.create(user);
        user.setName("testUpdatedName");
        user.setLogin("testUpdatedLogin");
        user.setEmail("updatedExample@mail.mail");
        userDbStorage.update(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getById(user.getId()))
                .hasFieldOrPropertyWithValue("login", "testUpdatedLogin")
                .hasFieldOrPropertyWithValue("name", "testUpdatedName")
                .hasFieldOrPropertyWithValue("email", "updatedExample@mail.mail");
    }

    @Test
    public void testUpdateUserNotFound() {
        User user = User.builder()
                .id(9999)
                .login("testName")
                .email("example@mail.mail")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        Assertions.assertThatThrownBy(() -> userDbStorage.update(user))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void addFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.mail")
                .login("friend")
                .name("Dow")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.mail")
                .login("follower")
                .name("Doe")
                .birthday(LocalDate.of(2000, 10, 20))
                .build();

        userDbStorage.create(friend);
        userDbStorage.create(follower);
        assertThat(userDbStorage.getFriendsListById(friend.getId()).isEmpty());
        userDbStorage.addFriendship(friend.getId(), follower.getId());
        assertThat(userDbStorage.getFriendsListById(friend.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 2);
    }

    @Test
    void removeFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.mail")
                .login("friend")
                .name("Dow")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.mail")
                .login("follower")
                .name("Doe")
                .birthday(LocalDate.of(2000, 10, 20))
                .build();

        userDbStorage.create(friend);
        userDbStorage.create(follower);
        assertThat(userDbStorage.getFriendsListById(friend.getId()).isEmpty());
        userDbStorage.addFriendship(friend.getId(), follower.getId());
        assertThat(userDbStorage.getFriendsListById(friend.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 2);
        userDbStorage.removeFriendship(friend.getId(), follower.getId());
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 1);
    }

    @Test
    void getFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.mail")
                .login("friend")
                .name("Dow")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.mail")
                .login("follower")
                .name("Doe")
                .birthday(LocalDate.of(2000, 10, 20))
                .build();
        userDbStorage.create(friend);
        userDbStorage.create(follower);
        assertThat(userDbStorage.getFriendsListById(friend.getId()).isEmpty());
        userDbStorage.addFriendship(friend.getId(), follower.getId());
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 2);
    }

    @Test
    void getCommonFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.mail")
                .login("friend")
                .name("Dow")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.mail")
                .login("follower")
                .name("Doe")
                .birthday(LocalDate.of(2000, 10, 20))
                .build();
        User following = User.builder()
                .email("example_followingd@mail.mail")
                .login("following")
                .name("Dire")
                .birthday(LocalDate.of(2000, 11, 21))
                .build();

        userDbStorage.create(friend);
        userDbStorage.create(follower);
        userDbStorage.create(following);
        userDbStorage.addFriendship(friend.getId(), following.getId());
        userDbStorage.addFriendship(follower.getId(), following.getId());
        Assertions.assertThat(userDbStorage.getCommonFriendsList(friend.getId(), follower.getId()).size() == 1);
    }

    @Test
    void getAllUsersTest() {
        User user = User.builder()
                .email("example@mail.mail")
                .login("login")
                .name("Doe")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.create(user);
        Collection<User> users = userDbStorage.findAll();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }

    @Test
    void removeUserByIdTest() {
        User user = User.builder()
                .email("example@mail.mail")
                .login("login")
                .name("Doe")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();

        userDbStorage.create(user);
        userDbStorage.deleteById(user.getId());
        Assertions.assertThatThrownBy(()-> userDbStorage.getById(user.getId()))
                .isInstanceOf(ObjectNotFoundException.class);
    }*/
}