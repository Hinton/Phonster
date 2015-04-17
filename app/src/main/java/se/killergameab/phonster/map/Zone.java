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

}
