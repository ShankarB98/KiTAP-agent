package com.kitap.agent.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * To check the platform of the system
 * @author KT1497
 */
@Slf4j
public final class PlatformUtil {

    /**
     * Disallow Instances
     */
    private PlatformUtil() {
    }

    /**
     * Check if the Current Platform is Windows
     * @return boolean
     */
    public static boolean isWindow() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Check if windows OS or not and return boolean");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Check if the Current Platform is MacOS
     * @return boolean
     */
    public static boolean isMac() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Check if Mac OS or not and return boolean");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    /**
     * Check if the Current Platform is Linux
     * @return boolean
     */
    public static boolean isLinux() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Check if Linux OS or not and return boolean");
        String os = System.getProperty("os.name").toLowerCase();
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0;
    }
}