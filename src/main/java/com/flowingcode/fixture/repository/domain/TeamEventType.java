package com.flowingcode.fixture.repository.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TeamEventType {

    SUBSTITUTION_IN("Substitution In"), SUBSTITUTION_OUT("Substitution Out"), YELLOW_CARD("Yellow Card"), RED_CARD("Red Card"), GOAL("Goal"), GOAL_PENALTY(
            "Penalty Goal"), GOAL_OWN("Goal Own");

    private static Map<String, TeamEventType> namesMap = new HashMap<>(6);

    private String display;

    private TeamEventType(final String display) {
        this.display = display;
    }

    static {
        namesMap.put("substitution-in", SUBSTITUTION_IN);
        namesMap.put("substitution-out", SUBSTITUTION_OUT);
        namesMap.put("yellow-card", YELLOW_CARD);
        namesMap.put("red-card", RED_CARD);
        namesMap.put("goal", GOAL);
        namesMap.put("goal-penalty", GOAL_PENALTY);
        namesMap.put("goal-own", GOAL_OWN);
    }


    @JsonCreator
    public static TeamEventType forValue(final String value) {
        return namesMap.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (final Entry<String, TeamEventType> entry : namesMap.entrySet()) {
            if (entry.getValue() == this) {
                return entry.getKey();
            }
        }

        return null; // or fail
    }

    public String getDisplay() {
        return display;
    }
}
