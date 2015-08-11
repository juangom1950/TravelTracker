package bitfontain.juangomez.com.traveltracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "MainActivity";
    private static final String MEMORY_DIALOG_TAG = "MemoryDialog";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, Memory> mMemories = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //addGoogleAPIClient();
    }

    /*Implements this interface at the top OnMapReadyCallback
        * Alt + Ins > Override Methods and add this method.
        * This method is going to be called as soon the map is going to be loaded*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Show current location on the maps
        mMap.setMyLocationEnabled(true);
        //Add this to be able to get coordinates when you click google maps.
        //Implement interface OnMapClickListener and get coordinates in its method
        mMap.setOnMapClickListener(this);
        //We set our adapter and pass the layout inflater. The adapter is the one that creates the view for us
        mMap.setInfoWindowAdapter(new MarkerAdapter(getLayoutInflater(), mMemories));
    }

    //Implement method from interface OnMapClickListener
    //With this method we can get coordinates when we click the map
    @Override
    public void onMapClick(LatLng latLng) {

        //Log.d(TAG, "Latlng is" + latLng);

        Geocoder geocoder = new Geocoder(this);

        List<Address> matches = null;

        try {
            //Last parameter 1 = maxResults: max number of addresses to return. Smaller numbers (1 to 5) are recommended
            matches = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address bestMatch = (matches.isEmpty()) ? null : matches.get(0);
        //Log.d(TAG, "Best match is" + bestMatch);
        int maxLine = bestMatch.getMaxAddressLineIndex();

        Memory memory = new Memory();
        memory.city = bestMatch.getAddressLine(maxLine -1);
        memory.country = bestMatch.getAddressLine(maxLine);
        memory.latitude = latLng.latitude;
        memory.longitude = latLng.longitude;
        memory.notes = "My notes...";

        //Show Dialog
        new MemoryDialogFragment().show(getFragmentManager(), MEMORY_DIALOG_TAG);

        Marker marker = mMap.addMarker(new MarkerOptions()
                //We need the position so it knows where to add the marker
                .position(latLng));
                /*.title(bestMatch.getAddressLine(maxLine - 1))
                .snippet(bestMatch.getAddressLine(maxLine)));*/

        mMemories.put(marker.getId(), memory);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Connects with the Google API. When it gets called it turns everything off.
        //If everything goes well onConnected method gets called and if something fails onConnectionFailed gets called
        //mGoogleApiClient.connect();
    }

    //Call this method in onStart and onCreate if you want to get coordinates back in onConnected method
    private void addGoogleAPIClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //Implement methods of interfaces GoogleApiClient.ConnectionCallbacks and GoogleApiClient.OnConnectionFailedListener
    //**********************************************************************************
    @Override
    public void onConnected(Bundle bundle) {
        //Get user's location
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*************************************************************************************/


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
