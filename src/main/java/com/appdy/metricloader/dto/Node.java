package com.appdy.metricloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by abey.tom on 8/23/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node extends AnEntity {
    private String agentType;
    private Long tierId;
    private String tierName;
    private Long applicationId;
    private String applicationName;
    private Long controllerId;
    private Long machineId;
    private String machineName;
    private String machineOSType;
    private boolean machineAgentPresent;
    private boolean appAgentPresent;

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public boolean isAppAgentPresent() {
        return appAgentPresent;
    }

    public void setAppAgentPresent(boolean appAgentPresent) {
        this.appAgentPresent = appAgentPresent;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getControllerId() {
        return controllerId;
    }

    public void setControllerId(Long controllerId) {
        this.controllerId = controllerId;
    }

    public boolean isMachineAgentPresent() {
        return machineAgentPresent;
    }

    public void setMachineAgentPresent(boolean machineAgentPresent) {
        this.machineAgentPresent = machineAgentPresent;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineOSType() {
        return machineOSType;
    }

    public void setMachineOSType(String machineOSType) {
        this.machineOSType = machineOSType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!super.equals(o)) return false;

        Node node = (Node) o;

        return !(controllerId != null ? !controllerId.equals(node.controllerId) : node.controllerId != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (controllerId != null ? controllerId.hashCode() : 0);
        return result;
    }
}
