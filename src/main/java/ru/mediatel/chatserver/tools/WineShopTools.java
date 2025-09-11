package ru.mediatel.chatserver.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WineShopTools {

    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .defaultHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8")
            .build();
    private static final String BASE_URL = "http://localhost:8070/api/rest/db_service/v1";

    @Tool("Список вин в наличии с фильтрацией. Можно указать: description (поиск по описанию), price_le, price_ge, quantity_ge. Если параметр фильтра не нуже не используй его. Если спрашивают не алкоголь то переспроси")
    public String getAvailableWines(
            @P(value = "Поиск по описанию", required = false) String description,
            @P(value = "Максимальная цена", required = false) Integer priceLe,
            @P(value = "Минимальная цена", required = false) Integer priceGe,
            @P(value = "Минимальное количество на складе", required = false) Integer quantityGe,
            @P(value = "Максимальное количество на складе", required = false) Integer quantityLe
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/products");
        if (description != null) builder.queryParam("description", description);
        if (priceLe != null) builder.queryParam("price_le", priceLe);
        if (priceGe != null) builder.queryParam("price_ge", priceGe);
        if (quantityLe != null) builder.queryParam("quantity_le", quantityLe);
        if (quantityGe != null) builder.queryParam("quantity_ge", quantityGe);

        URI url = builder.build().toUri();
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    @Tool("Добавить новое вино на склад. Укажи название, цену и количество. Верни результат операции в виде строки")
    public String addWine(
            @P("Описание вина") String description,
            @P("Цена") Integer price,
            @P("Количество") Integer quantity
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("description", description);
        body.put("price", price);
        body.put("quantity", quantity);

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL + "/products",
                body,
                String.class
        );

        return "Статус: " + response.getStatusCode() + ", тело ответа: " + response.getBody();
    }
}