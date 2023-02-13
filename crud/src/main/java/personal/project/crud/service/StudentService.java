package personal.project.crud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import personal.project.crud.controller.WebSocketController;
import personal.project.crud.exceptions.BAD_REQUEST_EXCEPTION;
import personal.project.crud.filter.StudentFilter;
import personal.project.crud.model.Student;
import personal.project.crud.page.PaginatedStudent;
import personal.project.crud.repository.StudentFilterRepository;
import personal.project.crud.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentFilterRepository studentFilterRepository;

    @Autowired
    WebSocketController webSocketController;

    public Mono<String> atHome(){
        webSocketController.WELCOME();
        return Mono.just("Welcome to this Micro-Service");
    }

    public Flux<Student> fetchAllStudents(){
        return studentRepository.findAll()
                .flatMap(student -> {
                    log.info("Found: {}", student);
                    return Flux.just(student);
                });
    }

    public Mono<Student> fetchStudent(String _id){
        return studentRepository.findById(_id)
                .map(student -> {
                    log.info("Resource: " + student);
                    return student;
                });
    }

    public Mono<Student> createStudent(Student student){
        return studentRepository.save(student)
                .map(studentResource -> {
                    log.info("Saved Resource: {}", studentResource);
                    webSocketController.NEW(studentResource);
                    return student;
                });
    }

    public Mono<Student> updateStudent(Student student){
        return studentRepository.findById(student.get_id())
                .switchIfEmpty(Mono.defer(()->{
                    throw new BAD_REQUEST_EXCEPTION("Student Does not exist in the Database!\nPlease Create a new Entry");
                })).flatMap(oldStudentData -> {
                    log.info("Updated Resource Data from : {} to {}", oldStudentData, student);
                    webSocketController.UPDATE(oldStudentData, student);
                    return studentRepository.save(student);
                });
    }

    public Mono<PaginatedStudent> filterStudentData(StudentFilter studentFilter){
        if(studentFilter.getPage() == null || studentFilter.getPage()<1){
            throw new BAD_REQUEST_EXCEPTION("Page CANNOT be LESS THAN 1!");
        }

        if (studentFilter.getCount() == null || studentFilter.getCount() <10 || studentFilter.getCount() > 500){
            throw new BAD_REQUEST_EXCEPTION("Count CANNOT BE LESS THAN 10 OR GREATER THAN 500!");
        }

        Pageable pageable = PageRequest.of(studentFilter.getPage() - 1, studentFilter.getCount(),
                Sort.by("_id"));

        if (studentFilter.getSortOrder() != null && studentFilter.getSortValue() != null){
            pageable = PageRequest.of(studentFilter.getPage()-1, studentFilter.getCount(),
                    Sort.by(getSortingOrders(studentFilter.getSortValue(), studentFilter.getSortOrder())));
        }

        return studentFilterRepository.filterStudents(studentFilter, pageable);
    }

    private List<Sort.Order> getSortingOrders(List<String> values, List<Sort.Direction> orders){
        List<Sort.Order> sorts = new ArrayList<>();

        for (int i=0; i<values.size(); i++){
            sorts.add(new Sort.Order(orders.get(i), values.get(i)));
        }

        return sorts;
    }
}
