package com.sparsity.routeplanner;

import com.sparsity.routeplanner.visualization.SolutionPanel;
import com.sparsity.routeplanner.vrpdomain.*;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Customer> customerList = new ArrayList<>();
        List<Location> locationList = new ArrayList<>();
        List<Depot> depotList = new ArrayList<>();
        List<Vehicle> vehicleList = new ArrayList<>();

        double[] lats = {2,64,33,77,96,53,38,7,7,73,4,82,30,38,42,5,1,94,50,35,14,94,23,96,74,68,73,92,4,24,52,61,38,90,58,93,8,13,34,84,35,96,73,21,28,55,56,68,30,85,85,0,12,26,72,71,33,51,93,70,54,26};
        double[] lons = {48,71,83,89,61,55,10,40,1,68,24,56,56,62,53,49,85,4,70,69,41,73,96,88,94,52,22,96,48,10,72,46,9,15,41,41,74,87,68,97,20,79,87,1,93,94,84,99,53,49,2,30,71,55,75,21,49,2,7,22,53,17};
        int[] demands = {0,26,18,16,8,7,11,4,9,9,16,7,6,1,2,22,23,4,3,20,7,1,2,12,22,6,11,12,2,14,14,2,9,20,2,18,19,18,4,16,26,3,23,16,10,9,21,24,24,19,12,16,6,2,2,2,23,16,26,21,2,7};
        int vehicleCount = 8;

        for (int i = 0; i < demands.length; i++) {
            Location newLocation = new Location(i, lats[i], lons[i]);
            newLocation.setName("Loc" + String.valueOf(i));
            locationList.add(newLocation);
            if (i == 0) {
                Depot newDepot = new Depot();
                newDepot.setLocation(newLocation);
                depotList.add(newDepot);
            } else {
                Customer newCustomer = new Customer();
                newCustomer.setId("Customer_" + String.valueOf(i));
                newCustomer.setLocation(newLocation);
                newCustomer.setDemand(demands[i]);
                customerList.add(newCustomer);
            }
        }

        for (int i = 0; i < vehicleCount; i++) {
            Vehicle newVehicle = new Vehicle();
            newVehicle.setId("Vehicle_" + String.valueOf(i));
            newVehicle.setCapacity(100);
            newVehicle.setDepot(depotList.get(0));
            vehicleList.add(newVehicle);
        }

        SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.createFromXmlResource("com/sparsity/routeplanner/solver/cvrpConfig.xml");
        Solver<VehicleRoutingSolution> solver = solverFactory.buildSolver();

        VehicleRoutingSolution unsolvedProblem = new VehicleRoutingSolution();
        unsolvedProblem.setCustomerList(customerList);
        unsolvedProblem.setDepotList(depotList);
        unsolvedProblem.setLocationList(locationList);
        unsolvedProblem.setVehicleList(vehicleList);
        unsolvedProblem.setName("Toy problem");

        SolutionPanel solutionPanel = new SolutionPanel();

        JFrame frame = new JFrame("Solution");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024,768);
        frame.add(solutionPanel);
        frame.setVisible( true );

        solver.addEventListener(new SolverEventListener<VehicleRoutingSolution>() {
            public void bestSolutionChanged(BestSolutionChangedEvent<VehicleRoutingSolution> event) {

                solutionPanel.updatePanel(event.getNewBestSolution());
            }
        });

        VehicleRoutingSolution solvedProblem = solver.solve(unsolvedProblem);
    }
}
