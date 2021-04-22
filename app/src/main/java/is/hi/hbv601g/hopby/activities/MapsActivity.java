package is.hi.hbv601g.hopby.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
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
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, Serializable {

    private GoogleMap mMap;
    private Button mMapButton;
    private String mPrevAddress;
    private Marker mPrevMarker;
    private  ArrayList<Session> mSessions;
    private HashMap<String, Double> mMarkerLocations;
    private Double mCoordinateOffset = 0.0003;
    private Session mSession;
    private String mCheckFlag;
    private Geocoder mGeocoder;
    private List<Address> mAddresses;
    private Marker mCreateMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSessions = new ArrayList<Session>();

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
        mMarkerLocations = new HashMap<String, Double>();


        // Add all or filtered sessions if overview or one session if info
        Intent i = getIntent();
        mCheckFlag = i.getStringExtra("flag");
        if (mCheckFlag.equals("overview")) {
            //mSessions = i.getParcelableExtra("sessions"); TODO find out how to pass sessions through intent
            mSessions = SessionOverviewActivity.getSessionArrayList();
        }
        else if (mCheckFlag.equals("info")){
            // TODO find better ("correct") method for this
            mSession = SessionService.getSessionForMaps();
            mSessions.add(mSession);
        }

        // Add a marker in Reykjavík and move the camera
        LatLng reykjavik = new LatLng(64.1466, -21.9426);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reykjavik, 13f));

        if (mCheckFlag.equals("create")){
            System.out.println("prump");
            mGeocoder = new Geocoder(this, Locale.getDefault());
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        mAddresses = mGeocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mCreateMarker != null){
                        mCreateMarker.remove();
                    }
                    mCreateMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Address: "+mAddresses.get(0).getAddressLine(0)));
                    Toast.makeText(MapsActivity.this, mAddresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            // Add markers on map
            addCurrentMarkers(mSessions);
        }



        // Make markers do something when clicked
        mMap.setOnMarkerClickListener(this);

        // Close map
        mMapButton = (Button) findViewById(R.id.maps_button_finish);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Adds markers to correct from current sessions
    private void addCurrentMarkers(ArrayList sessions) {
        int length = sessions.size();

        for(int i = 0; i < length; i++) {
            String location = mSessions.get(i).getLocation();
            int sport = mSessions.get(i).getHobbyId();
            Long id = mSessions.get(i).getId();
            String time = mSessions.get(i).getTime();
            String date = mSessions.get(i).getDate();
            String dateTime = date.substring(8,10) +"."+ date.substring(5,7) + "."+ date.substring(0,4)+" at "  + time.substring(0,5);

            LatLng coordinates = getLocationFromAddress(getApplicationContext(), location);
            String coordinatesString = coordinates.toString();

            // If only 1 session zoom in on that session
            if (mSessions.size() == 1) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates,15f));
            }

            // If location is already in use apply offset
            if(mapAlreadyHasMarkerForLocation(coordinatesString)){
                mMarkerLocations.put(coordinatesString, mMarkerLocations.get(coordinatesString) + mCoordinateOffset );
                coordinates = new LatLng(coordinates.latitude+mMarkerLocations.get(coordinatesString),coordinates.longitude+mMarkerLocations.get(coordinatesString));
            }
            else{
                mMarkerLocations.put(coordinatesString, 0.0);
            }

            // Draw markers on map with correct icon depending on activity
            switch (sport) {
                case 1:
                    mMap.addMarker((new MarkerOptions().position(coordinates).alpha(0.6f).title("Football  "+ dateTime).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_soccer_24)))).setTag(id);
                    break;
                case 2:
                    mMap.addMarker((new MarkerOptions().position(coordinates).alpha(0.6f).title("Basketball  "+ dateTime).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_basketball_24)))).setTag(id);
                    break;
                default:
                    mMap.addMarker((new MarkerOptions().position(coordinates).alpha(0.6f).title("Hike  "+ dateTime).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_walk_24)))).setTag(id);
            }
        }
    }



    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(String location) {
        return (mMarkerLocations.containsKey(location));
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


    // Opens sessionInfo when double clicked on marker
    // Increases opacity of selected marker
    @Override
    public boolean onMarkerClick(final Marker marker) {

        Long id = (Long) marker.getTag();
        if (mCheckFlag.equals("overview")) {

            if (mPrevAddress == null) {
                marker.setAlpha(1.0f);
                mPrevAddress = marker.getTitle();
                mPrevMarker = marker;
                Toast.makeText(this,
                        " Click marker again for session info",
                        Toast.LENGTH_SHORT).show();
            } else if (!marker.getTitle().equals(mPrevAddress)) {
                marker.setAlpha(1.0f);
                mPrevMarker.setAlpha(0.6f);
                mPrevAddress = marker.getTitle();
                mPrevMarker = marker;
                Toast.makeText(this,
                        "Click marker again for session info",
                        Toast.LENGTH_SHORT).show();
            } else {
                String sId = Long.toString(id);
                Intent intent = new Intent(this, SessionInfoActivity.class);
                intent.putExtra("id", sId);
                this.startActivity(intent);
            }
        }
        else {
            marker.setAlpha(1.0f);
        }
        return false;
    }
}