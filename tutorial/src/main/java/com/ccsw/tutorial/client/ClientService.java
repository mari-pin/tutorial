package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

import java.util.List;

/**
 * author ccsw
 */

public interface ClientService {

    /**
     * Metodo parar recuperar todas los clientes
     *
     * @return {@link List} de {@link Client}
     **/
    List<Client> findAll();

    /**
     * Método para crear o actualizar un {@link Client}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, ClientDto dto) throws Exception;

    /**
     * Método para borrar un {@link  Client}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    Client get(Long id);

}
