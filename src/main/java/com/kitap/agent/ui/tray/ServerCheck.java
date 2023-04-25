package com.kitap.agent.ui.tray;

import com.kitap.agent.ui.initializer.TrayIconAndMenuInitializer;
import com.kitap.agent.util.PropertyReaderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.swing.*;
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
     * Overrided run method as this class implements Runnable interface. Functionality includes
     * checking the server status for specified times in specified interval
     */
    @Override
        public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Checking whether server is reachable or not");
        int i = 1;
        long timeInterval = Long.parseLong(PropertyReaderHelper.getProperty("serverchecktimeinterval"));
        log.info("Time Interval to check server status is {} seconds",timeInterval);
        int numberOfTimes = Integer.parseInt(PropertyReaderHelper.getProperty("numberoftimes"));
        log.info("Number of times the server status should be checked is {}",numberOfTimes);
            while (i<=numberOfTimes) {
                log.info("{} time the server connection check",i);
                try(Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(serverAddress, port), timeout);
                        log.info("Server is up and running.");
                        if(!isUp){
                            log.info("On change of server status, shows an information message.");
                            final JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(dialog,"Server is UP");
                        }
                        isUp = true;
                } catch (Exception e){
                        log.info("Server is down or unreachable.");
                        if(isUp){
                            log.info("On change of server status, shows an information message.");
                            final JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(dialog,"Server is DOWN");
                        }
                        isUp = false;
                    e.printStackTrace();
                }
                i++;
                TrayIconAndMenuInitializer.serverUpdate(isUp);//calling method to update property value

                // Wait for delay time interval before checking again
                try {
                    TimeUnit.SECONDS.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}