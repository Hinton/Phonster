package se.killergameab.phonster.map;

import com.google.android.gms.maps.model.LatLng;

public class Monster {

    private LatLng position;

    public Monster(LatLng position) {
        this.position = position;
    }

    public LatLng getPosition() {
        return position;
    }

}
