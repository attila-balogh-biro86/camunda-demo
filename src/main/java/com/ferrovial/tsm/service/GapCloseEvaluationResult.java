package com.ferrovial.tsm.service;


import com.ferrovial.tsm.model.Rate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class GapCloseEvaluationResult {

    private GapOperation gapOperation;
    private Rate newRate;
    private String description;
}
