package com.halo.eventer.domain.map.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MenuJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertMenu(Long mapId, List<MenuCreateDto> menuCreateDtos) {
        if (menuCreateDtos.isEmpty()) {
            return;
        }

        final String sql = "INSERT INTO menu (map_id, name,price,summary,image) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, mapId);
                ps.setString(2, menuCreateDtos.get(i).getName());
                ps.setInt(3, menuCreateDtos.get(i).getPrice());
                ps.setString(4, menuCreateDtos.get(i).getSummary());
                ps.setString(5, menuCreateDtos.get(i).getImage());
            }

            @Override
            public int getBatchSize() {
                return menuCreateDtos.size();
            }
        });
    }
}
