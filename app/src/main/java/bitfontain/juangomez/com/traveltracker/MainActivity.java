package bitfontain.juangomez.com.traveltracker;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
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


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerDragListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener, MemoryDialogFragment.Listener, GoogleMap.OnInfoWindowClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "MainActivity";
    private static final String MEMORY_DIALOG_TAG = "MemoryDialog";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    //Hasg map: You provide a key and a value that is associated with that key
    private HashMap<String, Memory> mMemories = new HashMap<>();
    private MemoriesDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**** We set up our data ******/
        //Create and set up the database with this single line
        mDataSource = new MemoriesDataSource(this);

        //The loader is going to manage our data across the life cycle of the activity
        //The loader doesn't depends on the activity. If the activity gets destroy when we rotate the divise it doesn't mean that the loader is going to get destroy.
        //getLoaderManager() is a kind of fragment manager; it belongs to the activity; it will manage the loaders through the life cycle of the activity.
        //Here we initialize the loader and when everything is finished loading up, it calls deliver results and then the super class calls the onLoadFinished()
        //and we get back our cursor and then we turn that cursor into memories
        getLoaderManager().initLoader(0, null, this);
        /****************************/

        /**** We set up our map ******/
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        //This force to get the map first and call the data after it.
        mMap = mapFragment.getMap();
        //mapFragment.getMapAsync(this);
        /*****************************/

        //Ones our map is set up we can call
        onMapReady(mMap);

        //addGoogleAPIClient();
    }

    /* Implements this interface at the top OnMapReadyCallback
     * Alt + Ins > Override Methods and add this method.
     * This method is going to be called as soon the map is going to be loaded*/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Log.e(TAG, "onMapReady");

        //mMap = googleMap;
        //Show current location on the maps
        mMap.setMyLocationEnabled(true);
        //Add this to be able to get coordinates when you click google maps.
        //Implement interface OnMapClickListener and get coordinates in its method
        mMap.setOnMapClickListener(this);
        //We set our adapter and pass the layout inflater. The adapter is the one that creates the view for us
        mMap.setInfoWindowAdapter(new MarkerAdapter(getLayoutInflater(), mMemories));
        //Add this to allow marker dragging. Add interface OnMarkerDragListener
        mMap.setOnMarkerDragListener(this);
        //This line of code allows us to interact with the info window actions
        //To work with it we need to add interface OnInfoWindowClickListener. Methods interface implementation line 164
        mMap.setOnInfoWindowClickListener(this);

        //The loader is going to manage our data across the life cycle of the activity
        //getLoaderManager() is a kind of fragment manager; it belongs to the activity; it will manage the loaders through the life cycle of the activity.
        //Here we initialize the loader and when everything is finished loading up, it calls deliver results (It is in DbCursorLoader.java) and then the super class calls the onLoadFinished()
        //and we get back our cursor and then we turn that cursor into memories
        getLoaderManager().initLoader(0, null, this);

        /*//Get data from the background thread
        new AsyncTask<Void, Void, List<Memory>>() {

            //This thread is in background
            @Override
            protected List<Memory> doInBackground(Void... params) {
                return mDataSource.getAllMemories();
            }

            //This method gives us the results of the thread that runs in the background getting the data
            //This is in the main thread. You do here changes in the user interfaces
            @Override
            protected void onPostExecute(List<Memory> memories) {
                Log.d(TAG, "Got Results");
                onFetchedMemories(memories);
            }
        }.execute(); //If I need to pass any parameters I just put them here
*/
        //Log.d(TAG, "End of onMapReady");

        //When the map is ready get all our memories from the database
        //List<Memory> memories = mDataSource.getAllMemories();
        //Log.d(TAG, "Memories are" + memories);



    }

    private void onFetchedMemories(List<Memory> memories) {
        //Add a marker
        for(Memory memory: memories){
            addMarker(memory);
        }
    }

    //Implement method from interface OnMapClickListener
    //With this method we can get coordinates when we click the map
    @Override
    public void onMapClick(LatLng latLng) {

        //Log.d(TAG, "Latlng is" + latLng);

        Memory memory = new Memory();

        updateMemoryPosition(memory, latLng);

        //Show Dialog. You need to add this class MemoryDialogFragment
        //newInstance is a "factory method" that creates the object MemoryDialogFragment
        //You use this "factory method" because the constructor is going to be used by the fragment. You can't use it with fragmets
        MemoryDialogFragment.newInstance(memory).show(getFragmentManager(), MEMORY_DIALOG_TAG);

    }


    //Implement methods of interface MemoryDialogFragment.Listener
    /*************************************************************/
    @Override
    public void OnSaveClicked(Memory memory) {

        addMarker(memory);
        //Save values in the database
        mDataSource.createMemory(memory);

    }

    //Use this method to add a marker in the map
    private void addMarker(Memory memory) {

        Marker marker = mMap.addMarker(new MarkerOptions()
                //Allow marker to be dragable. Add mMap.setOnMarkerDragListener(this) in onMapReady
                .draggable(true)
                //We need the position so it knows where to add the marker
                .position(new LatLng(memory.latitude, memory.longitude)));


                /*.title(bestMatch.getAddressLine(maxLine - 1))
                .snippet(bestMatch.getAddressLine(maxLine)));*/

        //Put values into a hash map
        mMemories.put(marker.getId(), memory);
    }

    @Override
    public void OnCancelClicked(Memory memory) {

    }
    /*************************************************************/


    //Add methods of this interface LoaderCallbacks<Cursor>
    /*******************************************************/
    //We initialize the loader in line 83 and when everything is finished loading up, it calls deliver results and then the super class calls the onLoadFinished()
    //and we get back our cursor and then we turn that cursor into memories
    //The loader doesn't get destroy if the activity gets destroy

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //Log.e(TAG, "onCreateLoader");
        return new MemoriesLoader(this, mDataSource);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //Log.e(TAG, "onLoadFinished");
        onFetchedMemories(mDataSource.cursorToMemories(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /********************************************************/


    //Implement methods of interface MemoryDialogFragment.Listener
    /*************************************************************/
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Fetch our memory
        Memory memory = mMemories.get(marker.getId());
        //Udate marker position
        updateMemoryPosition(memory, marker.getPosition());
        //Update memory in database
        mDataSource.updateMemory(memory);
    }
    /*************************************************************/


    //Implement methods of inferface OnInfoWindowClickListener
    /*********************************************************/
    @Override
    public void onInfoWindowClick(final Marker marker) {
        //Log.d(TAG, "Clicked info windodw");

        final Memory memory = mMemories.get(marker.getId());
        String[] actions = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(memory.city + ", " + memory.country)
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //1 is the index of Delete
                        if (which == 1){
                            marker.remove();
                            //Delete marker from datasource
                            mDataSource.deleteMemory(memory);
                        }
                    }
                });
                /*.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/

                        builder.create().show();
    }
    /********************************************************/

    private void updateMemoryPosition(Memory memory, LatLng latLng) {

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

        memory.city = bestMatch.getAddressLine(maxLine -1);
        memory.country = bestMatch.getAddressLine(maxLine);
        memory.latitude = latLng.latitude;
        memory.longitude = latLng.longitude;
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
