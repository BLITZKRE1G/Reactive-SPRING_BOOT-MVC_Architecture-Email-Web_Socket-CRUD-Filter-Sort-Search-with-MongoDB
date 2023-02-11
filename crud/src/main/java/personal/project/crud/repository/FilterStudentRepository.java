package personal.project.crud.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import personal.project.crud.page.PaginatedStudent;
import personal.project.crud.filter.StudentFilter;
import personal.project.crud.model.Student;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class StudentFilterRepository {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<PaginatedStudent> filterStudents(StudentFilter studentFilter, Pageable pageable){
        Query query = new Query();

        if (studentFilter.getName() != null && !studentFilter.getName().isBlank() && !studentFilter.getName().isEmpty()){
            Criteria nameCriteria = Criteria.where("name").in(studentFilter.getName());
            query.addCriteria(nameCriteria);
        }

        if(studentFilter.getDept() != null && !studentFilter.getDept().isEmpty() && !studentFilter.getDept().isBlank()){
            Criteria departmentCriteria = Criteria.where("dept").in(studentFilter.getDept());
            query.addCriteria(departmentCriteria);
        }

        if(studentFilter.getEnrollmentDateGreaterThan() != null){
            Criteria dateGreaterThanCriteria = Criteria.where("enrollmentDate").gte(studentFilter.getEnrollmentDateGreaterThan());
            query.addCriteria(dateGreaterThanCriteria);
        }

        if(studentFilter.getEnrollmentDateLessThan() != null){
            Criteria dateLessThanCriteria = Criteria.where("enrollmentDate").lte(studentFilter.getEnrollmentDateLessThan());
            query.addCriteria(dateLessThanCriteria);
        }

        query.collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary().excludeCase()));
        log.info(query.toString());

        return reactiveMongoTemplate.find(query, Student.class, "student").count().flatMap(count -> {
            PaginatedStudent paginatedStudent = new PaginatedStudent();
            paginatedStudent.setTotalCount(count);

            if (pageable != null){
                return reactiveMongoTemplate.find(query.with(pageable), Student.class, "student").collectList()
                        .flatMap(students -> {
                            paginatedStudent.setStudentList(students);
                            return Mono.just(paginatedStudent);
                        });
            }

            return reactiveMongoTemplate.find(query, Student.class, "student").collectList()
                    .flatMap(students -> {
                        paginatedStudent.setStudentList(students);
                        return Mono.just(paginatedStudent);
                    });
        });
    }
}
