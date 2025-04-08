package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.time.LocalDate;

public class PrestamoDto {
    private Long id;
    private GameDto game;
    private ClientDto client;
    private LocalDate initDate;
    private LocalDate endDate;

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public GameDto getGame() {
        return game;
    }

    /**
     *
     * @param game
     */
    public void setGame(GameDto game) {
        this.game = game;
    }

    /**
     *
     * @return
     */
    public ClientDto getClient() {
        return client;
    }

    /**
     *
     * @param client
     */
    public void setClient(ClientDto client) {
        this.client = client;
    }

    /**
     *
     * @return
     */
    public LocalDate getInitDate() {
        return initDate;
    }

    /**
     *
     * @param initDate
     */
    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    /**
     *
     * @return
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
