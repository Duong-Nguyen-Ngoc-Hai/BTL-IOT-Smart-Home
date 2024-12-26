package com.example.demo.repository;

import com.example.demo.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    // Tìm kiếm theo các key khác nhau
    List<Device> findByLed1(String led1);

    List<Device> findByLed2(String led2);

    List<Device> findByLed3(String led3);

    List<Device> findByTimestamp(LocalDateTime timestamp);

    // Tìm kiếm theo nhiều key
    List<Device> findByLed1AndLed2(String led1, String led2);

    List<Device> findByLed1AndLed3(String led1, String led3);

    List<Device> findByLed2AndLed3(String led2, String led3);

    List<Device> findByLed1AndTimestamp(String led1, LocalDateTime timestamp);

    List<Device> findByLed2AndTimestamp(String led2, LocalDateTime timestamp);

    List<Device> findByLed3AndTimestamp(String led3, LocalDateTime timestamp);

    // Tìm kiếm theo tất cả các key
    List<Device> findByLed1AndLed2AndLed3(String led1, String led2, String led3);

    List<Device> findByLed1AndLed2AndTimestamp(String led1, String led2, LocalDateTime timestamp);

    List<Device> findByLed1AndLed3AndTimestamp(String led1, String led3, LocalDateTime timestamp);

    List<Device> findByLed2AndLed3AndTimestamp(String led2, String led3, LocalDateTime timestamp);

    List<Device> findByLed1AndLed2AndLed3AndTimestamp(String led1, String led2, String led3, LocalDateTime timestamp);

    // Sắp xếp
    List<Device> findAll(Sort sort);
}
