package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional

public class PrestamoServiceImp implements PrestamoService {
    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    ClientService clientService;
    @Autowired
    GameService gameService;

    @Override
    public List<Prestamo> findAll() {
        return (List<Prestamo>) this.prestamoRepository.findAll();
    }

    @Override
    public void save(Long id, PrestamoDto dto) {
        Prestamo prestamo;

        prestamo = new Prestamo();
        BeanUtils.copyProperties(dto, prestamo, "id", "game", "client");

        prestamo.setGame(gameService.get(dto.getGame().getId()));
        prestamo.setClient(clientService.get(dto.getClient().getId()));

        if (areGamesOverlapping(dto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El juego está prestado en esta fecha");
        } else if (areClientOverlapping(dto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente ya tiene 2 préstamos en estas fechas");
        } else {
            this.prestamoRepository.save(prestamo);
        }
    }

    @Override
    public Page<Prestamo> findPage(Long gameId, Long clientId, LocalDate date, PrestamoSearchDto dto) {
        PrestamoSpecification idGameSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", gameId));
        PrestamoSpecification idClientSpec = new PrestamoSpecification(new SearchCriteria("client.id", ":", clientId));
        PrestamoSpecification dateEndSpec = new PrestamoSpecification(new SearchCriteria("endDate", "lessThanOrEqualTo", date));
        PrestamoSpecification dateStartSpec = new PrestamoSpecification(new SearchCriteria("startDate", "greaterThanOrEqualTo", date));

        Specification<Prestamo> spec = Specification.where(idGameSpec).and(idClientSpec).and(dateEndSpec).and(dateStartSpec);

        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.prestamoRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        }

        this.prestamoRepository.deleteById(id);
    }

    @Override
    public boolean areGamesOverlapping(PrestamoDto dto) {
        Long gameId = dto.getGame().getId();
        Long clientId = dto.getClient().getId();
        LocalDate startDate = dto.getInitDate();
        LocalDate endDate = dto.getEndDate();

        List<Prestamo> prestamos = this.findAll();

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getGame().getId() == gameId) {
                LocalDate endDatePrestamo = prestamo.getEndDate();
                LocalDate initDatePrestamo = prestamo.getInitDate();

                boolean isOverLapping = (startDate.isBefore(endDatePrestamo) && endDate.isAfter(initDatePrestamo) || (initDatePrestamo.isBefore(endDate) && endDatePrestamo.isAfter(startDate)));

                if (isOverLapping) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean areClientOverlapping(PrestamoDto dto) {
        Long clientId = dto.getClient().getId();
        LocalDate initDate = dto.getInitDate();
        LocalDate endDate = dto.getEndDate();

        List<Prestamo> prestamos = this.findAll();

        for (LocalDate date = initDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int prestamoCount = 0;
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getClient().getId().equals(clientId) && !prestamo.getEndDate().isBefore(date) && !prestamo.getInitDate().isAfter(date)) {
                    prestamoCount++;
                }
            }
            if (prestamoCount >= 2) {
                return true;
            }
        }
        return false;
    }

    //implementados ahora

    /**
     *
     * @param dto
     * @return
     */
    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
        return null;
    }

    /**
     *
     * @param idClient
     * @param idGame
     * @param initDate
     * @return
     */
    @Override
    public List<Prestamo> find(Long idClient, Long idGame, LocalDate initDate) {
        return List.of();
    }
}
