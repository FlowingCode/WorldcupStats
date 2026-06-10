package com.flowingcode.fixture.service;

import java.util.Collections;
import java.util.Map;

/**
 * Resolves a team flag (a remote image URL) from its FIFA code. The mapping is
 * populated at runtime from the worldcup26.ir teams endpoint (which provides a
 * flag URL per team), so no local flag assets are required.
 */
public class FlagUtils {

    private static volatile Map<String, String> flagsByCode = Collections.emptyMap();

    private FlagUtils() {
    }

    public static void setFlags(final Map<String, String> flags) {
        flagsByCode = flags;
    }

    public static String getFlagForFifaCode(final String fifaCode) {
        if (fifaCode == null) {
            return "";
        }
        return flagsByCode.getOrDefault(fifaCode, "");
    }

}
