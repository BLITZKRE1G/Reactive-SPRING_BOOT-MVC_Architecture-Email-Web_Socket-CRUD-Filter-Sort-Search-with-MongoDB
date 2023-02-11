package personal.project.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import personal.project.crud.filter.StudentFilter;
import personal.project.crud.model.Student;
import personal.project.crud.page.PaginatedStudent;
import personal.project.crud.service.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    StudentService service;

    @PostMapping(value = "/add")
    public Mono<Student> addStudentDetails(@RequestBody Student student){
        return service.createStudent(student);
    }

    @GetMapping(value = "/all")
    public Flux<Student> getAllStudentDetails(){
        return service.fetchAllStudents();
    }

    @GetMapping(value = "/{_id}")
    public Mono<Student> getStudentDetails(@PathVariable String _id){
        return service.fetchStudent(_id);
    }

    @PutMapping(value = "/update")
    public Mono<Student> updateStudentDetails(@RequestBody Student student){
        return service.updateStudent(student);
    }

    @GetMapping(value = "/filter")
    public Mono<PaginatedStudent> filterStudentData(@RequestBody StudentFilter studentFilter){
        return service.filterStudentData(studentFilter);
    }
}
