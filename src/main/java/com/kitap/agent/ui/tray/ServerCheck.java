package com.kitap.agent.ui.tray;

import com.kitap.agent.ui.initializer.TrayIconAndMenuInitializer;
import com.kitap.agent.util.PropertyReaderHelper;
import lombok.extern.slf4j.Slf4j;

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
    private boolean isUp;
    int i = 1;

    /**
     * Constructor to assign values to instance variables
     * @param serverAddress ipAddress of the server to be checked
     * @param port port on which server is running
     * @param timeout time to give the response
     */
    public ServerCheck(String serverAddress, int port, int timeout, boolean initialServerStatus) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.timeout = timeout;
        this.isUp = initialServerStatus;
    }

    /**
     * Overrided run method as this class implements Runnable interface. Functionality includes
     * checking the server status for specified times in specified interval
     */
    @Override
        public void run() {
        log.info("Checking whether server is reachable or not");
        long timeInterval = Long.parseLong(PropertyReaderHelper.getProperty("serverchecktimeinterval"));
        log.info("Time Interval to check server status is {} seconds",timeInterval);
        int numberOfTimes = Integer.parseInt(PropertyReaderHelper.getProperty("numberoftimes"));
        log.info("Number of times the server status should be checked is {}",numberOfTimes);
            while (true) {
                try(Socket socket = new Socket()) {
                    log.info("Server Connection check");
                    socket.connect(new InetSocketAddress(serverAddress, port), timeout);
                        log.info("Server is up and running.");
                        i=1;
                        if(!isUp){
                            log.info("On change of server status, shows an information message.");
                            final JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(dialog,"Successfully connected to KiTAP Server.");
                        }
                        isUp = true;
                } catch (Exception e){
                        log.info("Server is down or unreachable.");
                        log.info("Server is down for count {} times",i);
                        i++;
                        if(i>numberOfTimes){
                            final JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(dialog,"Tried connecting to KiTAP server for "+numberOfTimes+" times but not reachable.Agent is Shutting Down");
                            log.info("Tried connecting to KiTAP server for {} times but not reachable.So shutting down the KiTAP agent",numberOfTimes);
                            System.exit(0);
                        }
                        if(isUp){
                            log.info("On change of server status, shows an information message.");
                            final JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(dialog,"Failed to connect to KiTAP server. Will try again in a while");
                        }
                        isUp = false;
                    e.printStackTrace();
                }
                TrayIconAndMenuInitializer.serverUpdate(isUp);//calling method to update property value

                // Wait for delay time interval before checking again
                try {
                    TimeUnit.SECONDS.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}