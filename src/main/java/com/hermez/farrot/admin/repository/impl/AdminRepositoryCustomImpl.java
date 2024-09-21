package com.hermez.farrot.admin.repository.impl;

import com.hermez.farrot.admin.dto.response.*;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import static com.hermez.farrot.member.entity.QMember.member;
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
                "and yearweek(p.sold_at, 1) = yearweek(curdate(), 1) " +
                "GROUP BY categoryCode " +
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
    public List<AdminRegisterWeeklyResponse> findSignupWeeklyCounts() {
        String sql = "WITH RECURSIVE DateRange AS (" +
                "    SELECT curdate() - INTERVAL 6 DAY AS signup_date " +
                "    UNION ALL " +
                "    SELECT signup_date + INTERVAL 1 DAY " +
                "    FROM DateRange " +
                "    WHERE signup_date < curdate()" +
                ") " +
                "SELECT " +
                "    d.signup_date, " +
                "    COALESCE(COUNT(m.member_id), 0) AS signup_count " +
                "FROM " +
                "    DateRange d " +
                "    LEFT JOIN member m ON DATE(m.create_at) = d.signup_date " +
                "GROUP BY " +
                "    d.signup_date " +
                "ORDER BY " +
                "    d.signup_date";

        return jdbcTemplate.query(sql, new RowMapper<AdminRegisterWeeklyResponse>() {
            @Override
            public AdminRegisterWeeklyResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                int signupCount = rs.getInt("signup_count");
                Date signupDate = rs.getDate("signup_date");

                return new AdminRegisterWeeklyResponse(signupCount, signupDate);
            }
        });
    }

    @Override
    public List<AdminRegisterMonthlyResponse> findSignupMonthlyCounts() {
        String sql = "WITH RECURSIVE DataRange AS (" +
                "    SELECT DATE_FORMAT(CURDATE(), '%Y-01-01') AS signup_month " +
                "    UNION ALL " +
                "    SELECT DATE_ADD(signup_month, INTERVAL 1 MONTH) " +
                "    FROM DataRange " +
                "    WHERE signup_month < DATE_FORMAT(CURDATE(), '%Y-12-01') " +
                ") " +
                "SELECT DATE_FORMAT(dr.signup_month, '%m') AS signup_month, " +
                "       COALESCE(COUNT(m.member_id), 0) AS signup_count " +
                "FROM DataRange dr " +
                "LEFT JOIN member m ON DATE_FORMAT(m.create_at, '%m') = DATE_FORMAT(dr.signup_month, '%m') " +
                "GROUP BY dr.signup_month " +
                "ORDER BY dr.signup_month;";

        return jdbcTemplate.query(sql, new RowMapper<AdminRegisterMonthlyResponse>() {
            @Override
            public AdminRegisterMonthlyResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                String signupDate = rs.getString("signup_month"); // 수정
                int signupCount = rs.getInt("signup_count");
                return new AdminRegisterMonthlyResponse(signupDate+"월", signupCount); // 생성자도 수정 필요
            }
        });

    }

    @Override
    public List<AdminCategoryThisWeekTotalViewsResponse> findThisWeekTotalViewsByCategory() {
        String sql = "SELECT " +
                "c.code AS categoryCode, " +
                "SUM(p.view) AS total_views " +
                "FROM product p " +
                "JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.sold_at >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) " +
                "AND p.sold_at < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY) " +
                "AND p.product_status_id = 3 " +
                "GROUP BY c.code " +
                "ORDER BY c.category_id ASC;";


        return jdbcTemplate.query(sql, new RowMapper<AdminCategoryThisWeekTotalViewsResponse>() {
            @Override
            public AdminCategoryThisWeekTotalViewsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                String categoryCode = rs.getString("categoryCode");
                int totalViews = rs.getInt("total_views");
                return new AdminCategoryThisWeekTotalViewsResponse(categoryCode, totalViews);
            }
        });
    }

    @Override
    public List<AdminCategoryAveragePriceResponse> findAveragePriceByCategory() {
        String sql = "SELECT " +
                "c.code AS categoryCode, " +
                "FLOOR(AVG(p.price)) AS averagePrice " +
                "FROM category c " +
                "LEFT JOIN product p ON c.category_id = p.category_id " +
                "GROUP BY c.category_id, c.code " +
                "ORDER BY c.category_id ASC";

        return jdbcTemplate.query(sql, new RowMapper<AdminCategoryAveragePriceResponse>() {
            @Override
            public AdminCategoryAveragePriceResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                int averagePrice = rs.getInt("averagePrice");
                String categoryCode = rs.getString("categoryCode");
                return new AdminCategoryAveragePriceResponse(averagePrice, categoryCode);
            }
        });
    }



    @Override
    public Page<Product> findProductsRegisterToday(LocalDate today, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(createdAtIsToday(today))
                .orderBy(product.createdAt.desc())
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

    @Override
    public int countByCreatedAtToday(LocalDate today) {
        return Math.toIntExact(queryFactory
                .select(member.count())
                .from(member)
                .where(member.createAt.between(today.atStartOfDay(), today.plusDays(1).atStartOfDay()))
                .fetchOne());
    }

    @Override
    public int countSalesToday(LocalDate today) {
        String sql = "SELECT SUM(price) AS total_sales " +
                "FROM product " +
                "WHERE sold_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        }, Integer.class);
    }


    @Override
    public Page<Product> findProductsRegisteredThisWeek(LocalDate today, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(registeredAtIsThisWeek(today))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(registeredAtIsThisWeek(today))
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    private BooleanExpression registeredAtIsThisWeek(LocalDate today) {
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDateTime startOfDay = startOfWeek.atStartOfDay();
        LocalDateTime endOfDay = endOfWeek.plusDays(1).atStartOfDay().minusNanos(1);

        return product.createdAt.between(startOfDay, endOfDay);
    }

    @Override
    public Page<Product> findProductsRegisteredThisMonth(LocalDate today, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(registeredAtIsThisMonth(today))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(registeredAtIsThisMonth(today))
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    private BooleanExpression registeredAtIsThisMonth(LocalDate today) {
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        LocalDateTime startOfDay = startOfMonth.atStartOfDay();
        LocalDateTime endOfDay = endOfMonth.plusDays(1).atStartOfDay().minusNanos(1);

        return product.createdAt.between(startOfDay, endOfDay);
    }

    @Override
    public List<AdminProductYearlyTotalSalesResponse> findYearlyTotalSales() {
        String sql = "WITH RECURSIVE months AS ( " +
                "    SELECT 1 AS month UNION ALL " +
                "    SELECT month + 1 FROM months WHERE month < 12 " +
                ") " +
                "SELECT m.month, IFNULL(SUM(p.price), 0) AS total_sales " +
                "FROM months m " +
                "LEFT JOIN product p ON MONTH(p.sold_at) = m.month " +
                "AND YEAR(p.sold_at) = YEAR(curdate()) " +
                "WHERE p.product_status_id = 3 " +
                "GROUP BY m.month " +
                "ORDER BY m.month";

        return jdbcTemplate.query(sql, new RowMapper<AdminProductYearlyTotalSalesResponse>() {
            @Override
            public AdminProductYearlyTotalSalesResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                String month = String.format("%02d월", rs.getInt("month"));
                int total_sales = rs.getInt("total_sales");
                return new AdminProductYearlyTotalSalesResponse(month, total_sales);
            }
        });
    }


    @Override
    public List<AdminProductTodayTotalSalesResponse> findTodayTotalSales() {
        String sql = "SELECT " +
                "HOUR(sold_at) AS hour, " +
                "SUM(price) AS total_sales " +
                "FROM product " +
                "WHERE sold_at >= curdate() " +
                "GROUP BY HOUR(sold_at) " +
                "ORDER BY HOUR(sold_at)";

        return jdbcTemplate.query(sql, new RowMapper<AdminProductTodayTotalSalesResponse>() {
            @Override
            public AdminProductTodayTotalSalesResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                String hour = String.format("%02d:00", rs.getInt("hour"));
                int total_sales = rs.getInt("total_sales");
                return new AdminProductTodayTotalSalesResponse(hour, total_sales);
            }
        });
    }


    @Override
    public List<AdminProductWeeklyTotalSalesResponse> findWeeklyTotalSales() {
        String sql = "WITH RECURSIVE DateRange AS ( " +
                "    SELECT curdate() - INTERVAL weekday(CURRENT_DATE()) DAY AS sale_date " +
                "    UNION ALL " +
                "    SELECT sale_date + INTERVAL 1 DAY " +
                "    FROM DateRange " +
                "    WHERE sale_date < curdate() " +
                ") " +
                "SELECT " +
                "    DATE_FORMAT(dr.sale_date, '%Y-%m-%d') AS week, " +
                "    COALESCE(SUM(p.price), 0) AS total_sales " +
                "FROM DateRange dr " +
                "LEFT JOIN product p ON DATE(p.sold_at) = dr.sale_date " +
                "GROUP BY dr.sale_date " +
                "ORDER BY dr.sale_date;";

        return jdbcTemplate.query(sql, new RowMapper<AdminProductWeeklyTotalSalesResponse>() {
            @Override
            public AdminProductWeeklyTotalSalesResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                String week = rs.getString("week");
                int total_sales = rs.getInt("total_sales");
                return new AdminProductWeeklyTotalSalesResponse(week, total_sales);
            }
        });
    }

    @Override
    public List<AdminProductMonthTotalSalesResponse> findMonthTotalSales() {
        String sql = "SELECT " +
                "MONTH(sold_at) AS month, " +
                "SUM(price) AS total_sales " +
                "FROM product " +
                "WHERE sold_at >= DATE_FORMAT(CURRENT_DATE(), '%Y-%m-01') " +
                "AND sold_at < DATE_FORMAT(CURRENT_DATE() + INTERVAL 1 MONTH, '%Y-%m-01') " +
                "GROUP BY MONTH(sold_at) " +
                "ORDER BY MONTH(sold_at)";


        return jdbcTemplate.query(sql, new RowMapper<AdminProductMonthTotalSalesResponse>() {
            @Override
            public AdminProductMonthTotalSalesResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                int month = rs.getInt("month");
                int total_sales = rs.getInt("total_sales");
                return new AdminProductMonthTotalSalesResponse(month + "월", total_sales);
            }
        });
    }
}