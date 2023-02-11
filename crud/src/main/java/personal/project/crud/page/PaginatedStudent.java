package personal.project.crud.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import personal.project.crud.model.Student;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedStudent {
    private List<Student> studentList;
    private Long totalCount;
}
