package com.ferrovial.tsm.service;

import com.ferrovial.tsm.model.Gantry;
import com.ferrovial.tsm.model.GantryStatus;
import com.ferrovial.tsm.model.Rate;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Slf4j
@Component("gapService")
public class GapService {

    private Gantry gantry;

    @PostConstruct
    public void init() {
        log.info("GapService initialized");
        this.gantry = Gantry.builder()
                .status(GantryStatus.OPEN)
                .id("gantry-1")
                .build();
    }

    public void execute(GapOperation action, Rate rate) {

        if(gantry.getGap() == null) {
            log.warn("No gap present in gantry {}", gantry.getId());
            return;
        }
        switch (action) {
            case GapOperation.CLOSE_GAP:
                closeGapAndOpenGantry(gantry, rate);
                break;
            case GapOperation.LEAVE_OPEN:
                leaveOpen(gantry);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private void closeGapAndOpenGantry(Gantry gantry, Rate newRate) {
        log.debug("Closing gap for gantry {} with new rate {}", gantry, newRate);
        gantry.getGap().setRate(newRate);
        gantry.getGap().setEndTime(LocalDateTime.now());
        gantry.setStatus(GantryStatus.OPEN);
    }

    private void leaveOpen(Gantry gantry) {
        log.debug("Leaving gap {} open", gantry);
        gantry.setStatus(GantryStatus.OPEN);
    }
}
