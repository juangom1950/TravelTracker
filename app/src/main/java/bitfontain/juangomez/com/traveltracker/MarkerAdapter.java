package bitfontain.juangomez.com.traveltracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by Juan on 8/11/2015.
 */
//The adapter creates the view for us
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "MarkerAdapter";

    private LayoutInflater mLayoutInflater;
    private View mView;
    private HashMap<String, Memory> mMemories;

    //Create constructor
    MarkerAdapter(LayoutInflater layoutInflater, HashMap<String, Memory> memories){

        mLayoutInflater = layoutInflater;
        mMemories = memories;
    }

    /***** This two methods comes from this interface InfoWindowAdapter **************/

    //It is going to keep the square pop up but it is going to change the content
    @Override
    public View getInfoContents(Marker marker) {

        //It makes sure that we got that view and that we return that view
        if (mView == null){
            mView = mLayoutInflater.inflate(R.layout.marker, null);
        }

        //Get memory class from the hash map using the id of the marker
        Memory memory = mMemories.get(marker.getId());

        //We populate the view with any data that we get from the memory class
        TextView titleView  =  (TextView)mView.findViewById(R.id.title);
        titleView.setText(memory.city);
        //titleView.setText(marker.getTitle());

        TextView snipetView  =  (TextView)mView.findViewById(R.id.snipet);
        snipetView.setText(memory.country);

        TextView notesView  =  (TextView)mView.findViewById(R.id.notes);
        notesView.setText(memory.notes);

        //It doesn't word here because this windows generates an image info not a view
        /*Button removeButton = (Button)mView.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked remove button");
            }
        });*/

        return mView;
    }

    //It will replace the hole pop up of the marker
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /***************************************************************/
}
