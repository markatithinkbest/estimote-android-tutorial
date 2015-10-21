package com.example.airport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    private static final Map<String, List<String>> PLACES_BY_BEACONS;
    TextView textView;

    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        //39413:43154
//        54892:15763
//        42060:30229
//        21812:32624
//        50378:64977
//        51157:29179



        placesByBeacons.put("39413:43154", new ArrayList<String>() {{
            add("111111111111");
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("Green & Green Salads");
            // "Green & Green Salads" is the next closest
            add("Mini Panini");
            // "Mini Panini" is the furthest away
        }});

        placesByBeacons.put("54892:15763", new ArrayList<String>() {{
            add("2222222222222");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        placesByBeacons.put("42060:30229", new ArrayList<String>() {{
            add("3333333333333333");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        placesByBeacons.put("21812:32624", new ArrayList<String>() {{
            add("4444444444444");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        placesByBeacons.put("50378:64977", new ArrayList<String>() {{
            add("55555");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        placesByBeacons.put("51157:29179", new ArrayList<String>() {{
            add("66666");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView);
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Log.d("Airport", "Nearest places: " + places);
                    String str="";
                    for(String p:places){
                        str+=p+"\n";
                    }

                    textView.setText(str);

                }
            }
        });

        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
       beaconManager.stopRanging(region);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
