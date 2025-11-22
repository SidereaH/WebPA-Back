package com.webpa.service;

import com.webpa.domain.ParseTask;
import com.webpa.domain.ProductCard;
import com.webpa.domain.enums.Marketplace;
import com.webpa.domain.enums.TaskStatus;
import com.webpa.dto.ParsedProduct;
import com.webpa.repository.ParseTaskRepository;
import com.webpa.repository.ProductCardRepository;
import com.webpa.service.parser.MarketplaceParser;
import com.webpa.service.parser.ParseContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParserOrchestrator {

    private final Map<Marketplace, MarketplaceParser> parserByMarketplace;
    private final AttributeNormalizer normalizer;
    private final ProductCardRepository productCardRepository;
    private final ParseTaskRepository parseTaskRepository;

    public ParserOrchestrator(
            List<MarketplaceParser> parsers,
            AttributeNormalizer normalizer,
            ProductCardRepository productCardRepository,
            ParseTaskRepository parseTaskRepository
    ) {
        this.parserByMarketplace = parsers.stream()
                .collect(Collectors.toUnmodifiableMap(MarketplaceParser::marketplace, parser -> parser));
        this.normalizer = normalizer;
        this.productCardRepository = productCardRepository;
        this.parseTaskRepository = parseTaskRepository;
    }

    @Transactional
    public int executeTask(ParseTask task) {
        log.info("Start parsing task {} for query '{}'", task.getId(), task.getQueryText());
        task.setStatus(TaskStatus.RUNNING);
        task.setStartedAt(Instant.now());
        task.setErrorMessage(null);
        parseTaskRepository.save(task);

        if (!task.isAppendToExisting()) {
            productCardRepository.deleteByQueryAndMarketplaces(task.getQueryText(), task.getMarketplaces());
        }

        int totalSaved = 0;
        try {
            for (Marketplace marketplace : task.getMarketplaces()) {
                MarketplaceParser parser = parserByMarketplace.get(marketplace);
                if (parser == null) {
                    log.warn("Parser for {} is not registered; skipping", marketplace);
                    continue;
                }
                for (int page = 1; page <= task.getPagesToScan(); page++) {
                    List<ParsedProduct> parsed = parser.parse(new ParseContext(marketplace, task.getQueryText(), page, task.getPageSize()));
                    List<ProductCard> cards = parsed.stream()
                            .map(product -> toEntity(product, task))
                            .toList();
                    productCardRepository.saveAll(cards);
                    totalSaved += cards.size();
                    log.info("Marketplace {} page {} parsed {} products", marketplace, page, cards.size());
                }
            }
            task.setStatus(TaskStatus.COMPLETED);
        } catch (Exception ex) {
            log.error("Task {} failed: {}", task.getId(), ex.getMessage(), ex);
            task.setStatus(TaskStatus.FAILED);
            task.setErrorMessage(ex.getMessage());
            throw ex;
        } finally {
            task.setFinishedAt(Instant.now());
            parseTaskRepository.save(task);
        }
        return totalSaved;
    }

    private ProductCard toEntity(ParsedProduct product, ParseTask task) {
        Map<String, String> normalized = normalizer.normalize(product.rawAttributes());
        Map<String, String> mergedNormalized = new HashMap<>(normalized);
        if (product.normalizedAttributes() != null) {
            mergedNormalized.putAll(product.normalizedAttributes());
        }

        ProductCard card = ProductCard.builder()
                .parseTask(task)
                .queryText(task.getQueryText())
                .marketplace(product.marketplace())
                .sourceUrl(product.sourceUrl())
                .name(product.name())
                .price(product.price())
                .imageUrl(product.imageUrl())
                .rating(product.rating())
                .feedbacksCount(product.feedbacksCount())
                .seller(product.seller())
                .supplierRating(product.supplierRating())
                .available(product.available())
                .description(product.description())
                .mainInfo(product.mainInfo())
                .rawAttributes(product.rawAttributes())
                .normalizedAttributes(mergedNormalized)
                .images(product.images())
                .excelFilename(product.excelFilename())
                .build();

        return card;
    }
}
