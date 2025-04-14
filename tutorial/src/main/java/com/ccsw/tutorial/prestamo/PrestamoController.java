package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Prestamo", description = "API of Prestamo")
@RequestMapping(value = "/prestamo")
@RestController
@CrossOrigin(origins = "*")
public class PrestamoController {

    @Autowired
    PrestamoService prestamoService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar una lista de {@link Prestamo}
     *
     * @param idClient título del juego
     * @param idGame PK de la categoría
     * @return {@link List} de {@link PrestamoDto}
     */
    //    @Operation(summary = "Find", description = "Method that return a filtered list of Prestamos")
    //    @RequestMapping(path = "", method = RequestMethod.GET)
    //    public List<PrestamoDto> find(@RequestParam(value = "idClient", required = false) Long idClient, @RequestParam(value = "idGame", required = false) Long idGame, @RequestParam(value = "initdate", required = false) LocalDate initDate) {
    //
    //        List<Prestamo> prestamos = prestamoService.find(idClient, idGame, initDate);
    //
    //        return prestamos.stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList());
    //    }

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     *
     * @param  dto de búsqueda
     * @return {@link Page} de {@link PrestamoDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Prestamos")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<PrestamoDto> findPage(@RequestBody PrestamoSearchDto dto, @RequestParam(value = "idClient", required = false) Long idClient, @RequestParam(value = "idGame", required = false) Long idGame,
            @RequestParam(value = "initdate", required = false) LocalDate initDat) {

        Page<Prestamo> page = this.prestamoService.findPage(idGame, idClient, initDat, dto);
        // Verifica que la página no es nula
        if (page == null || page.getContent().isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crean un {@link Prestamo}
     *
     * @param id PK de la entidad
     * @param data datos de la entidad
     */
    @Operation(summary = "Save", description = "Method that saves a Prestamo")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody PrestamoDto data) {
        prestamoService.save(id, data);
    }

    /**
     * Método para eliminar un {@link Prestamo}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Prestamo")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.prestamoService.delete(id);
    }
}
