package personal.project.rest.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import personal.project.rest.filter.StudentFilter;
import personal.project.rest.model.Student;
import personal.project.rest.page.PaginatedStudent;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class StudentFilterRepository {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<PaginatedStudent> studentDataFilter(StudentFilter studentFilter, Pageable pageable){
        Query query = new Query();

        if(studentFilter.getDepartment() != null && !studentFilter.getDepartment().isEmpty()){
            Criteria departmentCriteria = Criteria.where("department").in(studentFilter.getDepartment());
            query.addCriteria(departmentCriteria);
        }

        if(studentFilter.getAcademicYear() != null && !studentFilter.getAcademicYear().isEmpty()){
            Criteria academicYearCriteria = Criteria.where("academicYear").in(studentFilter.getAcademicYear());
            query.addCriteria(academicYearCriteria);
        }

        if(studentFilter.getSearchTerm() != null && !studentFilter.getSearchTerm().isEmpty() && !studentFilter.getSearchTerm().isBlank()){
            Criteria searchCriteria = Criteria.where("").orOperator(
                    Criteria.where("name").regex(studentFilter.getSearchTerm(), "i"),
                    Criteria.where("department").regex(studentFilter.getSearchTerm(), "i"),
                    Criteria.where("academicYear").regex(studentFilter.getSearchTerm(), "i"),
                    Criteria.where("enrollmentDate").regex(studentFilter.getSearchTerm(), "i")
            );
            query.addCriteria(searchCriteria);
        }

        query.collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary().excludeCase()));
        log.info(query.toString());

        return reactiveMongoTemplate.find(query, Student.class, "student").count().flatMap(count -> {
            PaginatedStudent paginatedStudent = new PaginatedStudent();
            paginatedStudent.setTotalCount(count);

            if (pageable != null){
                return reactiveMongoTemplate.find(query.with(pageable), Student.class, "student").collectList().flatMap(students -> {
                    paginatedStudent.setStudentList(students);
                    return Mono.just(paginatedStudent);
                });
            }

            return reactiveMongoTemplate.find(query, Student.class, "student").collectList().flatMap(students -> {
                paginatedStudent.setStudentList(students);
                return Mono.just(paginatedStudent);
            });
        });
    }
}
