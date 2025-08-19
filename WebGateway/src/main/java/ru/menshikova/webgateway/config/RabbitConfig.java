package ru.menshikova.webgateway.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfig {
    public static final String OWNER_LIST_QUEUE       = "owner.list";
    public static final String OWNER_GET_QUEUE        = "owner.get";
    public static final String OWNER_FIND_BY_NAME_QUEUE = "owner.findByName";
    public static final String OWNER_CREATE_QUEUE     = "owner.create";
    public static final String OWNER_UPDATE_QUEUE     = "owner.update";
    public static final String OWNER_DELETE_QUEUE     = "owner.delete";

    public static final String PET_LIST_QUEUE         = "pet.list";
    public static final String PET_GET_QUEUE          = "pet.get";
    public static final String PET_FIND_NAME_QUEUE    = "pet.findByName";
    public static final String PET_FIND_BREED_QUEUE   = "pet.findByBreed";
    public static final String PET_FIND_OWNER_QUEUE   = "pet.findByOwner";
    public static final String PET_FIND_COLOR_QUEUE   = "pet.findByColor";
    public static final String PET_CREATE_QUEUE       = "pet.create";
    public static final String PET_UPDATE_QUEUE       = "pet.update";
    public static final String PET_DELETE_QUEUE       = "pet.delete";

    @Bean Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
}