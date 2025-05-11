package com.halo.eventer.domain.duration.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.halo.eventer.domain.duration.Duration;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DurationMapJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertMapDurations(Long mapId, List<Duration> durations) {
        if (durations == null || durations.isEmpty()) {
            return;
        }

        final String sql = "INSERT INTO duration_map (map_id, duration_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, mapId);
                ps.setLong(2, durations.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return durations.size();
            }
        });
    }
}
