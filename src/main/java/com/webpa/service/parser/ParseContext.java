package com.webpa.service.parser;

import com.webpa.domain.enums.Marketplace;

public record ParseContext(
        Marketplace marketplace,
        String query,
        int page,
        int pageSize
) {
}
