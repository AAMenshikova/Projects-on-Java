package ru.menshikova.petmicroservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.menshikova.petmicroservice.config.RabbitConfig;
import ru.menshikova.petmicroservice.dto.PetDTO;
import ru.menshikova.petmicroservice.model.Color;
import ru.menshikova.petmicroservice.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Component
public class PetMessageListener {
    @Autowired
    private PetService svc;

    @RabbitListener(queues = RabbitConfig.PET_LIST_QUEUE)
    public Page<PetDTO> list(Map<String,Integer> req) {
        return svc.getAllPets(req.get("page"), req.get("size"));
    }

    @RabbitListener(queues = RabbitConfig.PET_GET_QUEUE)
    public PetDTO get(Long id) {
        return svc.getById(id).orElse(null);
    }

    @RabbitListener(queues = RabbitConfig.PET_FIND_NAME_QUEUE)
    public List<PetDTO> findByName(String name) {
        return svc.getByName(name);
    }

    @RabbitListener(queues = RabbitConfig.PET_FIND_BREED_QUEUE)
    public List<PetDTO> findByBreed(String breed) {
        return svc.getByBreed(breed);
    }

    @RabbitListener(queues = RabbitConfig.PET_FIND_OWNER_QUEUE)
    public List<PetDTO> findByOwner(Long ownerId) {
        return svc.getByOwnerId(ownerId);
    }

    @RabbitListener(queues = RabbitConfig.PET_FIND_COLOR_QUEUE)
    public Page<PetDTO> findByColor(Map<String,Object> req) {
        Color color = Color.valueOf((String)req.get("color"));
        int page = (Integer)req.get("page"), size = (Integer)req.get("size");
        return svc.getByColor(color, page, size);
    }

    @RabbitListener(queues = RabbitConfig.PET_CREATE_QUEUE)
    public PetDTO create(PetDTO dto) {
        return svc.create(dto);
    }

    @RabbitListener(queues = RabbitConfig.PET_UPDATE_QUEUE)
    public PetDTO update(Map<String,Object> req) {
        Long id = ((Number)req.get("id")).longValue();
        PetDTO dto = (PetDTO) req.get("dto");
        return svc.update(id, dto).orElse(null);
    }

    @RabbitListener(queues = RabbitConfig.PET_DELETE_QUEUE)
    public Boolean delete(Long id) {
        return svc.delete(id);
    }
}