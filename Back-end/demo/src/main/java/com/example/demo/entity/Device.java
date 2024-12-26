package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device") // Tên bảng trong database

public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String led1;
    private String led2;
    private String led3;
    private LocalDateTime timestamp;

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLed1() {
        return led1;
    }

    public void setLed1(String led1) {
        this.led1 = led1;
    }

    public String getLed2() {
        return led2;
    }

    public void setLed2(String led2) {
        this.led2 = led2;
    }

    public String getLed3() {
        return led3;
    }

    public void setLed3(String led3) {
        this.led3 = led3;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
