package ru.menshikova.ownermicroservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.menshikova.ownermicroservice.config.RabbitConfig;
import ru.menshikova.ownermicroservice.dto.OwnerDTO;
import ru.menshikova.ownermicroservice.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Component
public class OwnerMessageListener {
    @Autowired
    private OwnerService svc;

    @RabbitListener(queues = RabbitConfig.OWNER_LIST_QUEUE)
    public Page<OwnerDTO> list(Map<String,Integer> req) {
        return svc.getAllOwners(req.get("page"), req.get("size"));
    }

    @RabbitListener(queues = RabbitConfig.OWNER_GET_QUEUE)
    public OwnerDTO get(Long id) {
        return svc.getById(id).orElse(null);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_FIND_BY_NAME_QUEUE)
    public List<OwnerDTO> findByName(String name) {
        return svc.getByName(name);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_CREATE_QUEUE)
    public OwnerDTO create(OwnerDTO dto) {
        return svc.create(dto);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_UPDATE_QUEUE)
    public OwnerDTO update(Map<String,Object> req) {
        Long id = ((Number)req.get("id")).longValue();
        OwnerDTO dto = (OwnerDTO) req.get("dto");
        return svc.update(id, dto).orElse(null);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_DELETE_QUEUE)
    public Boolean delete(Long id) {
        return svc.delete(id);
    }
}