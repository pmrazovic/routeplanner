package com.sparsity.routeplanner.vrpdomain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;

// TODO: Not implemented yet...
public class TWCustomer {

    private long readyTime;
    private long dueTime;
    private long serviceDuration;

    // Shadow variable
    private Long arrivalTime;

    public long getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(long readyTime) {
        this.readyTime = readyTime;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public long getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(long serviceDuration) {
        this.serviceDuration = serviceDuration;
    }


}
