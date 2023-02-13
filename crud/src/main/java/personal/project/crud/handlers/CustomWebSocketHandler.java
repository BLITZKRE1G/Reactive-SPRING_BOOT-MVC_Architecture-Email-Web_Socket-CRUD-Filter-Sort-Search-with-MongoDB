package personal.project.crud.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class CustomWebSocketHandler implements WebSocketHandler {

    private final Sinks.Many<String> sinks;

    public CustomWebSocketHandler(Sinks.Many<String> sinks) {
        this.sinks = sinks;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var serverFlux= sinks.asFlux()
                .map(session::textMessage);
        return session.send(serverFlux);
    }
}
