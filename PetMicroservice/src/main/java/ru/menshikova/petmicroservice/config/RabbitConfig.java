package ru.menshikova.petmicroservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfig {
    public static final String PET_LIST_QUEUE        = "pet.list";
    public static final String PET_GET_QUEUE         = "pet.get";
    public static final String PET_FIND_NAME_QUEUE   = "pet.findByName";
    public static final String PET_FIND_BREED_QUEUE  = "pet.findByBreed";
    public static final String PET_FIND_OWNER_QUEUE  = "pet.findByOwner";
    public static final String PET_FIND_COLOR_QUEUE  = "pet.findByColor";
    public static final String PET_CREATE_QUEUE      = "pet.create";
    public static final String PET_UPDATE_QUEUE      = "pet.update";
    public static final String PET_DELETE_QUEUE      = "pet.delete";

    @Bean Queue listQueue()       { return new Queue(PET_LIST_QUEUE); }
    @Bean Queue getQueue()        { return new Queue(PET_GET_QUEUE); }
    @Bean Queue findNameQueue()   { return new Queue(PET_FIND_NAME_QUEUE); }
    @Bean Queue findBreedQueue()  { return new Queue(PET_FIND_BREED_QUEUE); }
    @Bean Queue findOwnerQueue()  { return new Queue(PET_FIND_OWNER_QUEUE); }
    @Bean Queue findColorQueue()  { return new Queue(PET_FIND_COLOR_QUEUE); }
    @Bean Queue createQueue()     { return new Queue(PET_CREATE_QUEUE); }
    @Bean Queue updateQueue()     { return new Queue(PET_UPDATE_QUEUE); }
    @Bean Queue deleteQueue()     { return new Queue(PET_DELETE_QUEUE); }

    @Bean Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
}