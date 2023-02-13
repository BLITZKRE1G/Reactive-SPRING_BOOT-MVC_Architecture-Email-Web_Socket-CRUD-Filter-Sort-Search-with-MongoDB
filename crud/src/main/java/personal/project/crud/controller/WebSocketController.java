package personal.project.crud.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.crud.model.Student;
import reactor.core.publisher.Sinks;

@RestController
public class WebSocketController {

    private final Sinks.Many<String> sinks;

    public WebSocketController(Sinks.Many<String> sinks) {
        this.sinks = sinks;
    }

    @PostMapping(value = "/welcome")
    public void WELCOME(){
        sinks.emitNext("Welcome to this MicroService", Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @PostMapping(value = "/new")
    public void NEW(Student student){
        sinks.emitNext("A New Student was added to the Database!\nDetails: " + student, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @PostMapping(value = "/update")
    public void UPDATE(Student oldDetails, Student newDetails){
        sinks.emitNext("Student details were updated\nOld Details: " + oldDetails +
                "\nNew Details: " + newDetails, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
