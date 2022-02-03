package hexlet.code.app.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";

    private static final String ONLY_AUTHOR_BY_ID = """
                @TaskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
            """;

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Operation(summary = "Get All tasks")
    @GetMapping
    public Iterable<Task> getAll(@QuerydslPredicate(root = Task.class) Predicate predicate,
                                 @RequestParam(required = false) Long taskStatus,
                                 @RequestParam(required = false) Long executorId,
                                 @RequestParam(required = false) Long labels,
                                 @RequestParam(required = false) String authorId) {
        return taskRepository.findAll(predicate);
    }

    @Operation(summary = "Get task by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task found"),
            @ApiResponse(responseCode = "404", description = "task with that id not found")
    })
    @GetMapping(ID)
    public Task getById(@PathVariable final Long id) {
        return taskRepository.findById(id).get();
    }

    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "task created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Task createNewTask(@RequestBody @Valid final TaskDto dto) {
        return taskService.createNewTask(dto);
    }

    @Operation(summary = "Update task")
    @ApiResponse(responseCode = "200", description = "task updated")
    @PutMapping(ID)
    public Task updateTask(@PathVariable final Long id,
                           // Schema используется, чтобы указать тип данных для параметра
                           @Parameter(schema = @Schema(implementation = TaskDto.class))
                           @RequestBody @Valid final TaskDto dto) {
        return taskService.updateTask(id, dto);
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task deleted"),
            @ApiResponse(responseCode = "404", description = "task with that id not found")
    })
    @DeleteMapping(ID)
    public void deleteTask(@PathVariable final Long id) {
        taskRepository.deleteById(id);
    }
}
