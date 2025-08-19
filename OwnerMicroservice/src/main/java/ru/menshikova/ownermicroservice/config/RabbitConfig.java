package ru.menshikova.ownermicroservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfig {
    public static final String OWNER_LIST_QUEUE       = "owner.list";
    public static final String OWNER_GET_QUEUE        = "owner.get";
    public static final String OWNER_FIND_BY_NAME_QUEUE = "owner.findByName";
    public static final String OWNER_CREATE_QUEUE     = "owner.create";
    public static final String OWNER_UPDATE_QUEUE     = "owner.update";
    public static final String OWNER_DELETE_QUEUE     = "owner.delete";

    @Bean Queue listQueue()       { return new Queue(OWNER_LIST_QUEUE); }
    @Bean Queue getQueue()        { return new Queue(OWNER_GET_QUEUE); }
    @Bean Queue findByNameQueue() { return new Queue(OWNER_FIND_BY_NAME_QUEUE); }
    @Bean Queue createQueue()     { return new Queue(OWNER_CREATE_QUEUE); }
    @Bean Queue updateQueue()     { return new Queue(OWNER_UPDATE_QUEUE); }
    @Bean Queue deleteQueue()     { return new Queue(OWNER_DELETE_QUEUE); }

    @Bean Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
}