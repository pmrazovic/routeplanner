package com.sparsity.routeplanner.vrpdomain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import java.math.BigDecimal;

@PlanningEntity(difficultyWeightFactoryClass = DepotAngleCustomerDifficultyWeightFactory.class)
public class Customer implements Standstill {

    protected String id;
    protected Location location;
    protected int demand;
    protected int priority = 0;

    // Planning variables: changes during planning, between score calculations.
    protected Standstill previousStandstill;

    // Shadow variables
    protected Customer nextCustomer;
    protected Vehicle vehicle;

    @PlanningId
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @PlanningVariable(valueRangeProviderRefs = {"vehicleRange", "customerRange"},
            graphType = PlanningVariableGraphType.CHAINED)
    public Standstill getPreviousStandstill() {
        return previousStandstill;
    }

    public void setPreviousStandstill(Standstill previousStandstill) {
        this.previousStandstill = previousStandstill;
    }

    @Override
    public Customer getNextCustomer() {
        return nextCustomer;
    }

    @Override
    public void setNextCustomer(Customer nextCustomer) {
        this.nextCustomer = nextCustomer;
    }

    @Override
    @AnchorShadowVariable(sourceVariableName = "previousStandstill")
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public long getDistanceFromPreviousStandstill() {
        if (previousStandstill == null) {
            throw new IllegalStateException("This method must not be called when the previousStandstill ("
                    + previousStandstill + ") is not initialized yet.");
        }
        return getDistanceFrom(previousStandstill);
    }

    public long getDistanceFrom(Standstill standstill) {
        return standstill.getLocation().getDistanceTo(location);
    }

    public long getDistanceTo(Standstill standstill) {
        return location.getDistanceTo(standstill.getLocation());
    }

    // TODO: Maybe introduce shadow variable and listener class instead of these recursive calls
    @Override
    public int getVisitIndex() {
        return 1 + previousStandstill.getVisitIndex();
    }

    @Override
    public String toString() {
        if (location.getName() == null) {
            return super.toString();
        }
        return location.getName();
    }

}
