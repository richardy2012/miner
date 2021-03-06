package miner.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;

/**
 * Log Class
 *
 */

public class MySysLogger1 {
    private static ConfigurationSource source1;
    private Logger logger = null;
    static {
        try {
            //source = new ConfigurationSource(new FileInputStream("/usr/local/storm/conf/log4j2.xml"));

            String config = System.getProperty("user.dir");
            source1 = new ConfigurationSource(new FileInputStream(config + "/log4j2/log4j2_1.xml"));

            Configurator.initialize(null, source1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MySysLogger1(Class logClass) {
        this.logger = LogManager.getLogger(logClass.getName());
    }

    private Logger getLogger(Class logClass) {
        return LogManager.getLogger(logClass.getName());
    }

    public void info(Object obj) {
        this.logger.info(obj);
    }

    public void warn(Object obj) {
        System.err.println("============");
        this.logger.warn(obj);
    }

    public void error(Object obj) {
        this.logger.error(obj);
    }

    public void debug(Object obj) {
        this.logger.debug(obj);
    }

}
