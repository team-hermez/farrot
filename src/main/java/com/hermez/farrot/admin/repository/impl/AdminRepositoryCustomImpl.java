package com.hermez.farrot.admin.repository.impl;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.repository.AdminRepositoryCustom;
import com.hermez.farrot.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.hermez.farrot.product.entity.QProduct.product;

@Repository
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryCustomImpl(JdbcTemplate jdbcTemplate, EntityManager em) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AdminCategorySalesTop5Response> findCategorySales() {
        String sql = "SELECT COUNT(*) AS count, c.code AS categoryCode " +
                "FROM product p " +
                "JOIN category c ON p.category_id = c.category_id " +
                "JOIN product_status s ON p.product_status_id = s.product_status_id " +
                "WHERE s.product_status_id = 3 " +
                "and yearweek(created_at, 1) = yearweek(curdate(), 1) " +
                "GROUP BY c.category_id " +
                "ORDER BY count DESC " +
                "LIMIT 5";

        return jdbcTemplate.query(sql, new RowMapper<AdminCategorySalesTop5Response>() {
            @Override
            public AdminCategorySalesTop5Response mapRow(ResultSet rs, int rowNum) throws SQLException, SQLException {
                int count = rs.getInt("count");
                String categoryCode = rs.getString("categoryCode");
                return new AdminCategorySalesTop5Response(count, categoryCode);
            }
        });
    }

    @Override
    public Page<Product> findProductsSoldToday(LocalDate today, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(createdAtIsToday(today))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(createdAtIsToday(today))
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    private BooleanExpression createdAtIsToday(LocalDate today) {
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        return product.createdAt.between(startOfDay, endOfDay);
    }
}
