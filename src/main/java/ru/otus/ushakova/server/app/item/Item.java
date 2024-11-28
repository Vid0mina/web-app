package ru.otus.ushakova.server.app.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;
    private String title;
    private BigDecimal price;

}