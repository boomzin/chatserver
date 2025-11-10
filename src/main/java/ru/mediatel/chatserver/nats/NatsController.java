package ru.mediatel.chatserver.nats;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class NatsController {

    private Connection connection;

    public NatsController(Connection connection) {
        this.connection = connection;
    }

    @PostConstruct
    public void subscribe() {
        log.info("Subscribing to nats ...");
        Dispatcher dispatcher = connection.createDispatcher(this::doMassage);

        dispatcher.subscribe("mcpe", "mcpe");
    }

    public void doMassage(Message msg) {
        System.out.printf("[%s]: %s\n", Thread.currentThread().getName(), msg);
    }
}
