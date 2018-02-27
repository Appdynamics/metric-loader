package com.appdy.metricloader.dto;

import java.util.List;

/**
 * Created by abey.tom on 8/23/15.
 */
public class Tier extends AnEntity {
    private String agentType;
    private long numberOfNodes;
    private String type;
    private Long applicationId;
    private String applicationName;
    private List<Node> nodes;


    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public long getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(long numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "Tier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", agentType='" + agentType + '\'' +
                '}';
    }
}
