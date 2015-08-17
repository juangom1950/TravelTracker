package bitfontain.juangomez.com.traveltracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Juan on 8/11/2015.
 */
public class MemoryDialogFragment extends DialogFragment {

    private static final String TAG = "MemoryDialog";
    private static final String MEMORY_KEY = "Memory";

    private Memory mMemory;
    private Listener mListener;
    private View mView;

    //Check if the activity conforms with this interface that was created at the bottom and implemented in MainActivity
    //Here We grab our listener interface wich is created in this class and implemented in MainActivity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Do this to catch any exception cause in the implementation of the interface.
        try{
            //Lets grab our listener
            mListener = (Listener)getActivity();
        }catch (ClassCastException e){
            throw new IllegalStateException("Activity does not implement contract");
        }
    }

    //We need to unpack the memory from the arguments
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null){
            mMemory = (Memory)args.getSerializable(MEMORY_KEY);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //We need a layout inflater to create a custom dialog. First you need to create layout memory_dialog_fragment
        //This gives us back a view
        mView = getActivity().getLayoutInflater().inflate(R.layout.memory_dialog_fragment, null);

        TextView cityView = (TextView)mView.findViewById(R.id.city);
        cityView.setText(mMemory.city);

        TextView countryView = (TextView)mView.findViewById(R.id.country);
        countryView.setText(mMemory.country);

        //Add alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setTitle(getString(R.string.memory_dialog_title))
                //.setMessage(mMemory.city + " " + mMemory.country)
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Log.d(TAG, "Clicked ok");
                        EditText notesView = (EditText)mView.findViewById(R.id.notes);
                        mMemory.notes = notesView.getText().toString();

                        //Here you use the interface that was created here and implemented in MainActivity. It is grab in line 35
                        mListener.OnSaveClicked(mMemory);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.d(TAG, "Clicked cancel");
                        mListener.OnCancelClicked(mMemory);
                    }
                });

        return builder.create();
    }

    //Create a factory method: This is a static method that creates an object
    //You can't use the constructor with fragment because the fragment requires a constructor without arguments
    //When working with fragments always use this method instead of fragments
    public static MemoryDialogFragment newInstance(Memory memory){

        MemoryDialogFragment fragment = new MemoryDialogFragment();

        //If fragment gets destroy we need android to be able to rebuild it
        //Arguments is a bundle that stays with the fragment and allows us to maintain its state.
        //We unpack the memory values of Bundle args in onCreate method
        Bundle args = new Bundle();
        args.putSerializable(MEMORY_KEY, memory);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Add this public interface that is going to listen for clicks
    public interface Listener{
        public void OnSaveClicked(Memory memory);
        public void OnCancelClicked(Memory memory);
    }
}
