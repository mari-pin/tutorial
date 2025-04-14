package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrestamoIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/prestamo";
    private static final int PAGE_SIZE = 5;
    private static final long TOTAL_PRESTAMOS = 9L;
    private static final long EXISTS_GAME_ID = 1L;
    private static final long NOT_EXISTS_GAME_ID = 0L;
    private static final long EXISTS_CLIENT_ID = 2L;
    private static final long NOT_EXISTS_CLIENT_ID = 0L;
    private static final LocalDate EXISTS_DATE = LocalDate.of(2025, 4, 15);
    private static final LocalDate EXISTS_DATE_2 = LocalDate.of(2025, 4, 12);
    private static final LocalDate NOT_EXISTS_DATE = LocalDate.of(2025, 8, 2);
    private static final long DELETE_PRESTAMO_ID = 1L;

    private static final String GAME_ID_PARAM = "gameId";
    private static final String CLIENT_ID_PARAM = "clientId";

    private static final String DATE_PARAM = "date";

    @LocalServerPort

    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, "{" + GAME_ID_PARAM + "}").queryParam(CLIENT_ID_PARAM, "{" + CLIENT_ID_PARAM + "}").queryParam(DATE_PARAM, "{" + DATE_PARAM + "}")
                .encode().toUriString();
    }

    ParameterizedTypeReference<ResponsePage<PrestamoDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<PrestamoDto>>() {
    };
    ParameterizedTypeReference<List<PrestamoDto>> responseType = new ParameterizedTypeReference<List<PrestamoDto>>() {
    };

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {
        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {
        long elementsCount = TOTAL_PRESTAMOS - PAGE_SIZE;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void findWithoutFiltersShouldReturnAllPrestamosInDB() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    public void findNotExistsGameShouldReturnEmpty() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    public void findExistsClientShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsClientShouldReturnEmpty() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsDateShouldReturnPrestamos() {

        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), EXISTS_DATE);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsDateShouldReturnEmpty() {

        int LOANS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientShouldReturnPrestamos() {

        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    public void findExistsGameAndDateShouldReturPrestamos() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), EXISTS_DATE_2);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsClientAndDateShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE_2);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientAndDateShouldReturnPrestamos() {
        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE_2);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsGameOrClientOrDateShouldReturnEmpty() {

        int PRESTAMOS_WITH_FILTER = 9;

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

        params.put((GAME_ID_PARAM), NOT_EXISTS_GAME_ID);
        params.put((CLIENT_ID_PARAM), NOT_EXISTS_CLIENT_ID);
        params.put((DATE_PARAM), NOT_EXISTS_DATE);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getTotalElements());

    }

    @Test
    void saveWithoutIdShouldCreateNewPrestamo() {
        long TOTAL_PRESTAMOS = 9;

        long NEW_PRESTAMO_SIZE = TOTAL_PRESTAMOS + 1;
        long NEW_PRESTAMO_ID = TOTAL_PRESTAMOS + 1;
        long GAME_ID_INSERTED = 6L;

        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(GAME_ID_INSERTED);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(3L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setInitDate(LocalDate.of(2025, 4, 10));
        dto.setEndDate(LocalDate.of(2025, 4, 22));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        Map<String, Object> params = new HashMap<>();
        params.put((GAME_ID_PARAM), null);
        params.put((CLIENT_ID_PARAM), null);
        params.put((DATE_PARAM), null);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(1, (int) NEW_PRESTAMO_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(NEW_PRESTAMO_SIZE, response.getBody().getTotalElements());

        PrestamoDto data = response.getBody().getContent().stream().filter(item -> item.getId().equals(NEW_PRESTAMO_ID)).findFirst().orElse(null);
        assertNotNull(data);
        assertEquals(GAME_ID_INSERTED, data.getGame().getId());

    }

    @Test
    void deleteWithExistsIdShouldDeletePrestamo() {
        long NEW_PRESTAMO_SIZE = TOTAL_PRESTAMOS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_PRESTAMO_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(NEW_PRESTAMO_SIZE, response.getBody().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteLoanId = TOTAL_PRESTAMOS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteLoanId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findAllShouldReturnAllPrestamos() {

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        System.out.printf(String.valueOf(response.getBody()), new Object[] {});
        assertEquals(TOTAL_PRESTAMOS, response.getBody().size());
    }
}
