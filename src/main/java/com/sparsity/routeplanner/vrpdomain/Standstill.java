package com.sparsity.routeplanner.vrpdomain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public interface Standstill {

    Location getLocation();

    Vehicle getVehicle();

    @InverseRelationShadowVariable(sourceVariableName = "previousStandstill")
    Customer getNextCustomer();
    void setNextCustomer(Customer nextCustomer);

    int getVisitIndex();

}
