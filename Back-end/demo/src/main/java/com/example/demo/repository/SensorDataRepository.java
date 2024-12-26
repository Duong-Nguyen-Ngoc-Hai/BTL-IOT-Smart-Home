package com.example.demo.repository;

import com.example.demo.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;


@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    
    // Tìm kiếm theo các key khác nhau
    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature")
    List<SensorData> findByTemperature(@Param("temperature") Double temperature);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.humidity, 2) = :humidity")
    List<SensorData> findByHumidity(Double humidity);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.lux, 2) = :lux")
    List<SensorData> findByLux(Double lux);

    List<SensorData> findByTimestamp(LocalDateTime timestamp);

    @Query("SELECT s FROM SensorData s WHERE CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByTimestampContaining(@Param("timestamp") String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.outtemp, 2) = :outtemp")
    List<SensorData> findByOuttemp(Double outtemp);

    // Tìm kiếm theo nhiều key

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND ROUND(s.humidity, 2) = :humidity")
    List<SensorData> findByTemperatureAndHumidity(Double temperature, Double humidity);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND ROUND(s.lux, 2) = :lux")
    List<SensorData> findByTemperatureAndLux(Double temperature, Double lux);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.humidity, 2) = :humidity AND ROUND(s.lux, 2) = :lux")
    List<SensorData> findByHumidityAndLux(Double humidity, Double lux);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByTemperatureAndTimestamp(Double temperature, String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.humidity, 2) = :humidity AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByHumidityAndTimestamp(Double humidity, String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.lux, 2) = :lux AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByLuxAndTimestamp(Double lux, String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.outtemp, 2) = :outtemp AND ROUND(s.temperature, 2) = :temperature")
    List<SensorData> findByTemperatureAndOuttemp(Double temperature, Double outtemp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.outtemp, 2) = :outtemp AND ROUND(s.humidity, 2) = :humidity")
    List<SensorData> findByHumidityAndOuttemp(Double humidity, Double outtemp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.outtemp, 2) = :outtemp AND ROUND(s.lux, 2) = :lux")
    List<SensorData> findByLuxAndOuttemp(Double lux, Double outtemp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.outtemp, 2) = :outtemp AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByTimeAndOuttemp(String timestamp, Double outtemp);

    // Tìm kiếm theo tất cả các key
    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND ROUND(s.humidity, 2) = :humidity AND ROUND(s.lux, 2) = :lux")
    List<SensorData> findByTemperatureAndHumidityAndLux(Double temperature, Double humidity, Double lux);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND ROUND(s.humidity, 2) = :humidity AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByTemperatureAndHumidityAndTimestamp(Double temperature, Double humidity, String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND ROUND(s.lux, 2) = :lux AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByTemperatureAndLuxAndTimestamp(Double temperature, Double lux, String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.humidity, 2) = :humidity AND ROUND(s.lux, 2) = :lux AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByHumidityAndLuxAndTimestamp(Double humidity, Double lux, String timestamp);

    @Query("SELECT s FROM SensorData s WHERE ROUND(s.temperature, 2) = :temperature AND ROUND(s.humidity, 2) = :humidity AND ROUND(s.lux, 2) = :lux AND CAST(s.timestamp AS string) LIKE CONCAT('%', :timestamp, '%')")
    List<SensorData> findByTemperatureAndHumidityAndLuxAndTimestamp(Double temperature, Double humidity, Double lux, String timestamp);

    // Sắp xêp
    List<SensorData> findAll(Sort sort);

    // Lấy 10 dữ liệu mới nhất theo thời gian
    List<SensorData> findTop10ByOrderByTimestampDesc();

    // Lấy 100 dữ liệu mới nhất theo thời gian
    List<SensorData> findTop100ByOrderByTimestampDesc();


}
