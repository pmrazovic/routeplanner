package com.sparsity.routeplanner.visualization;

import com.sparsity.routeplanner.vrpdomain.*;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SolutionPainter {

    private static final int TEXT_SIZE = 12;
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0.00");
    private static final String IMAGE_PATH_PREFIX = "/com/sparsity/routeplanner/gui/";

    private ImageIcon depotImageIcon;
    private ImageIcon vehicleImageIcon;

    private BufferedImage canvas = null;
    private LatitudeLongitudeTranslator translator = null;

    public SolutionPainter() {
        depotImageIcon = new ImageIcon(getClass().getResource(IMAGE_PATH_PREFIX + "depot.png"));
        vehicleImageIcon = new ImageIcon(getClass().getResource(IMAGE_PATH_PREFIX + "vehicle.png"));
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public void reset(VehicleRoutingSolution solution, Dimension size, ImageObserver imageObserver) {
        translator = new LatitudeLongitudeTranslator();
        for (Location location : solution.getLocationList()) {
            translator.addCoordinates(location.getLatitude(), location.getLongitude());
        }

        double width = size.getWidth();
        double height = size.getHeight();
        translator.prepareFor(width, height - 10 - TEXT_SIZE);

        Graphics2D g = createCanvas(width, height);
        g.setFont(g.getFont().deriveFont((float) TEXT_SIZE));
        g.setStroke(TangoColorFactory.NORMAL_STROKE);

        for (Customer customer : solution.getCustomerList()) {
            Location location = customer.getLocation();
            int x = translator.translateLongitudeToX(location.getLongitude());
            int y = translator.translateLatitudeToY(location.getLatitude());
            g.setColor(TangoColorFactory.ALUMINIUM_4);
            g.fillRect(x - 1, y - 1, 3, 3);
            String demandString = Integer.toString(customer.getDemand());
            g.drawString(demandString, x - (g.getFontMetrics().stringWidth(demandString) / 2), y - TEXT_SIZE / 2);
            if (customer.getPriority() > 0) {
                g.setColor(TangoColorFactory.SCARLET_2);
                String priorityString = "Priority";
                g.drawString(priorityString, x - (g.getFontMetrics().stringWidth(priorityString) / 2), y - TEXT_SIZE * 2 );
            }
        }

        g.setColor(TangoColorFactory.ALUMINIUM_3);

        for (Depot depot : solution.getDepotList()) {
            int x = translator.translateLongitudeToX(depot.getLocation().getLongitude());
            int y = translator.translateLatitudeToY(depot.getLocation().getLatitude());
            g.fillRect(x - 2, y - 2, 5, 5);
            g.drawImage(depotImageIcon.getImage(),
                    x - depotImageIcon.getIconWidth() / 2, y - 2 - depotImageIcon.getIconHeight(), imageObserver);
        }

        int colorIndex = 0;
        for (Vehicle vehicle : solution.getVehicleList()) {
            g.setColor(TangoColorFactory.SEQUENCE_2[colorIndex]);
            Customer vehicleInfoCustomer = null;
            double longestNonDepotDistance = -1L;
            int load = 0;
            double totalDistance = 0;
            for (Customer customer : solution.getCustomerList()) {
                if (customer.getPreviousStandstill() != null && customer.getVehicle() == vehicle) {
                    load += customer.getDemand();
                    totalDistance += customer.getDistanceFromPreviousStandstill();
                    Location previousLocation = customer.getPreviousStandstill().getLocation();
                    Location location = customer.getLocation();
                    translator.drawRoute(g, previousLocation.getLongitude(), previousLocation.getLatitude(),
                            location.getLongitude(), location.getLatitude(),true, false);
                    // Determine where to draw the vehicle info
                    double distance = customer.getDistanceFromPreviousStandstill();
                    if (customer.getPreviousStandstill() instanceof Customer) {
                        if (longestNonDepotDistance < distance) {
                            longestNonDepotDistance = distance;
                            vehicleInfoCustomer = customer;
                        }
                    } else if (vehicleInfoCustomer == null) {
                        // If there is only 1 customer in this chain, draw it on a line to the Depot anyway
                        vehicleInfoCustomer = customer;
                    }
                    // Line back to the vehicle depot
                    if (customer.getNextCustomer() == null) {
                        Location vehicleLocation = vehicle.getLocation();
                        totalDistance += customer.getDistanceTo(vehicle);
                        translator.drawRoute(g, location.getLongitude(), location.getLatitude(),
                                vehicleLocation.getLongitude(), vehicleLocation.getLatitude(), true, true);
                    }
                }
            }
            // Draw vehicle info
            if (vehicleInfoCustomer != null) {
                Location previousLocation = vehicleInfoCustomer.getPreviousStandstill().getLocation();
                Location location = vehicleInfoCustomer.getLocation();
                double longitude = (previousLocation.getLongitude() + location.getLongitude()) / 2.0;
                int x = translator.translateLongitudeToX(longitude);
                double latitude = (previousLocation.getLatitude() + location.getLatitude()) / 2.0;
                int y = translator.translateLatitudeToY(latitude);
                boolean ascending = (previousLocation.getLongitude() < location.getLongitude())
                        ^ (previousLocation.getLatitude() < location.getLatitude());

                int vehicleInfoHeight = vehicleImageIcon.getIconHeight() + 2 + TEXT_SIZE;
                g.drawImage(vehicleImageIcon.getImage(),
                        x + 1, (ascending ? y - vehicleInfoHeight - 1 : y + 1), imageObserver);
                if (load > vehicle.getCapacity()) {
                    g.setColor(TangoColorFactory.SCARLET_2);
                }
                g.drawString("Cap: " + load + "/" + vehicle.getCapacity(),
                        x + 1, (ascending ? y - 1 : y + vehicleInfoHeight + 1));
                if (totalDistance > vehicle.getMaxDistance()) {
                    g.setColor(TangoColorFactory.SCARLET_2);
                }
                g.drawString("Dist: " + NUMBER_FORMAT.format(totalDistance) + "/" + NUMBER_FORMAT.format(vehicle.getMaxDistance()),
                        x + 1, (ascending ? y + TEXT_SIZE - 1 : y + vehicleInfoHeight + TEXT_SIZE + 1));
            }
            colorIndex = (colorIndex + 1) % TangoColorFactory.SEQUENCE_2.length;
        }

        // Legend
        g.setColor(TangoColorFactory.ALUMINIUM_3);
        g.fillRect(5, (int) height - 12 - TEXT_SIZE - (TEXT_SIZE / 2), 5, 5);
        g.drawString("Depot", 15, (int) height - 10 - TEXT_SIZE);
        String vehiclesSizeString = solution.getVehicleList().size() + " vehicles";
        g.drawString(vehiclesSizeString,
                ((int) width - g.getFontMetrics().stringWidth(vehiclesSizeString)) / 2, (int) height - 10 - TEXT_SIZE);
        g.setColor(TangoColorFactory.ALUMINIUM_4);
        g.fillRect(6, (int) height - 6 - (TEXT_SIZE / 2), 3, 3);
        g.drawString("Customer: demand", 15, (int) height - 5);
        String customersSizeString = solution.getCustomerList().size() + " customers";
        g.drawString(customersSizeString,
                ((int) width - g.getFontMetrics().stringWidth(customersSizeString)) / 2, (int) height - 5);
        // Show soft score
        g.setColor(TangoColorFactory.ORANGE_3);
        HardSoftDoubleScore score = solution.getScore();
        if (score != null) {
            String distanceString;
            if (!score.isFeasible()) {
                distanceString = "Not feasible";
            } else {
                distanceString = solution.getDistanceString(NUMBER_FORMAT);
            }
            g.setFont(g.getFont().deriveFont(Font.BOLD, (float) TEXT_SIZE * 2));
            g.drawString(distanceString,
                    (int) width - g.getFontMetrics().stringWidth(distanceString) - 10, (int) height - 10 - TEXT_SIZE);
        }

    }

    public Graphics2D createCanvas(double width, double height) {
        int canvasWidth = (int) Math.ceil(width) + 1;
        int canvasHeight = (int) Math.ceil(height) + 1;
        canvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        return g;
    }
}
