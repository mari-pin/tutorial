package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PrestamoServiceImp implements PrestamoService {
    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;
    private GameDto data;

    @Override
    public List<Prestamo> find(Long idClient, Long idGame, LocalDate fechaInicio) {
        PrestamoSpecification clientSpec = new PrestamoSpecification(new SearchCriteria("client.id", ":", idClient));
        PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", idGame));
        PrestamoSpecification fechaSpec = new PrestamoSpecification(new SearchCriteria("initDate", ">", fechaInicio));

        Specification<Prestamo> spec = Specification.where(clientSpec).and(gameSpec).and(fechaSpec);

        return this.prestamoRepository.findAll(spec);
    }

    /**
     *
     * @param gameId
     * @param clientId
     * @param date
     * @param dto dto de búsqueda
     * @return
     */
    @Override
    public Page<Prestamo> findPage(Long gameId, Long clientId, LocalDate date, PrestamoSearchDto dto) {
        PrestamoSpecification clientSpec = new PrestamoSpecification(new SearchCriteria("client.id", ":", clientId));
        PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", gameId));
        PrestamoSpecification fechaInicioSpec = new PrestamoSpecification(new SearchCriteria("initDate", "<=", date));
        PrestamoSpecification fechaFinSpec = new PrestamoSpecification(new SearchCriteria("endDate", ">=", date));

        Specification<Prestamo> spec = Specification.where(clientSpec).and(gameSpec).and(fechaInicioSpec).and(fechaFinSpec);

        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());

    }

    //    @Override
    //    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
    //        return this.prestamoRepository.findAll(dto.getPageable().getPageable());
    //    }

    @Override
    public void save(Long id, PrestamoDto data) {
        Prestamo prestamo;
        Boolean juegoPrestado;
        juegoPrestado = this.findByGame(data.getGame(), data.getInitDate(), data.getEndDate(), data);

        Boolean clienteJuego;
        clienteJuego = this.findyByClientGame(data.getClient(), data);

        if (id == null) {

            if (data.getInitDate().isBefore(data.getInitDate())) {
                System.out.println("La fecha de fin no puede ser anterior a la fecha de inicio");
            }
            if (!juegoPrestado) {
                System.out.println("Ya hay un juego prestado");
            }
            if (clienteJuego) {
                System.out.println("Ya has reservado mas de 2 juegos");
            } else {
                prestamo = new Prestamo();
                BeanUtils.copyProperties(data, prestamo, "id", "game", "client");

                prestamo.setGame(gameService.get(data.getGame().getId()));
                prestamo.setClient(clientService.get(data.getClient().getId()));

                this.prestamoRepository.save(prestamo);
            }

        } else {
            prestamo = this.prestamoRepository.findById(id).orElse(null);
            BeanUtils.copyProperties(data, prestamo, "id", "game", "client");

            prestamo.setGame(gameService.get(data.getGame().getId()));
            prestamo.setClient(clientService.get(data.getClient().getId()));

            this.prestamoRepository.save(prestamo);
        }

    }

    @Override
    public void delete(Long id) throws Exception {

        if (this.prestamoRepository.findById(id).orElse(null) == null) {
            throw new Exception("No existe");
        }

        this.prestamoRepository.deleteById(id);
    }

    public boolean findByGame(GameDto game, LocalDate fechaInicio, LocalDate endDate, PrestamoDto dto) {
        List<Prestamo> prestamosExistentes = find(null, game.getId(), null);
        if (!prestamosExistentes.isEmpty()) {
            if (!(dto.getInitDate().isBefore(fechaInicio) || fechaInicio.isAfter(dto.getEndDate()))) {
                return false;
            }
        }
        return true;
    }

    public boolean findyByClientGame(ClientDto client, PrestamoDto dto) {
        List<Prestamo> prestamosCliente = find(client.getId(), null, dto.getInitDate());

        if (prestamosCliente.size() >= 2) {
            return true;
        }
        return false;
    }

}