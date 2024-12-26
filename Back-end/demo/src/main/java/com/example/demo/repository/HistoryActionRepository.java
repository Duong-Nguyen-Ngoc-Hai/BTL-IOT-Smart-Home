package com.example.demo.repository;

import com.example.demo.entity.HistoryAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;

@Repository
public interface HistoryActionRepository extends JpaRepository<HistoryAction, Long> {

    // Tìm kiếm theo các key khác nhau
    List<HistoryAction> findByDevice(String device);

    List<HistoryAction> findByAction(String action);

    @Query("SELECT s FROM HistoryAction s WHERE CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<HistoryAction> findByTimestamp(@Param("timestamp") String timestamp);

    // Tìm kiếm theo nhiều key
    List<HistoryAction> findByDeviceAndAction(String device, String action);

    @Query("SELECT s FROM HistoryAction s WHERE s.device = :device AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<HistoryAction> findByDeviceAndTimestamp(String device, String timestamp);

    @Query("SELECT s FROM HistoryAction s WHERE s.action = :action AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<HistoryAction> findByActionAndTimestamp(String action, String timestamp);

    // Tìm kiếm theo tất cả các key
    @Query("SELECT s FROM HistoryAction s WHERE s.device = :device AND s.action = :action AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<HistoryAction> findByDeviceAndActionAndTimestamp(String device, String action, String timestamp);

    // Sắp xếp
    List<HistoryAction> findAll(Sort sort);
}
