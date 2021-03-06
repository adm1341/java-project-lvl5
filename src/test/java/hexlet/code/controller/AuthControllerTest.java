package hexlet.code.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.dto.LoginDto;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DBRider
@DataSet(value = "users.yml", cleanAfter = true, transactional = true)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void login() throws Exception {

        final LoginDto loginDto = new LoginDto(
                "John@gmail.com",
                "123"
        );
        String content = TestUtils.asJson(loginDto);

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        MockMvcRequestBuilders.post(AuthController.LOGIN)
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
        String content = TestUtils.asJson(loginDto);

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        MockMvcRequestBuilders.post(AuthController.LOGIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);

    }
}
