package hexlet.code.app.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet({"users.yml", "taskStatus.yml"})
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    private TestUtils utils;

    @Test
    void testCreateTask() throws Exception {

        final TaskDto taskDto = new TaskDto();
        taskDto.setName("Новая задача");
        taskDto.setDescription("Описание новой задачи");
        taskDto.setExecutorId(1L);
        taskDto.setTaskStatusId(1L);

        String content = utils.asJson(taskDto);

        TaskStatus actualStatus = taskStatusRepository.getById(1L);
        System.out.println(actualStatus);

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                post(TASK_CONTROLLER_PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);
    }

    @Test
    void tesUnCreateStatus() throws Exception {
        String content = "{\"name\": \"\"}";

        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                post(TASK_STATUS_CONTROLLER_PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
    }
//
//    @Test
//    void testShowStatusById() throws Exception {
//        MockHttpServletResponse response2 = mockMvc
//                .perform(
//                        utils.setJWTToken(
//                                get(TASK_STATUS_CONTROLLER_PATH + "/1"),
//                                "John@gmail.com")
//                )
//                .andReturn()
//                .getResponse();
//
//        assertThat(response2.getStatus()).isEqualTo(200);
//    }
//
//    @Test
//    void testUpdateStatus() throws Exception {
//        String content = "{\"name\": \"НеНовый\"}";
//        MockHttpServletResponse responsePost = mockMvc
//                .perform(
//                        utils.setJWTToken(
//                                put(TASK_STATUS_CONTROLLER_PATH + "/1")
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .content(content),
//                                "John@gmail.com")
//                )
//                .andReturn()
//                .getResponse();
//
//        assertThat(responsePost.getStatus()).isEqualTo(200);
//
//
//        TaskStatus actualStatus = taskStatusRepository.getById(1L);
//        assertThat(actualStatus).isNotNull();
//        assertThat(actualStatus.getName()).isEqualTo("НеНовый");
//
//    }
//
//    @Test
//    void testDeleteUserById() throws Exception {
//        MockHttpServletResponse response2 = mockMvc
//                .perform(
//                        utils.setJWTToken(
//                                delete(TASK_STATUS_CONTROLLER_PATH + "/2"),
//                                "John@gmail.com")
//                )
//                .andReturn()
//                .getResponse();
//
//        assertThat(response2.getStatus()).isEqualTo(200);
//
//        assertThat(taskStatusRepository.existsById(2L)).isFalse();
//    }
}
