package bitfontain.juangomez.com.traveltracker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by Juan on 8/14/2015.
 */
//AsyncTaskLoader is going to give us back a Cursor
public abstract class DbCursorLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;

    //Create constructor
    public DbCursorLoader(Context context){
        //Super explanation http://beginnersbook.com/2014/07/super-keyword-in-java-with-example/
        /*first the constructor of parent class gets invoked and then the constructor of child class.
        It happens because compiler itself adds super()[it invokes parent class constructor] to the constructor of child class.*/
        super(context);
    }

    //Abstract method. We don't need to define how the abstract method is going to work. We are going to let the concrete
    // implementation of the subclasses decide that
    protected abstract Cursor loadCursor();

    //Ctrl + I (Implement methods)
    @Override
    public Cursor loadInBackground() {

        Cursor cursor = loadCursor();
        if (cursor != null){
            //Calling getCount() makes sure that when the cursor is getting back from the main thread the data is there.
            cursor.getCount();
        }

        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;

        //If the loader has started get some data back and then deliver that data
        if (isStarted()) {
            //This supper works with the methods onLoadFinished and onLoaderReset of this interface LoaderCallbacks<Cursor> in the main activity
            super.deliverResult(data);
        }

        //If we have a cursor but it is different from the current cursor
        if (oldCursor != null && oldCursor != data){
            onReleaseResources(oldCursor);
        }

    }

    @Override
    protected void onStartLoading() {

        if (mCursor != null){
            deliverResult(mCursor);
        }

        if (takeContentChanged() || mCursor == null){
            //Reload data in the background
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);

        if (data != null){
            onReleaseResources(data);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mCursor != null){
            onReleaseResources(mCursor);
        }

        mCursor = null;
    }

    //Cleanning up cursor
    private void onReleaseResources(Cursor cursor){

        if (!cursor.isClosed()){
            cursor.close();
        }


    }
}
