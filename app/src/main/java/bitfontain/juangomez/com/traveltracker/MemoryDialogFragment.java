package bitfontain.juangomez.com.traveltracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Juan on 8/11/2015.
 */
public class MemoryDialogFragment extends DialogFragment {

    private static final String TAG = "MemoryDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Add alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Memory")
                .setMessage("You want to create a new memory?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Clicked ok");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Clicked cancel");
                    }
                });

        return builder.create();
    }
}
