package com.ferrovial.tsm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@Builder
@ToString
public class Gap {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GapReason reason;
    private Rate rate;

    public Integer getGapLength() {
        return (endTime != null) ? (int) java.time.Duration.between(startTime, endTime).toMinutes() : 0;
    }
}
