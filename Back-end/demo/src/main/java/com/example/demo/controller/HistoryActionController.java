package com.example.demo.controller;

import com.example.demo.entity.HistoryAction;
import com.example.demo.repository.HistoryActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/historyaction")
public class HistoryActionController {

    @Autowired
    private HistoryActionRepository historyActionRepository;

    /**
     * Endpoint để tìm kiếm dữ liệu cảm biến theo nhiệt độ, độ ẩm hoặc lux.
     * @param id          ID của dữ liệu cảm biến (tuỳ chọn)
     * @param device thiết bị cần tìm kiếm (tuỳ chọn)
     * @param action   Hành động cần tìm kiếm (tuỳ chọn)
     * @param timestamp   Thời gian cần tìm kiếm (tuỳ chọn)
     * @return danh sách dữ liệu cảm biến phù hợp
     */

    // API để thêm dữ liệu vào bảng HistoryAction
    @PostMapping
    public ResponseEntity<HistoryAction> addHistoryAction(@RequestBody HistoryAction historyAction) {
        historyAction.setTimestamp(LocalDateTime.now());  // Thiết lập thời gian cho hành động
        HistoryAction savedHistoryAction = historyActionRepository.save(historyAction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHistoryAction);
    }

    // API để lấy tất cả dữ liệu trong bảng HistoryAction
    @GetMapping
    public ResponseEntity<List<HistoryAction>> getAllHistoryActions() {
        List<HistoryAction> historyActions = historyActionRepository.findAll();
        return ResponseEntity.ok(historyActions);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<HistoryAction>> getSortedHistoryAction (
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder) {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        try {
            Sort sort = Sort.by(direction, sortField);
            List<HistoryAction> sortedData = historyActionRepository.findAll(sort);
            return ResponseEntity.ok(sortedData);
        } catch (IllegalArgumentException e) {
            // Trường hợp `sortField` không hợp lệ
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchHistoryAction(
            @RequestParam(required = false) String device,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String timestamp
    ) {
        if (device == null && action == null && timestamp == null) {
            return ResponseEntity.badRequest().body("At least one search parameter is required.");
        }

        List<HistoryAction> results;

        if (device != null && action != null && timestamp != null) {
            results = historyActionRepository.findByDeviceAndActionAndTimestamp(device, action, timestamp);
        } else if (device != null && action != null) {
            results = historyActionRepository.findByDeviceAndAction(device, action);
        } else if(device != null && timestamp != null) {
            results = historyActionRepository.findByDeviceAndTimestamp(device, timestamp);
        } else if (action != null && timestamp != null) {
            results = historyActionRepository.findByActionAndTimestamp(action, timestamp);
        } else if (device != null) {
            results = historyActionRepository.findByDevice(device);
        } else if (action != null) {
            results = historyActionRepository.findByAction(action);
        } else {
            results = historyActionRepository.findByTimestamp(timestamp);
        }

        if (results.isEmpty()) {
            return ResponseEntity.ok("No data found for the given parameters.");
        }

        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllSensorData() {
        historyActionRepository.deleteAll();
        return ResponseEntity.ok("All sensor data has been deleted.");
    }
}

