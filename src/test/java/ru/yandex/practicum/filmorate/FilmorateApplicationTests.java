package ru.yandex.practicum.filmorate;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import com.google.gson.Gson;

@SpringBootTest
class FilmorateApplicationTests {

	UserController userController = new UserController();

	User user1 = User.builder()
			//.id(1)
			.email("mail1@mail.ru")
			.login("userLogin1")
			.name("userName1")
			.birthday(LocalDate.parse("1980-08-20"))
			.build();

	User user2 = User.builder()
			//.id(1)
			.email("mail2@mail.ru")
			.login("userLogin2")
			.name("userName2")
			.birthday(LocalDate.parse("1990-08-20"))
			.build();

	@BeforeEach
	void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args).close();
		//System.exit(0);
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void userParametersTest(){

		userController.addUser(user1);

		Assertions.assertEquals(1, userController.getListAllUsers().size(), "Данные добавлены ошибочно");
	}

}
