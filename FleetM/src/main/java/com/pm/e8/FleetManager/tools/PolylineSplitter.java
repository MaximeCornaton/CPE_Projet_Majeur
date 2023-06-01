package com.pm.e8.FleetManager.tools;

import java.util.ArrayList;
import java.util.List;
import com.google.maps.model.LatLng;
import com.google.maps.model.EncodedPolyline;
import com.mapbox.geojson.Point;
import com.project.model.dto.Coord;
import com.mapbox.geojson.utils.PolylineUtils;

public class PolylineSplitter {

    private static final double EARTH_RADIUS = 6371;
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    private static Coord interpolate(Coord coord1, Coord coord2, double fraction) {
        double lat1 = deg2rad(coord1.getLat());
        double lon1 = deg2rad(coord1.getLon());
        double lat2 = deg2rad(coord2.getLat());
        double lon2 = deg2rad(coord2.getLon());

        double d = 2 * Math.asin(Math.sqrt(Math.pow((Math.sin((lat1 - lat2) / 2)), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon1 - lon2) / 2), 2)));
        double A = Math.sin((1 - fraction) * d) / Math.sin(d);
        double B = Math.sin(fraction * d) / Math.sin(d);
        double x = A * Math.cos(lat1) * Math.cos(lon1) + B * Math.cos(lat2) * Math.cos(lon2);
        double y = A * Math.cos(lat1) * Math.sin(lon1) + B * Math.cos(lat2) * Math.sin(lon2);
        double z = A * Math.sin(lat1) + B * Math.sin(lat2);

        Coord coord = new Coord();
        coord.setLat(rad2deg(Math.atan2(z, Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)))));
        coord.setLon(rad2deg(Math.atan2(y, x)));
        return coord;
    }

    public static List<Coord> cutPolyline(String polyline, double distance) {
        List<Point> pointsList = PolylineUtils.decode(polyline,6);
        List<Coord> coordList = new ArrayList<>();
        Coord previousCoord = null;
        for (Point latLng : pointsList) {
            Coord currentCoord = new Coord();
            currentCoord.setLat(latLng.latitude());
            currentCoord.setLon(latLng.longitude());
            if (previousCoord != null) {
                double d = calculateDistance(previousCoord, currentCoord);
                for (double i = distance; i < d; i += distance) {
                    Coord newCoord = interpolate(previousCoord, currentCoord, i / d);
                    coordList.add(newCoord);
                }
            }
            coordList.add(currentCoord);
            previousCoord = currentCoord;
        }

        return coordList;
    }

    public static List<Coord> decodePolyline(String polyline) {
        List<Coord> coordonneesList = new ArrayList<>();

        try {
            EncodedPolyline encodedPolyline = new EncodedPolyline(polyline);
            List<com.google.maps.model.LatLng> latLngs = encodedPolyline.decodePath();
            for (com.google.maps.model.LatLng latLng : latLngs) {
                Coord coord = new Coord();
                coord.setLat(latLng.lat);
                coord.setLon(latLng.lng);
                coordonneesList.add(coord);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordonneesList;
    }

    private static double calculateDistance(Coord coord1, Coord coord2) {
        double lat1 = deg2rad(coord1.getLat());
        double lon1 = deg2rad(coord1.getLon());
        double lat2 = deg2rad(coord2.getLat());
        double lon2 = deg2rad(coord2.getLon());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
