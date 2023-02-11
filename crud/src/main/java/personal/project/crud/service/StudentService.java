package personal.project.crud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.crud.exceptions.BAD_REQUEST_EXCEPTION;
import personal.project.crud.model.Student;
import personal.project.crud.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

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
                    return student;
                });
    }

    public Mono<Student> updateStudent(Student student){
        return studentRepository.findById(student.get_id())
                .switchIfEmpty(Mono.defer(()->{
                    throw new BAD_REQUEST_EXCEPTION("Student Does not exist in the Database!\nPlease Create a new Entry");
                })).flatMap(oldStudentData -> {
                    log.info("Updated Resource Data from : {} to {}", oldStudentData, student);
                    return studentRepository.save(student);
                });
    }
}
