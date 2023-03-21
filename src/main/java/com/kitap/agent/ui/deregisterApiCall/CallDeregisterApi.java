package com.kitap.agent.ui.deregisterApiCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author KT1497
 *
 * @Description Calling the API for Deregistration
 */
@Slf4j
@Component
public class CallDeregisterApi {
//    @Autowired
//    ApiCalls apiCalls;
//    @Autowired
//    MachineInformation machineInformation;

    /**
     * @Description Deregistering the API
     */
    public void deRegisterAgent() {
        // AgentTrayIcon agentTrayIcon= new AgentTrayIcon();
        log.info("calling api to deregister agent");
        //apiCalls.deRegister(machineInformation.getMacAddress());
        //agentTrayIcon.addMenuToTrayIcon();
    }
}
