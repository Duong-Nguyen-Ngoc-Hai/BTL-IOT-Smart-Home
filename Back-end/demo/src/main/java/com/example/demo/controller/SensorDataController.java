package com.example.demo.controller;

import com.example.demo.entity.SensorData;
import com.example.demo.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/api/sensor_data")
public class SensorDataController {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    /**
     * Endpoint để tìm kiếm dữ liệu cảm biến theo nhiệt độ, độ ẩm hoặc lux.
     * @param id          ID của dữ liệu cảm biến (tuỳ chọn)
     * @param temperature Nhiệt độ cần tìm kiếm (tuỳ chọn)
     * @param humidity    Độ ẩm cần tìm kiếm (tuỳ chọn)
     * @param lux         Độ sáng cần tìm kiếm (tuỳ chọn)
     * @param timestamp   Thời gian cần tìm kiếm (tuỳ chọn)
     * @param outtemp     Nhiệt độ ngoài cần tìm kiếm (tuỳ chọn)
     * @return danh sách dữ liệu cảm biến phù hợp
     */

    @PostMapping
    public ResponseEntity<SensorData> addSensorData(@RequestBody SensorData sensorData) {
        sensorData.setTimestamp(LocalDateTime.now());  // Thiết lập thời gian cho dữ liệu mới
        SensorData savedSensorData = sensorDataRepository.save(sensorData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSensorData);
    }

    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        return ResponseEntity.ok(sensorDataList);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<SensorData>> getTop10SensorData() {
        List<SensorData> top10Data = sensorDataRepository.findTop10ByOrderByTimestampDesc();
        return ResponseEntity.ok(top10Data);
    }

    @GetMapping("/top100")
    public ResponseEntity<List<SensorData>> getTop100SensorData() {
        List<SensorData> top100Data = sensorDataRepository.findTop100ByOrderByTimestampDesc();
        return ResponseEntity.ok(top100Data);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<SensorData>> getSortedSensorData(
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder) {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        try {
            Sort sort = Sort.by(direction, sortField);
            List<SensorData> sortedData = sensorDataRepository.findAll(sort);
            return ResponseEntity.ok(sortedData);
        } catch (IllegalArgumentException e) {
            // Trường hợp `sortField` không hợp lệ
            return ResponseEntity.badRequest().body(null);
        }
    }
     
    @GetMapping("/search")
    public ResponseEntity<?> searchSensorData(
            @RequestParam(required = false) Double temperature,
            @RequestParam(required = false) Double humidity,
            @RequestParam(required = false) Double lux,
            @RequestParam(required = false) Double outtemp,
            @RequestParam(required = false) String timestamp
    ) {
        if (temperature == null && humidity == null && lux == null && timestamp == null && outtemp == null) {
            return ResponseEntity.badRequest().body("At least one search parameter is required.");
        }


        List<SensorData> results;
        if (outtemp != null && temperature == null && humidity == null && lux == null && timestamp == null) {
            results = sensorDataRepository.findByOuttemp(outtemp);
        } else if (outtemp != null && temperature != null) {
            results = sensorDataRepository.findByTemperatureAndOuttemp(temperature, outtemp);
        } else if (outtemp != null && humidity != null) {
            results = sensorDataRepository.findByHumidityAndOuttemp(humidity, outtemp);
        } else if (outtemp != null && lux != null) {
            results = sensorDataRepository.findByLuxAndOuttemp(lux, outtemp);
        } else if (outtemp != null && timestamp != null) {
            results = sensorDataRepository.findByTimeAndOuttemp(timestamp, outtemp);
        } else
        if (temperature != null && humidity != null && lux != null && timestamp != null) {
            results = sensorDataRepository.findByTemperatureAndHumidityAndLuxAndTimestamp(temperature, humidity, lux, timestamp);
        } else if (temperature != null && humidity != null && lux != null) {
            results = sensorDataRepository.findByTemperatureAndHumidityAndLux(temperature, humidity, lux);
        } else if (temperature != null && humidity != null && timestamp != null) {
            results = sensorDataRepository.findByTemperatureAndHumidityAndTimestamp(temperature, humidity, timestamp);
        } else if (temperature != null && lux != null && timestamp != null) {
            results = sensorDataRepository.findByTemperatureAndLuxAndTimestamp(temperature, lux, timestamp);
        } else if (humidity != null && lux != null && timestamp != null) {
            results = sensorDataRepository.findByHumidityAndLuxAndTimestamp(humidity, lux, timestamp);
        } else if (temperature != null && humidity != null) {
            results = sensorDataRepository.findByTemperatureAndHumidity(temperature, humidity);
        } else if (temperature != null && lux != null) {
            results = sensorDataRepository.findByTemperatureAndLux(temperature, lux);
        } else if (humidity != null && lux != null) {
            results = sensorDataRepository.findByHumidityAndLux(humidity, lux);
        } else if (temperature != null && timestamp != null) {
            results = sensorDataRepository.findByTemperatureAndTimestamp(temperature, timestamp);
        } else if (humidity != null && timestamp != null) {
            results = sensorDataRepository.findByHumidityAndTimestamp(humidity, timestamp);
        } else if (lux != null && timestamp != null) {
            results = sensorDataRepository.findByLuxAndTimestamp(lux, timestamp);
        } else if (temperature != null) {
            results = sensorDataRepository.findByTemperature(temperature);
        } else if (humidity != null) {
            results = sensorDataRepository.findByHumidity(humidity);
        } else if (lux != null) {
            results = sensorDataRepository.findByLux(lux);
        } else if (timestamp != null) {
            results = sensorDataRepository.findByTimestampContaining(timestamp);
        } else {
            results = List.of(); // Không có tham số hợp lệ, trả về danh sách rỗng.
        }

        if (results.isEmpty()) {
            return ResponseEntity.ok("No data found for the given parameters.");
        }

        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllSensorData() {
        sensorDataRepository.deleteAll();
        return ResponseEntity.ok("All sensor data has been deleted.");
    }
}
