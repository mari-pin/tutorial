package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientServiceImp implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     *
     * @return
     */
    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Override
    public void save(Long id, ClientDto dto) {
        Client client;
        if (id == null) {
            client = new Client();

        } else {
            client = this.clientRepository.findById(id).orElse(null);
        }
        client.setName(dto.getName());
        this.clientRepository.save(client);
    }

    /**
     *
     * @param id PK de la entidad
     */
    @Override
    public void delete(Long id) throws Exception {
        if (this.clientRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not Exist");
        }
        this.clientRepository.deleteById(id);
    }
}
