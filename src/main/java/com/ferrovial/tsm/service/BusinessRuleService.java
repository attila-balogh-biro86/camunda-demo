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

    static List<Map<String, String>> readCsv() throws Exception {
        List<Map<String, String>> out = new ArrayList<>();
        File dataFile = ResourceUtils.getFile(
                "classpath:data/scenarios-with-dates.csv");
        List<String> lines = Files.readAllLines(Paths.get(dataFile.toURI()));
        if (lines.isEmpty()) return out;
        String[] headers = lines.get(0).split(",");
        for (int i=1; i<lines.size(); i++) {
            String[] vals = splitCsv(lines.get(i), headers.length);
            Map<String, String> row = new LinkedHashMap<>();
            for (int j=0; j<headers.length; j++) {
                row.put(headers[j], j < vals.length ? vals[j] : "");
            }
            out.add(row);
        }
        return out;
    }

    // naive CSV split (no quoted commas in our samples)
    static String[] splitCsv(String line, int min) {
        String[] parts = line.split(",");
        if (parts.length < min) {
            String[] p = new String[min];
            System.arraycopy(parts, 0, p, 0, parts.length);
            for (int i=parts.length; i<min; i++) p[i] = "";
            return p;
        }
        return parts;
    }

    static void put(Map<String,Object> m, String k, Object v){ m.put(k, v); }
    static Double d(String s){ return s==null||s.isEmpty()? null : Double.valueOf(s); }
    static Integer i(String s){ return s==null||s.isEmpty()? null : Integer.valueOf(s); }




    public Double calculateNewRatePerMile(Map<String, Object> vars) throws FileNotFoundException {
        DmnDecision decision = DMN_ENGINE.parseDecision("NewRatePerMile", MODEL_INSTANCE);
        DmnDecisionResult result = DMN_ENGINE.evaluateDecision(decision, vars);
        Double newRatePerMile = ((Number) result.getSingleResult().getSingleEntry()).doubleValue();
        log.debug("NewRatePerMile: {}", newRatePerMile);
        return newRatePerMile;
    }

}
