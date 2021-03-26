package is.hi.hbv601g.hopby.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.hopby.R;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Button mapButton;
    private ArrayList<String[]> markers = new ArrayList <String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reykjavik, 12f));

        markers.add(new String[]{"Reykjavík","Ganga"});
        markers.add(new String[]{"Laugalækjarskóli","Körfubolti"});
        markers.add(new String[]{"Álftamýri","Fótbolti"});
        markers.add(new String[]{"Laugardalsvöllur","Fótbolti"});
        markers.add(new String[]{"Akureyri","Ganga"});
        markers.add(new String[]{"Fagradalsfjall", "Ganga"});

        addMarkers(markers); // Add markers on map
        mMap.setOnMarkerClickListener(this); // Make markers do something when clicked

        mapButton = (Button) findViewById(R.id.button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapsActivity.this, SessionOverviewActivity.class);
                startActivity(intent);

            }
        });
    }

    private void addMarkers(ArrayList markers) {

        int lenMarkers = markers.size();
        String [] locSpo;

        for(int i = 0; i < lenMarkers; i++) {
            locSpo = (String[]) markers.get(i); // each instance of location + sport
            String locName = locSpo[0]; // location address
            String sport = locSpo[1]; // type of sport
            LatLng loc = getLocationFromAddress(getApplicationContext(), locName); // coordinates for location from address

            switch (sport) {
                case "Körfubolti":
                    mMap.addMarker((new MarkerOptions().position(loc).title(sport + " " + locName).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_basketball_24)))).setTag(0);
                    break;
                case "Fótbolti":
                    mMap.addMarker((new MarkerOptions().position(loc).title(sport + " " + locName).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_soccer_24)))).setTag(0);
                    break;
                default:
                    mMap.addMarker((new MarkerOptions().position(loc).title(sport + " " + locName).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_walk_24)))).setTag(0);

            }
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return resLatLng;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
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