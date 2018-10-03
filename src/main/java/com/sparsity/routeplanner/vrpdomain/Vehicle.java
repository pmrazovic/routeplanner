package com.sparsity.routeplanner.vrpdomain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.math.BigDecimal;

public class Vehicle implements Standstill {

    protected String id;

    protected int capacity;
    protected Depot depot;
    protected long maxDistance;
    boolean dummy = false;

    // Shadow variables
    protected Customer nextCustomer;

    @PlanningId
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getMaxDistance() {
        return maxDistance;
    }

    // TODO: Choose scaling factor
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = (long) (maxDistance * 1000.0);
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    @Override
    public Customer getNextCustomer() {
        return nextCustomer;
    }

    @Override
    public void setNextCustomer(Customer nextCustomer) {
        this.nextCustomer = nextCustomer;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    @Override
    public Vehicle getVehicle() {
        return this;
    }

    @Override
    public Location getLocation() {
        return depot.getLocation();
    }

    public long getDistanceTo(Standstill standstill) {
        return depot.getDistanceTo(standstill);
    }

    @Override
    public int getVisitIndex() {
        return 0;
    }

    @Override
    public String toString() {
        Location location = getLocation();
        if (location.getName() == null) {
            return super.toString();
        }
        return location.getName() + "/" + super.toString();
    }

}
