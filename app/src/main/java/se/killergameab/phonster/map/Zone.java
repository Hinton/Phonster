package se.killergameab.phonster.map;

import com.google.android.gms.maps.model.LatLng;

public class Zone {

    private LatLng[] coordinates;
    private int color;

    public Zone(LatLng[] coordinates, int color) {
        this.coordinates = coordinates;
        this.color = color;
    }

    public LatLng[] getCoordinates() {
        return coordinates;
    }

    public int getColor() {
        return color;
    }

    public boolean contains(LatLng position) {

        int i;
        int j;
        boolean result = false;
        for (i = 0, j = coordinates.length - 1; i < coordinates.length; j = i++) {
            if ((coordinates[i].longitude > position.longitude) != (coordinates[j].longitude > position.longitude) &&
                    (position.latitude < (coordinates[j].latitude - coordinates[i].latitude) * (position.longitude - coordinates[i].longitude) / (coordinates[j].longitude-coordinates[i].longitude) + coordinates[i].latitude)) {
                result = !result;
            }
        }
        return result;

    }

    private boolean inLongitude(double longitude) {
        return coordinates[0].longitude < longitude && coordinates[2].longitude > longitude;
    }

    public boolean inLatitude(double latitude) {
        return coordinates[0].latitude < latitude && coordinates[2].latitude > latitude;
    }
}
