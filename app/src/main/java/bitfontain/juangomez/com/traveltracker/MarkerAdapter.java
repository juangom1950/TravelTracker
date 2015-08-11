package bitfontain.juangomez.com.traveltracker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by Juan on 8/11/2015.
 */
//The adapter creates the view for us
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

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

        Memory memory = mMemories.get(marker.getId());

        //We populate the view with any data that we get from the marker
        TextView titleView  =  (TextView)mView.findViewById(R.id.title);
        titleView.setText(memory.city);
        //titleView.setText(marker.getTitle());

        TextView snipetView  =  (TextView)mView.findViewById(R.id.snipet);
        snipetView.setText(memory.country);

        TextView notesView  =  (TextView)mView.findViewById(R.id.notes);
        notesView.setText(memory.notes);

        return mView;
    }

    //It will replace the hole pop up of the marker
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /***************************************************************/
}
