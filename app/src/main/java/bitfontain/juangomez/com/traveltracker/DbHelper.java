package bitfontain.juangomez.com.traveltracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan on 8/12/2015.
 */
//We convert this class into a singleton to make it be visible to the whole project
//This is in charge of setting up and creating a dapabase
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "traveltracker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String MEMORIES_TABLE = "memories";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_ID = "_id";

    //Add this to convert it to a singleton
    //**********************************/
    private static DbHelper singleton = null;

    //Adding the "synchronized" key word is a way to make sure that only one sigleton object is actually created.
    //what it does is that even though things are happening in different threads at the same time, synchronize makes sure that only one thread can call this method at the time
    public synchronized static DbHelper getInstance(Context context){
        if (singleton == null){
            //The getApplicationContext() is a context for the whole application and that is not related to any single activity. Use this context when using singleton
            singleton = new DbHelper(context.getApplicationContext());
        }
        return singleton;
    }
    /***********************************/

    //We need to implement this two methods plus the constructor to make this DbHelper works
    //If you initialize this DbHelper class it is going to create the raveltracker.db file which will be an SQLiteDatabase file
    //We make this constructor private so that you only can access the DbHelper object through the singleton
    /*******************************************************************************************************/
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //If there is not any database file we are going to handle the creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create the database
        //We put all our lowercases like constants because we are going to use them a lot.
        db.execSQL("CREATE TABLE " + MEMORIES_TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LATITUDE + " DOUBLE, "
                + COLUMN_LONGITUDE + " DOUBLE, "
                + COLUMN_CITY + " TEXT, "
                + COLUMN_COUNTRY + " TEXT, "
                + COLUMN_NOTES + " TEXT"
                + ")");
    }

    //If the user has an old version of the app install, the user is going to have an old version of the database
    //If the user has a new version of the app install, the user is going to have a new version of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //If we are going to upgrade our table we need to clear first what whatever we have and then we create our table
        //Use this in development not in production becouse it is going to delete all the users information
        db.execSQL("DROP TABLE IF EXISTS " + MEMORIES_TABLE);
        onCreate(db);
    }
    /******************************************************************************************************/
}
