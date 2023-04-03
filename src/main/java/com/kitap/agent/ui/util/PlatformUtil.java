package com.kitap.agent.ui.util;

import lombok.extern.slf4j.Slf4j;

/**
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
     *
     * @return boolean
     */
    public static boolean isWindow() {
        log.info("Check if windows OS or not and return boolean");
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Check if the Current Platform is MacOS
     *
     * @return boolean
     */
    public static boolean isMac() {
        log.info("Check if Mac OS or not and return boolean");
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    /**
     * Check if the Current Platform is Linux
     *
     * @return boolean
     */
    public static boolean isLinux() {
        log.info("Check if Linux OS or not and return boolean");
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0;
    }

}
