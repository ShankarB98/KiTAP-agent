package com.kitap.agent.ui.tray;

import com.kitap.agent.ui.initializer.TrayIconAndMenuInitializer;
import com.kitap.agent.util.PropertyReaderHelper;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Class contains functionality to check whether the server is
 * reachable or not in a specified time interval for specified number of times
 * @author KT1497
 */
@Slf4j
public class ServerCheck implements Runnable {
    private final String serverAddress;
    private final int port;
    private final int timeout;
    static boolean isUp;

    /**
     * Constructor to assign values to instance variables
     * @param serverAddress ipAddress of the server to be checked
     * @param port port on which server is running
     * @param timeout time to give the response
     */
    public ServerCheck(String serverAddress, int port, int timeout) {
            this.serverAddress = serverAddress;
            this.port = port;
            this.timeout = timeout;
        }

    /**
     * Overrided run method as this class implements Runnable interface
     */
    @Override
        public void run() {
        int i = 1;
        long timeInterval = Long.parseLong(PropertyReaderHelper.getProperty("serverchecktimeinterval"));
        int numberOfTimes = Integer.parseInt(PropertyReaderHelper.getProperty("numberoftimes"));
            while (i<=numberOfTimes) {
                log.info("{} time the server connection check",i);
                try(Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(serverAddress, port), timeout);
                        log.info("Server is up and running.");
                        isUp = true;
                } catch (Exception e){
                        log.info("Server is down or unreachable");
                        isUp = false;
                    e.printStackTrace();
                }

                i++;
                TrayIconAndMenuInitializer.serverUpdate(isUp);

                // Wait for delay time interval before checking again
                try {
                    TimeUnit.SECONDS.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}