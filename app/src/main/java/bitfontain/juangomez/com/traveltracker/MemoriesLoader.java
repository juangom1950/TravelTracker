package bitfontain.juangomez.com.traveltracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Juan on 8/14/2015.
 */
public class MemoriesLoader extends DbCursorLoader {

    private MemoriesDataSource mDataSource;

    public MemoriesLoader(Context context, MemoriesDataSource memoriesDataSource){
        super(context);
        mDataSource = memoriesDataSource;
    }

    //The database always return a cursor lets work with that
    @Override
    protected Cursor loadCursor() {
        return mDataSource.allMemoriesCursor();
    }
}
