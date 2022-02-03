package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"users.yml", "taskStatus.yml", "task.yml", "labels.yml"}, cleanAfter = true, transactional = true)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private TestUtils utils;

    @Test
    void testCreateTask() throws Exception {

        final TaskDto taskDto = new TaskDto();
        taskDto.setName("Новая задача");
        taskDto.setDescription("Описание новой задачи");
        taskDto.setExecutorId(1L);
        taskDto.setTaskStatusId(1L);

        String content = TestUtils.asJson(taskDto);

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
    void tesUnCreateTask() throws Exception {
        final TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Описание новой задачи");
        taskDto.setExecutorId(1L);
        taskDto.setTaskStatusId(1L);

        String content = TestUtils.asJson(taskDto);

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

        assertThat(responsePost.getStatus()).isEqualTo(422);
    }

    @Test
    void testShowTaskById() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                get(TASK_CONTROLLER_PATH + "/1"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);
    }

    @Test
    void testUpdateTask() throws Exception {
        final TaskDto taskDto = new TaskDto();
        taskDto.setName("НеНовый");
        taskDto.setDescription("Описание новой задачи");
        taskDto.setExecutorId(1L);
        taskDto.setTaskStatusId(1L);

        String content = TestUtils.asJson(taskDto);
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        utils.setJWTToken(
                                put(TASK_CONTROLLER_PATH + "/1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        Task actualTask = taskRepository.findById(1L).get();
        assertThat(actualTask).isNotNull();
        assertThat(actualTask.getName()).isEqualTo("НеНовый");

    }

    @Test
    void testDeleteTaskById() throws Exception {
        MockHttpServletResponse response2 = mockMvc
                .perform(
                        utils.setJWTToken(
                                delete(TASK_CONTROLLER_PATH + "/2"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);

        assertThat(taskRepository.existsById(2L)).isFalse();
    }

    @Test
    void testFilterByStatusAndLabelAndExecutor() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(
                        utils.setJWTToken(
                                get(TASK_CONTROLLER_PATH + "?taskStatus=1&executorId=1&labels=1"),
                                "John@gmail.com")
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        List<Task> actualUsers = TestUtils.fromJson(response.getContentAsString(),
                new TypeReference<List<Task>>() {
                });

        assertThat(actualUsers.size()).isEqualTo(1);

    }
}
