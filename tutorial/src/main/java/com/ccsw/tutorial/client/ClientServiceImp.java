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
    public void save(Long id, ClientDto dto) throws Exception {
        Client client;
        boolean exist;

        if (id == null) {
            exist = this.clientRepository.existsByName(dto.getName());
            if (exist) {
                //lanza la excepcion si el cliente exixte
                throw new Exception("el nombre ya existe");
            }
            //si no existe lo crea y lo guarda
            client = new Client();
            client.setName(dto.getName());
            this.clientRepository.save(client);

        } else {
            client = this.clientRepository.findById(id).orElse(null);
            if (client != null) {
                client.setName(dto.getName());
                this.clientRepository.save(client);
            } else {
                throw new Exception("el cliente ni se ha encontrado");
            }

        }

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

    public Client get(Long Id) {
        return this.clientRepository.findById(Id).orElse(null);
    }
}
