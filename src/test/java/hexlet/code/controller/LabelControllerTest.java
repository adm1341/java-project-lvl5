package hexlet.code.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"users.yml", "taskStatus.yml", "task.yml", "labels.yml"}, cleanAfter = true, transactional = true)
public class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestUtils utils;

    @Test
    void testCreateLabel() throws Exception {
        String content = "{\"name\": \"<баг>\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                post(LABEL_CONTROLLER_PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);
    }

    @Test
    void tesUnCreateLabel() throws Exception {
        String content = "{\"name\": \"\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                post(LABEL_CONTROLLER_PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
    }

    @Test
    void testShowLabelById() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                get(LABEL_CONTROLLER_PATH + "/1"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);
    }

    @Test
    void testUpdateLabel() throws Exception {
        String content = "{\"name\": \"НеНовый\"}";
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                put(LABEL_CONTROLLER_PATH + "/1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);


        Label actuallabel = labelRepository.findById(1L).get();
        assertThat(actuallabel).isNotNull();
        assertThat(actuallabel.getName()).isEqualTo("НеНовый");

    }

    @Test
    void testDeleteLabelById() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                delete(LABEL_CONTROLLER_PATH + "/2"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);

        assertThat(labelRepository.existsById(2L)).isFalse();
    }
}
