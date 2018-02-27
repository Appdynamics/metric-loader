package com.appdy.metricloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricData {
    private long metricId;
    private String metricPath;
    private String metricName;
    private String frequency;
    private List<MetricValue> metricValues;
    private Long rolledUpValue;
    private Long maxRolledUpValue;


    public long getMetricId() {
        return metricId;
    }

    public void setMetricId(long metricId) {
        this.metricId = metricId;
    }

    public String getMetricPath() {
        return metricPath;
    }

    public void setMetricPath(String metricPath) {
        this.metricPath = metricPath;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<MetricValue> getMetricValues() {
        return metricValues;
    }

    public void setMetricValues(List<MetricValue> metricValues) {
        this.metricValues = metricValues;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricName() {
        return metricName;
    }

    public Long getRolledUpValue() {
        return rolledUpValue;
    }

    public void setRolledUpValue(Long rolledUpValue) {
        this.rolledUpValue = rolledUpValue;
    }

    public Long getMaxRolledUpValue() {
        return maxRolledUpValue;
    }

    public void setMaxRolledUpValue(Long maxRolledUpValue) {
        this.maxRolledUpValue = maxRolledUpValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricData)) return false;
        MetricData that = (MetricData) o;
        return getMetricId() == that.getMetricId() &&
                Objects.equals(getMetricPath(), that.getMetricPath()) &&
                Objects.equals(getMetricName(), that.getMetricName()) &&
                Objects.equals(getFrequency(), that.getFrequency()) &&
                Objects.equals(getMetricValues(), that.getMetricValues()) &&
                Objects.equals(getRolledUpValue(), that.getRolledUpValue()) &&
                Objects.equals(getMaxRolledUpValue(), that.getMaxRolledUpValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMetricId(), getMetricPath(), getMetricName(),
                getFrequency(), getMetricValues(), getRolledUpValue(), getMaxRolledUpValue());
    }
}
