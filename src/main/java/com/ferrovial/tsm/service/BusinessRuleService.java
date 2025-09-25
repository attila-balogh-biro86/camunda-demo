package com.ferrovial.tsm.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.*;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class BusinessRuleService {

    private static final DmnEngine DMN_ENGINE = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();
    private static DmnModelInstance MODEL_INSTANCE;


    @PostConstruct
    public void init() throws Exception {
        File modelFile = ResourceUtils.getFile(
                "classpath:rules/toll-pricing-effective-dated.dmn");
        MODEL_INSTANCE = Dmn.readModelFromFile(modelFile);
    }

    public Double calculateNewRatePerMile(Map<String, Object> vars) throws FileNotFoundException {
        DmnDecision decision = DMN_ENGINE.parseDecision("NewRatePerMile", MODEL_INSTANCE);
        DmnDecisionResult result = DMN_ENGINE.evaluateDecision(decision, vars);
        Double newRatePerMile = ((Number) result.getSingleResult().getSingleEntry()).doubleValue();
        log.debug("NewRatePerMile: {}", newRatePerMile);
        return newRatePerMile;
    }

}
