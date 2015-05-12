package se.killergameab.phonster;

import android.content.Intent;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import se.killergameab.phonster.map.GoogleMapPresenter;
import se.killergameab.phonster.map.Map;
import se.killergameab.phonster.map.Zone;

import android.media.MediaPlayer;

public class MapsActivity extends FragmentActivity {

    private GoogleMap gMap; // Might be null if Google Play services APK is not available.
    public MediaPlayer mp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mp1 = MediaPlayer.create(this, R.raw.song1);
        mp1.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #gMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            gMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (gMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #gMap} is not null.
     */
    private void setUpMap() {
        UiSettings settings = gMap.getUiSettings();

        settings.setAllGesturesEnabled(false);
        settings.setCompassEnabled(false);
        settings.setMapToolbarEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        settings.setZoomControlsEnabled(false);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.71445, 13.212652), 17));
        gMap.setMyLocationEnabled(true);
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

        final Map map = Map.createDemoMap();

        GoogleMapPresenter googleMapPresenter = new GoogleMapPresenter(map);
        googleMapPresenter.setup(gMap);

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");

                Location myLocation = gMap.getMyLocation();
                //System.out.println(myLocation.getLatitude() + " " + myLocation.getLongitude());
                Zone z = map.getCurrentZone(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));

                int id = -1;
                if (z != null) {
                    id = z.getZoneId();
                }

                Intent i = new Intent(getApplicationContext(), AimingActivity.class);
                i.putExtra("zone", id);
                startActivity(i);
                
            }
        }.start();
    }
    // Use http://www.birdtheme.org/useful/v3tool.html to get coordinates
}
