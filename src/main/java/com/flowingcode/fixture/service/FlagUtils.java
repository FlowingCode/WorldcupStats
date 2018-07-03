package com.flowingcode.fixture.service;

public class FlagUtils {

    public static String getFlagForFifaCode(final String fifaCode) {
        return "/frontend/images/flags/" + fifaCode.toLowerCase() + ".svg";
    }

}
