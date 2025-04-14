package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

public class PrestamoSearchDto {
    private PageableRequest pageable;

    public PrestamoSearchDto() {
        this.pageable = new PageableRequest(); // Inicializa pageable con un nuevo objeto
    }

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}
