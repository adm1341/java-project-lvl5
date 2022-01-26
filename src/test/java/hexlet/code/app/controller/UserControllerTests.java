package hexlet.code.app.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("users.yml")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestUtils utils;

    @Test
    void testCreateUser() throws Exception {
        String content = "{\"firstName\": \"Petr_12\", \"lastName\": \"Ivanov\",\"email\": \"petrilo@yandex.ru\", \"password\": \"mypass\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                post(USER_CONTROLLER_PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);

        // Проверяем, что пользователь добавился в базу данных
        User actualUser = userRepository.findByEmail("petrilo@yandex.ru").get();
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getFirstName()).isEqualTo("Petr_12");

        // Проверяем, что пароль хранится в базе в зашифрованном виде
        assertThat(actualUser.getPassword()).isNotEqualTo("mypass");

    }

    @Test
    void tesUnCorrectCreateUser() throws Exception {
        String content = "{\"firstName\": \"\", \"lastName\": \"Ivanov\", \"password\": \"my\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                post(USER_CONTROLLER_PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
    }

    @Test
    void testShowUsers() throws Exception {
        MockHttpServletResponse response1 = mockMvc
                .perform(
                        get(USER_CONTROLLER_PATH)
                )
                .andReturn()
                .getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentAsString()).contains("John", "John@gmail.com");
        assertThat(response1.getContentAsString()).contains("Jassica", "Jassica@gmail.com");


    }

    @Test
    void testShowUserById() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                get(USER_CONTROLLER_PATH + "/1"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getContentAsString()).contains("John", "John@gmail.com");

    }

    @Test
    void testUpdateUser() throws Exception {
        String content = "{\"firstName\": \"Petr_12\", \"lastName\": \"Ivanov\",\"email\": \"petrilo@yandex.ru\", \"password\": \"mypass\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                put(USER_CONTROLLER_PATH + "/1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);


        User actualUser = userRepository.findByEmail("petrilo@yandex.ru").get();
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getFirstName()).isEqualTo("Petr_12");

        // Проверяем, что пароль хранится в базе в зашифрованном виде
        assertThat(actualUser.getPassword()).isNotEqualTo("mypass");

    }

    @Test
    void testUpdateUserFail() throws Exception {
        String content = "{\"firstName\": \"Petr_12\", \"lastName\": \"Ivanov\",\"email\": \"petrilo@yandex.ru\", \"password\": \"mypass\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                put(USER_CONTROLLER_PATH + "/2")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(403);
    }

    @Test
    void testDeleteUserById() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                delete(USER_CONTROLLER_PATH + "/1"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);

        User actualUser = userRepository.findByEmail("John@gmail.com").orElse(null);
        assertThat(actualUser).isNull();
    }

    @Test
    void testDeleteUserByIdFail() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                delete(USER_CONTROLLER_PATH + "/2"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(403);
    }
}
