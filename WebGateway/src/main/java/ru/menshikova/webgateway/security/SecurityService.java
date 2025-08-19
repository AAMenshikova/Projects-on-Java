package ru.menshikova.webgateway.security;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.menshikova.webgateway.config.RabbitConfig;
import ru.menshikova.webgateway.dto.PetDTO;
import ru.menshikova.webgateway.model.User;
import ru.menshikova.webgateway.repository.UserRepository;

@Service("sec")
public class SecurityService {
    private final RabbitTemplate rabbit;
    private final UserRepository users;

    public SecurityService(RabbitTemplate rabbit, UserRepository users) {
        this.rabbit = rabbit;
        this.users  = users;
    }

    public boolean isPetOwner(Authentication auth, Long petId) {
        PetDTO pet = (PetDTO) rabbit.convertSendAndReceive(
                RabbitConfig.PET_GET_QUEUE, petId
        );
        if (pet == null) return false;

        User user = users.findByUsername(auth.getName()).orElse(null);
        return user != null && user.getOwnerId().equals(pet.getOwnerId());
    }

    public boolean isCurrent(Authentication auth, Long ownerId) {
        return users.findByUsername(auth.getName())
                .map(u -> u.getOwnerId().equals(ownerId))
                .orElse(false);
    }
}