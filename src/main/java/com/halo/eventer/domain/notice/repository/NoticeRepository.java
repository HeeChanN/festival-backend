package com.halo.eventer.domain.notice.repository;

import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.ArticleType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

  Page<Notice> findAll(Pageable pageable);

  @Query("SELECT n FROM Notice n WHERE n.festival.id = :id AND n.picked = :picked ")
  List<Notice> findAllByPickedAndFestivalId(@Param("id") Long id,
                                             @Param("picked") Boolean picked);

  @Query("SELECT n FROM Notice n " +
          "WHERE n.type = :type " +
          "AND n.festival.id = :festivalId " +
          "ORDER BY n.updatedAt DESC")
  Page<Notice> findAllByTypeAndFestivalIdOrderByUpdatedAtDesc(@Param("type") ArticleType type,
                                                              @Param("festivalId") Long festivalId,
                                                              Pageable pageable);
}
