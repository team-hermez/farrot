package com.hermez.farrot.admin.repository.impl;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.repository.AdminRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
