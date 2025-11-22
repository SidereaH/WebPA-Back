package com.webpa.dto;

import com.webpa.domain.enums.Marketplace;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateParseTaskRequest(
        @NotBlank(message = "Поисковый запрос обязателен")
        String query,

        @NotEmpty(message = "Укажите хотя бы одну площадку")
        List<Marketplace> marketplaces,

        @Min(value = 1, message = "Минимум 1 страница")
        @Max(value = 50, message = "Не больше 50 страниц за раз")
        int pages,

        @Min(value = 1, message = "Размер страницы не может быть меньше 1")
        @Max(value = 120, message = "Размер страницы не может быть больше 120")
        int pageSize,

        boolean appendToExisting,

        String comment
) {
}
