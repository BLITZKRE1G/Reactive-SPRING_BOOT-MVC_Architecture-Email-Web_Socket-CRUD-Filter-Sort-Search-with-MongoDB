package personal.project.crud.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import personal.project.crud.model.Student;

@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {
}
