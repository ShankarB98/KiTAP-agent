package com.kitap.agent.ui.machineInfo;

import com.kitap.agent.database.model.dto.AgentDto;
import com.kitap.agent.util.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Getting the information of the machine currently using by agent
 *               like IP Address, MAC Address, AdminName,
 *               OS Information,AgentName & agentKey from UI.
 *               Setting these values to a DTO class
 *@author KT1497
 */
@Slf4j
@Component
public class MachineInformation{
    private final PropertyReader reader = new PropertyReader();
    public final InetAddress inetAddress;
    private final String osName;
    private final String osVersion;
    private final String adminUser;

    public final String macAddress;

    public MachineInformation(){
        this.inetAddress = getInetAddress();
        this.osName = System.getProperty("os.name");
        this.osVersion = System.getProperty("os.version");
        this.adminUser = System.getProperty("user.name");
        this.macAddress = getMacAddress();
    }

    /**
     * setting the system details in which agent is running to dto class
     * and returning the agentdto
     * @param agentName name of the agent given by user
     * @return agentdto object with all details
     */
    public AgentDto getAgentDto(String agentName){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method getAgentDto started");
        AgentDto agentDto = new AgentDto();
        agentDto.setName(agentName);
        agentDto.setIpAddress(inetAddress.getHostAddress());
        agentDto.setOsName(osName);
        agentDto.setOsVersion(osVersion);
        agentDto.setDeviceType(osName);
        agentDto.setMacAddress(getMacAddress());
        agentDto.setHostName(adminUser);
        String portNumber = reader.getProperty("server.port");
        agentDto.setPortNumber(portNumber == null ? 8080 : Integer.parseInt(portNumber));
        log.info("Host : " + inetAddress.getHostAddress());
        log.info(agentDto.toString());
        log.info("completed getAgentDto method by returning agentDto");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return agentDto;
    }

    /**
     * Getting InetAddress
     * @return InetAddress system inetaddress
     */
    public InetAddress getInetAddress() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method getInetAddress started");
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        log.info("completed getInetAddress method by returning inetAddress");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return inetAddress;
    }

    /**
     * Getting MACAddress of a Machine
     * @return String(MAC Address) system macaddress
     */
    public String getMacAddress() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method getMacAddress started");
        NetworkInterface network;
        StringBuilder macAddressStringBuilder;
        try {
            network = NetworkInterface.getByInetAddress(getInetAddress());
            byte[] macArray = network.getHardwareAddress();
            macAddressStringBuilder = new StringBuilder();
            for (int i = 0; i < macArray.length; i++) {
                macAddressStringBuilder
                        .append(String.format("%02X%s", macArray[i], (i < macArray.length - 1) ? " " : ""));
            }
        } catch (SocketException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        log.info("completed getMacAddress method by returning macAddress as string");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return macAddressStringBuilder.toString();
    }
}