package se.killergameab.phonster.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Zone> zoneList;
    private List<Monster> monsterList;
    private LatLng shopLocation;

    public static Map createDemoMap() {
        Map map = new Map();

        map.addZone(new Zone(new LatLng[]{
                new LatLng(55.714307, 13.210),
                new LatLng(55.714307, 13.216),
                new LatLng(55.712266, 13.216),
                new LatLng(55.712266, 13.210),
                new LatLng(55.714307, 13.210)
        }, Color.argb(100, 255, 0, 0)));

        map.addZone(new Zone(new LatLng[]{
                new LatLng(55.716434, 13.212235),
                new LatLng(55.716434, 13.216),
                new LatLng(55.714307, 13.216),
                new LatLng(55.714307, 13.212235),
                new LatLng(55.716434, 13.212235)
        }, Color.argb(100, 0, 255, 0)));

        map.addZone(new Zone(new LatLng[]{
                new LatLng(55.716434, 13.200),
                new LatLng(55.716434, 13.212235),
                new LatLng(55.714307, 13.212235),
                new LatLng(55.714307, 13.200),
                new LatLng(55.716434, 13.200)
        }, Color.argb(100, 0, 0, 255)));

        map.setShopLocation(new LatLng(55.714937, 13.212331));

        map.addMonster(new Monster(new LatLng(55.714780, 13.212224)));

        return map;
    }

    private void addMonster(Monster monster) {
        this.monsterList.add(monster);
    }


    public Map() {
        this.zoneList = new ArrayList<>();
        this.monsterList = new ArrayList<>();
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

    public List<Monster> getMonsters() {
        return monsterList;
    }

    /**
     * Returns the zone the player currently is in
     *
     * @param position Current position
     * @return current zone
     */
    public Zone getCurrentZone(LatLng position) {

        for (Zone z : zoneList) {
            if (z.contains(position)) {
                return z;
            }
        }

        return null;
    }
}

