package is.hi.hbv601g.hopby.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Button mapButton;
    private String prevAddress;
    private Marker mapPrevMarker;
    private  ArrayList<Session> mSessions;
    private HashMap<String, Double> markerLocation;
    private Double COORDINATE_OFFSET = 0.0003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get sessions from overview
        mSessions = SessionOverviewActivity.getSessionArrayList();
        markerLocation = new HashMap<String, Double>();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Reykjavík and move the camera
        LatLng reykjavik = new LatLng(64.1466, -21.9426);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reykjavik, 13f));

        // Add markers on map
        addCurrentMarkers(mSessions);

        // Make markers do something when clicked
        mMap.setOnMarkerClickListener(this);

        // Close map
        mapButton = (Button) findViewById(R.id.maps_button_finish);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(String location) {
        return (markerLocation.containsKey(location));
    }

    // Adds markers to correct from current sessions
    private void addCurrentMarkers(ArrayList sessions) {
        int length = sessions.size();

        for(int i = 0; i < length; i++) {
            String location = mSessions.get(i).getLocation();
            int sport = mSessions.get(i).getHobbyId();
            LatLng coordinates = getLocationFromAddress(getApplicationContext(), location);

            if(mapAlreadyHasMarkerForLocation(location)){
                markerLocation.put(location, markerLocation.get(location) + COORDINATE_OFFSET );
                coordinates = new LatLng(coordinates.latitude+markerLocation.get(location),coordinates.longitude+markerLocation.get(location));
                Log.d("herna", "kemst hingað "+(coordinates.latitude+markerLocation.get(location)));
            }
            else{
                markerLocation.put(location, 0.0);
            }

            // Draw markers on map with correct icon depending on activity
            switch (sport) {
                case 1:
                    mMap.addMarker((new MarkerOptions().position(coordinates).alpha(0.6f).title("Football - " + location).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_soccer_24)))).setTag(0);
                    break;
                case 2:
                    mMap.addMarker((new MarkerOptions().position(coordinates).alpha(0.6f).title("Basketball - " + location).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_basketball_24)))).setTag(0);
                    break;
                default:
                    mMap.addMarker((new MarkerOptions().position(coordinates).alpha(0.6f).title("Hike - " + location).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_walk_24)))).setTag(0);
            }
        }
    }

    // Draws markers on map
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is used to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is used to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is used to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is used to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is used to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Returns coordinates from address name
    private LatLng getLocationFromAddress(Context context, String inputtedAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(inputtedAddress, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            //Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return resLatLng;
    }


    // Do something when a marker is selected
    // Increases opacity
    // TODO open session info when double clicked
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (prevAddress == null){
            marker.setAlpha(1.0f);
            prevAddress = marker.getTitle();
            mapPrevMarker = marker;
        }
        else if (!marker.getTitle().equals(prevAddress)){
            marker.setAlpha(1.0f);
            mapPrevMarker.setAlpha(0.6f);
            prevAddress = marker.getTitle();
            mapPrevMarker = marker;
        }

        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}