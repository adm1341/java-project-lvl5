package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;

@Service
@AllArgsConstructor
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    private final UserService userService;


    public Task createNewTask(final TaskDto dto) {
        final Task newtask = fromDto(dto);
        return taskRepository.save(newtask);
    }


    public Task updateTask(final long id, final TaskDto dto) {
        final Task task = taskRepository.findById(id).get();
        merge(task, dto);
        return taskRepository.save(task);
    }

    private void merge(final Task task, final TaskDto postDto) {
        final Task newTask = fromDto(postDto);
        task.setName(newTask.getName());
        task.setDescription(newTask.getDescription());
        task.setExecutor(newTask.getExecutor());
        task.setTaskStatus(newTask.getTaskStatus());

    }

    private Task fromDto(final TaskDto dto) {
        final User author = userService.getCurrentUser();

        final TaskStatus taskStatus = taskStatusRepository.findById(dto.getTaskStatusId()).get();

        final User executor = userRepository.findById(dto.getExecutorId()).orElse(null);


        return Task.builder()
                .author(author)
                .name(dto.getName())
                .description(dto.getDescription())
                .taskStatus(taskStatus)
                .executor(executor)
                .build();
    }
}
