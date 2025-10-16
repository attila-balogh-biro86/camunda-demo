package com.ferrovial.tsm.api;

import com.ferrovial.tsm.model.Gantry;
import com.ferrovial.tsm.model.Gap;
import com.ferrovial.tsm.model.GapReason;
import com.ferrovial.tsm.model.GantryStatus;
import com.ferrovial.tsm.service.GapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class GantryController {

    private final GapService gapService;

    @Autowired
    public GantryController(GapService gapService) {
        this.gapService = gapService;
    }

    @GetMapping("/gantry")
    public Gantry getGantry() {
        return gapService.getGantry();
    }

    @GetMapping("/gantry/reset")
    public void resetGantry() {
        gapService.resetGantry();
    }

    @GetMapping("/createGap")
    public String createGap() {
        Gap gap = Gap.builder()
                .reason(GapReason.TECHNICAL_ISSUE)
                .startTime(LocalDateTime.now())
                .build();
        this.gapService.getGantry().setStatus(GantryStatus.CLOSED);
        this.gapService.getGantry().setGap(gap);
        return "Gap created: " + gap;
    }
}
