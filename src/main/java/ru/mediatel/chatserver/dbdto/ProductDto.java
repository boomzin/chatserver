package ru.mediatel.chatserver.dbdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private UUID id;
    private Integer quantity;
    private BigDecimal price;
    private String description;
}

