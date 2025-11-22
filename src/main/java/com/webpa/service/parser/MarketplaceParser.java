package com.webpa.service.parser;

import com.webpa.domain.enums.Marketplace;
import com.webpa.dto.ParsedProduct;

import java.util.List;

public interface MarketplaceParser {

    Marketplace marketplace();

    List<ParsedProduct> parse(ParseContext context);
}
