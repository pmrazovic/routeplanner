package com.sparsity.routeplanner.vrpdomain;

import java.math.BigDecimal;

public class Location {

    protected long id;
    protected String name = null;
    protected double latitude;
    protected double longitude;

    public Location() {
    }

    public Location(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDistanceTo(Location location) {
        return getAirDistanceTo(location);
    }

    public long getAirDistanceTo(Location location) {
        double latitudeDifference = location.latitude - latitude;
        double longitudeDifference = location.longitude - longitude;
        double distance = Math.sqrt((latitudeDifference * latitudeDifference) + (longitudeDifference * longitudeDifference));
        // TODO: Choose adequate factor
        return (long) (distance * 1000.0);
    }

    /**
     * The angle relative to the direction EAST.
     * @param location never null
     * @return in Cartesian coordinates
     */
    public double getAngle(Location location) {
        // Euclidean distance (Pythagorean theorem) - not correct when the surface is a sphere
        double latitudeDifference = location.latitude - latitude;
        double longitudeDifference = location.longitude - longitude;
        return Math.atan2(latitudeDifference, longitudeDifference);
    }


    @Override
    public String toString() {
        if (name == null) {
            return super.toString();
        }
        return name;
    }

}
