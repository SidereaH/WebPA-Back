package com.webpa.repository;

import com.webpa.domain.ProductCard;
import com.webpa.domain.enums.Marketplace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductCardRepository extends JpaRepository<ProductCard, UUID>, JpaSpecificationExecutor<ProductCard> {

    Page<ProductCard> findByParseTaskId(UUID taskId, Pageable pageable);

    @Modifying
    @Query("delete from ProductCard p where lower(p.queryText) = lower(:query) and p.marketplace in :marketplaces")
    void deleteByQueryAndMarketplaces(@Param("query") String query, @Param("marketplaces") List<Marketplace> marketplaces);

    @Query("""
            select p from ProductCard p
            where p.marketplace in :marketplaces
              and (
                    :query is null
                 or lower(p.queryText) like lower(concat('%', :query, '%'))
                 or lower(p.name) like lower(concat('%', :query, '%'))
              )
            """)
    Page<ProductCard> search(
            @Param("marketplaces") List<Marketplace> marketplaces,
            @Param("query") String query,
            Pageable pageable
    );
}
