package personal.project.rest.filter;

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
    private String department, academicYear;
    private List<String> status;

    //Searching
    private String searchTerm;

    //Pagination
    private Integer page;
    private Integer count;

    //Sorting
    private List<String> sortValue;
    private List<Direction> sortOrder;
}
