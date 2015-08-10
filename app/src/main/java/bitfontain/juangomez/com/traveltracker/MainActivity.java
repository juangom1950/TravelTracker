package bitfontain.juangomez.com.traveltracker;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addGoogleAPIClient();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Connects with the Google API. When it gets called it turns everything off.
        //If everything goes well onConnected method gets called and if something fails onConnectionFailed gets called
        mGoogleApiClient.connect();
    }

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

    /*************************************************************************************

    /*Implements this interface at the top OnMapReadyCallback
        * Alt + Ins > Override Methods and add this method.
        * This method is going to be called as soon the map is going to be loaded*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Show current location on the map
        mMap.setMyLocationEnabled(true);
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
