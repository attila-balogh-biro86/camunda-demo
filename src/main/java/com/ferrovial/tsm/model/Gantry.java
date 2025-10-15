package com.ferrovial.tsm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class Gantry {
    private String id;
    private Gap gap;
    private GantryStatus status;
}
