package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoService {
    /**
     * Recupera la lista de prestamos
     * @return {@link List} de {@link Prestamo}
     */
    List<Prestamo> findAll();

    /**
     * Guarda un prestamo
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, PrestamoDto dto);

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findPage(Long gameId, Long clientId, LocalDate date, PrestamoSearchDto dto);

    /**
     *
     * @param id
     * @throws Exception
     */
    void delete(Long id) throws Exception;

    /**
     *
     * @param dto
     * @return
     */
    boolean areGamesOverlapping(PrestamoDto dto);

    /**
     *
     * @param dto
     * @return
     */
    boolean areClientOverlapping(PrestamoDto dto);

    Page<Prestamo> findPage(PrestamoSearchDto dto);

    List<Prestamo> find(Long idClient, Long idGame, LocalDate initDate);
}
