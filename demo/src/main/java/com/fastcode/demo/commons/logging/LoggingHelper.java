package com.fastcode.demo.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingHelper {
    private Logger logger;

    public Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
