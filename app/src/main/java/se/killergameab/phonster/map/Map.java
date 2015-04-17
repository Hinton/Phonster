package se.killergameab.phonster.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Zone> zoneList;
    private LatLng shopLocation;

    public static Map createDemoMap() {
        Map map = new Map();

        map.addZone(new Zone(new LatLng[]{
                new LatLng(55.714307, 13.210),
                new LatLng(55.714307, 13.216),
                new LatLng(55.713266, 13.216),
                new LatLng(55.713266, 13.210),
                new LatLng(55.714307, 13.210)
        }, Color.argb(100, 255, 0, 0)));

        map.setShopLocation(new LatLng(55.714937, 13.212331));

        return map;
    }


    public Map() {
        this.zoneList = new ArrayList<>();
    }

    public void addZone(Zone z) {
        zoneList.add(z);
    }

    public List<Zone> getZones() {
        return zoneList;
    }


    public void setShopLocation(LatLng shopLocation) {
        this.shopLocation = shopLocation;
    }

    public LatLng getShopLocation() {
        return shopLocation;
    }
}

