package hexlet.code.app.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.app.dto.LoginDto;
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

import static hexlet.code.app.controller.AuthController.LOGIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("users.yml")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestUtils utils;

    @Test
    public void login() throws Exception {

        final LoginDto loginDto = new LoginDto(
                "John@gmail.com",
                "123"
        );
        String content = utils.asJson(loginDto);

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(LOGIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

    }
    @Test
    public void loginFail() throws Exception {

        final LoginDto loginDto = new LoginDto(
                "John@gmail.com",
                "311"
        );
        String content = utils.asJson(loginDto);

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(LOGIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);

    }
}
