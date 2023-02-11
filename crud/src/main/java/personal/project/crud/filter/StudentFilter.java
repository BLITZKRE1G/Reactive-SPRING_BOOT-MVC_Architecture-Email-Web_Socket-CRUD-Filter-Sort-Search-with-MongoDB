package personal.project.crud.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentFilter {
    private String name, dept;
    private LocalDate enrollmentDateLessThan;
    private LocalDate enrollmentDateGreaterThan;

    //Searching
    private String searchTerm;

    //Pagination
    private Integer page;
    private Integer count;

    private List<String> sortValue;
    private List<Direction> sortOrder;
}
