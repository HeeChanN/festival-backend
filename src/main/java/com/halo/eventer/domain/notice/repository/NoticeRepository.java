package com.halo.eventer.domain.notice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n WHERE n.festival.id = :id AND n.picked = :picked ")
    List<Notice> findAllByPickedAndFestivalId(@Param("id") Long id, @Param("picked") Boolean picked);

    @Query("SELECT n FROM Notice n "
            + "WHERE n.type = :type "
            + "AND n.festival.id = :festivalId "
            + "ORDER BY n.updatedAt DESC")
    Page<Notice> findAllByTypeAndFestivalIdOrderByUpdatedAtDesc(
            @Param("type") ArticleType type, @Param("festivalId") Long festivalId, Pageable pageable);

    @Query(
            value = "SELECT * FROM Notice n WHERE n.festival_id = :festivalId AND n.type = :type "
                    + "ORDER BY n.updated_at DESC, n.id ASC "
                    + "LIMIT :size ",
            nativeQuery = true)
    List<Notice> getNoticeFirstPageByFestivalId(
            @Param("festivalId") Long festivalId, @Param("type") String type, @Param("size") int size);

    @Query(
            value = "SELECT * FROM Notice n WHERE n.festival_id = :festivalId AND n.type = :type "
                    + "AND ((n.updated_at = :updatedAt AND n.id > :lastId) OR (n.updated_at < :updatedAt)) "
                    + "ORDER BY n.updated_at DESC, n.id ASC "
                    + "LIMIT :size ",
            nativeQuery = true)
    List<Notice> getNoticesNextPageByFestivalIdAndLastValue(
            @Param("festivalId") Long festivalId,
            @Param("type") String type,
            @Param("updatedAt") LocalDateTime updatedAt,
            @Param("lastId") Long lastId,
            @Param("size") int size);
}
