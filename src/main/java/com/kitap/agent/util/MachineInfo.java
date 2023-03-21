package com.kitap.agent.util;

import com.kitap.testresult.dto.agent.MachineDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MachineInfo {
    Logger logger = LoggerFactory.getLogger(MachineInfo.class);

    public MachineDetails getMachineInformation() {

        MachineDetails details = new MachineDetails();

        //Get Host Name and Get IP Address
        InetAddress inetAddress = getInetAddress();
        String hostName = inetAddress.getHostName();
        String ipAddress = inetAddress.getHostAddress();

        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String adminUser = System.getProperty("user.name");

        details.setIpAddress(ipAddress);
        details.setOsName(osName);
        details.setOsVersion(osVersion);
        details.setDeviceType(osName);
        details.setMacAddress(getMacAddress());
        details.setHostName(adminUser);
        return details;
    }

    public InetAddress getInetAddress(){
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
        return inetAddress;
    }

    public String getMacAddress(){
        NetworkInterface network;
        StringBuilder macAddressStringBuilder;
        try {
            network = NetworkInterface.getByInetAddress(getInetAddress());
            byte[] macArray = network.getHardwareAddress();
            macAddressStringBuilder = new StringBuilder();
            for (int i = 0; i < macArray.length; i++) {
                macAddressStringBuilder.append(String.format("%02X%s", macArray[i], (i < macArray.length - 1) ? " " : ""));
            }
        } catch (SocketException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
        return macAddressStringBuilder.toString();
    }
}
