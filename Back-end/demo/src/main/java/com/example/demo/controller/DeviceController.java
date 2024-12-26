package com.example.demo.controller;

import com.example.demo.entity.Device;
import com.example.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    // API để thêm dữ liệu vào bảng Device
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        device.setTimestamp(LocalDateTime.now());  // Thiết lập thời gian cho trạng thái thiết bị
        Device savedDevice = deviceRepository.save(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
    }

    // API để lấy tất cả dữ liệu trong bảng Device
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Device>> getSortedDevices (
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder) {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        try {
            Sort sort = Sort.by(direction, sortField);
            List<Device> sortedData = deviceRepository.findAll(sort);
            return ResponseEntity.ok(sortedData);
        } catch (IllegalArgumentException e) {
            // Trường hợp `sortField` không hợp lệ
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDevice(
            @RequestParam(required = false) String led1,
            @RequestParam(required = false) String led2,
            @RequestParam(required = false) String led3,
            @RequestParam(required = false) String timestamp
    ) {
        if (led1 == null && led2 == null && led3 == null && timestamp == null) {
            return ResponseEntity.badRequest().body("At least one search parameter is required.");
        }

        List<Device> results;

        if (led1 != null && led2 != null && led3 != null && timestamp != null) {
            results = deviceRepository.findByLed1AndLed2AndLed3AndTimestamp(led1, led2, led3, LocalDateTime.parse(timestamp));
        } else if (led1 != null && led2 != null && led3 != null) {
            results = deviceRepository.findByLed1AndLed2AndLed3(led1, led2, led3);
        } else if (led1 != null && led2 != null) {
            results = deviceRepository.findByLed1AndLed2(led1, led2);
        } else if (led1 != null) {
            results = deviceRepository.findByLed1(led1);
        } else if (led2 != null) {
            results = deviceRepository.findByLed2(led2);
        } else if (led3 != null) {
            results = deviceRepository.findByLed3(led3);
        } else {
            results = deviceRepository.findByTimestamp(LocalDateTime.parse(timestamp));
        }
        if (results.isEmpty()) {
            return ResponseEntity.ok("No data found for the given parameters.");
        }
        return ResponseEntity.ok(results);
    }
}
