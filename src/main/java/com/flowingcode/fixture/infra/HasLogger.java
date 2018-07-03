package com.flowingcode.fixture.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface HasLogger {

    default Logger logger() {
        return LoggerFactory.getLogger(getClass());
    }

}
