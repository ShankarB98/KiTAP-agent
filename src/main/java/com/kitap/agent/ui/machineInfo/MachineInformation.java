package com.kitap.agent.ui.machineInfo;

import com.kitap.agent.database.model.dto.AgentDto;
import com.kitap.agent.util.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @Author: KT1497
 *
 * @Description: Getting the information of the machine currently using by agent
 *               like IP Address, MAC Address, AdminName,
 *               OS Information,AgentName & agentKey from UI. Setting these
 *               values to a DTO class
 */
@Slf4j
@Component
public class MachineInformation{
    final PropertyReader reader = new PropertyReader();
    public final InetAddress inetAddress;
    public final String osName;
    public final String osVersion;
    public final String adminUser;

    public final String macAddress;

    public MachineInformation(){
        this.inetAddress = getInetAddress();
        this.osName = System.getProperty("os.name");
        this.osVersion = System.getProperty("os.version");
        this.adminUser = System.getProperty("user.name");
        this.macAddress = getMacAddress();
    }

    public AgentDto getAgentDto(String agentName){
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
        return agentDto;
    }

    /**
     * Getting InetAddress
     * 
     * @return InetAddress
     */
    public InetAddress getInetAddress() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return inetAddress;
    }

    /**
     * Getting MACAddress of a Machine
     * 
     * @return String(MAC Address)
     */
    public String getMacAddress() {
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
        return macAddressStringBuilder.toString();
    }
}
