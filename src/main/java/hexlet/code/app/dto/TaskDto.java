package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    private Long executorId;

    private Long taskStatusId;
}
