package com.halo.eventer.domain.monitoring_data.service;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.timestreamquery.TimestreamQueryClient;
import software.amazon.awssdk.services.timestreamquery.model.QueryRequest;
import software.amazon.awssdk.services.timestreamquery.model.QueryResponse;
import software.amazon.awssdk.services.timestreamquery.model.Row;
import software.amazon.awssdk.services.timestreamquery.model.TimestreamQueryException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class QueryService {

    private final String DATABASE_NAME = "monitoringDB";
    private final String TABLE_NAME = "visitor";

    private final TimestreamQueryClient queryClient;

    public QueryService() {
        queryClient = TimestreamQueryClient.builder().region(Region.AP_NORTHEAST_1).build();
    }

    private String getQueryResponse(QueryRequest queryRequest) {
        try {
            QueryResponse response = queryClient.query(queryRequest);
            List<Row> rows = response.rows();

            if (rows.isEmpty()) {
                return "0";     // 해당 시간대에 방문자 수가 0명
            } else if (rows.get(0).data().get(0) != null) {
                return rows.get(0).data().get(0).scalarValue();
            }else {
                log.info("No data found for the given timestamp and festivalId.");
                throw new BaseException(ErrorCode.ELEMENT_NOT_FOUND);
            }
        } catch (TimestreamQueryException e) {
            log.info("Query failed: {}" + e.getMessage());
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /** 특정 시간만 조회 */
    public Integer bigintQueryRequestAtTime(Long festivalId, String timestamp, String measureName) {
        String query = String.format("SELECT measure_value::bigint FROM \"%s\".\"%s\" " +
                        "WHERE measure_name = '%s' " +
                        "AND time = '%s' " +
                        "AND festival_id = '%s' ",
                DATABASE_NAME, TABLE_NAME, measureName, timestamp, festivalId);
        QueryRequest queryRequest = QueryRequest.builder().queryString(query).build();

        String result = getQueryResponse(queryRequest);
        if (result == null) throw new BaseException(ErrorCode.ELEMENT_NOT_FOUND);
        return Integer.valueOf(result);
    }

    /** 특정 시간만 조회 */
    public String stringQueryRequestAtTime(Long festivalId, String timestamp, String measureName) {
        String query = String.format("SELECT measure_value::varchar FROM \"%s\".\"%s\" " +
                        "WHERE measure_name = '%s' " +
                        "AND time = '%s' " +
                        "AND festival_id = '%s' ",
                DATABASE_NAME, TABLE_NAME, measureName, timestamp, festivalId);

        QueryRequest queryRequest = QueryRequest.builder().queryString(query).build();

        return getQueryResponse(queryRequest);
    }

    /** 시간대별 조회 */
    public Integer querySumHour(Long festivalId, String timestamp, String measureName) {
        Instant startTime = Instant.parse(timestamp);
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS).minusSeconds(1);

        String query = String.format("SELECT SUM(measure_value::bigint) AS total_sum FROM \"%s\".\"%s\" " +
                        "WHERE measure_name = '%s' " +
                        "AND time BETWEEN from_milliseconds(%d) AND from_milliseconds(%d) " +
                        "AND festival_id = '%s' ",
                DATABASE_NAME, TABLE_NAME, measureName, startTime.toEpochMilli(), endTime.toEpochMilli(), festivalId);

        QueryRequest queryRequest = QueryRequest.builder().queryString(query).build();

        String result = getQueryResponse(queryRequest);
        if (result == null) return 0;
        return Integer.valueOf(result);
    }

    /** 특정 날짜 마지막 레코드의 measureName 값 조회 */
    public Integer bigintQueryDate(Long festivalId, String date, String measureName) {
        String query = String.format(
                "SELECT measure_value::bigint AS total_sum FROM \"%s\".\"%s\" " +
                        "WHERE measure_name = '%s' " +
                        "AND festival_id = '%s' " +
                        "AND time BETWEEN from_iso8601_timestamp('%sT00:00:00Z') AND from_iso8601_timestamp('%sT23:59:59Z') " +
                        "ORDER BY time DESC LIMIT 1",
                DATABASE_NAME, TABLE_NAME, measureName, festivalId, date, date
        );
        QueryRequest queryRequest = QueryRequest.builder().queryString(query).build();

        String result = getQueryResponse(queryRequest);
        if (result == null) throw new BaseException(ErrorCode.ELEMENT_NOT_FOUND);
        return Integer.valueOf(result);
    }

    /** 특정 날짜 마지막 레코드의 measureName 값 조회 */
    public String stringQueryDate(Long festivalId, String date, String measureName) {
        String query = String.format(
                "SELECT measure_value::varchar AS total_sum FROM \"%s\".\"%s\" " +
                        "WHERE measure_name = '%s' " +
                        "AND festival_id = '%s' " +
                        "AND time BETWEEN from_iso8601_timestamp('%sT00:00:00Z') AND from_iso8601_timestamp('%sT23:59:59Z') " +
                        "ORDER BY time DESC LIMIT 1",
                DATABASE_NAME, TABLE_NAME, measureName, festivalId, date, date
        );
        QueryRequest queryRequest = QueryRequest.builder().queryString(query).build();

        String result = getQueryResponse(queryRequest);
        if (result == null) throw new BaseException(ErrorCode.ELEMENT_NOT_FOUND);
        else if (result.equals("0") && measureName.equals("cumulative_ageCount")) result = "{\"teenager\":0,\"twenties_and_thirties\":0,\"forties_and_fifties\":0,\"sixties_and_above\":0}\n";
        else if (result.equals("0") && measureName.equals("cumulative_genderCount")) result = "{\"male\":0,\"female\":0,\"none\":0}";

        return result;
    }
}
