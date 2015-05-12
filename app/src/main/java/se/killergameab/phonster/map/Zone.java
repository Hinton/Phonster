package se.killergameab.phonster.map;

import com.google.android.gms.maps.model.LatLng;

public class Zone {

    private int id;
    private LatLng[] coordinates;
    private int color;

    public Zone(int id, LatLng[] coordinates, int color) {
        this.id = id;
        this.coordinates = coordinates;
        this.color = color;
    }

    public int getZoneId() {
        return id;
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
}
