package com.sparsity.routeplanner.vrpdomain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;

import java.text.NumberFormat;
import java.util.List;

@PlanningSolution
public class VehicleRoutingSolution {

    protected String name;
    protected List<Location> locationList;
    protected List<Depot> depotList;
    protected List<Vehicle> vehicleList;
    protected List<Customer> customerList;

    protected HardSoftDoubleScore score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ProblemFactCollectionProperty
    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    @ProblemFactCollectionProperty
    public List<Depot> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Depot> depotList) {
        this.depotList = depotList;
    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "vehicleRange")
    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "customerRange")
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @PlanningScore
    public HardSoftDoubleScore getScore() {
        return score;
    }

    public void setScore(HardSoftDoubleScore score) {
        this.score = score;
    }

    public String getDistanceString(NumberFormat numberFormat) {
        if (score == null) {
            return null;
        }
        double distance = - score.getSoftScore();
        return numberFormat.format(distance);
    }
}
