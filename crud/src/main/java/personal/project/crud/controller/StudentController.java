package personal.project.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import personal.project.rest.filter.StudentFilter;
import personal.project.rest.model.Student;
import personal.project.rest.page.PaginatedStudent;
import personal.project.rest.service.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    StudentService service;

    @GetMapping(value = "/all")
    public Flux<Student> showAllStudents(){
        return service.fetchAllStudents();
    }

    @GetMapping(value = "/find/{_id}")
    public Mono<Student> showStudentById(@PathVariable String _id){
        return service.fetchById(_id);
    }

    @PostMapping(value = "/add")
    public Mono<Student> addNewStudent(@RequestParam Student student){
        return service.saveStudentDetails(student);
    }

    @PutMapping(value = "/update")
    public Mono<Student> updateStudentDetails(@RequestParam Student student){
        return service.updateStudentDetails(student);
    }

    @GetMapping(value = "/filter")
    public Mono<PaginatedStudent> filterStudent(@RequestParam StudentFilter studentFilter){
        return service.filterStudentsData(studentFilter);
    }
}
