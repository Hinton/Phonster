package se.killergameab.phonster.map;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

public class GoogleMapPresenter {

    private Map map;

    public GoogleMapPresenter(Map map) {
        this.map = map;
    }

    public void setup(GoogleMap googleMap) {

        setupZones(googleMap, map.getZones());

    }

    private void setupZones(GoogleMap googleMap, List<Zone> zones) {

        for (Zone z : zones) {
            setupZone(googleMap, z);
        }

    }

    private void setupZone(GoogleMap googleMap, Zone z) {

        PolygonOptions polygonOptions = new PolygonOptions();

        for (LatLng coordinate : z.getCoordinates()) {
            polygonOptions.add(coordinate);
        }

        //        .add(new LatLng(55.714307, 13.210))
        //        .add(new LatLng(55.714307, 13.216))  // North of the previous point, but at the same longitude
        //        .add(new LatLng(55.713266, 13.216))  // Same latitude, and 30km to the west
        //        .add(new LatLng(55.713266, 13.210))  // Same longitude, and 16km to the south
        //        .add(new LatLng(55.714307, 13.210)); // Closes the polyline.

        polygonOptions.strokeWidth(0.5f);
        polygonOptions.fillColor(Color.argb(100, 255, 0, 0));

        googleMap.addPolygon(polygonOptions);
    }


}
