package bitfontain.juangomez.com.traveltracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 8/12/2015.
 */
//This is going to be a data access object. It is going to save the information in a database
public class MemoriesDataSource {

    private DbHelper mDbHelper;
    private String[] allColumns = {
            DbHelper.COLUMN_ID, DbHelper.COLUMN_CITY,
            DbHelper.COLUMN_COUNTRY, DbHelper.COLUMN_LATITUDE,
            DbHelper.COLUMN_LONGITUDE, DbHelper.COLUMN_NOTES
    };

    //Create constructor
    public MemoriesDataSource(Context context){
        //Call the singleton
        mDbHelper = DbHelper.getInstance(context);
    }

    public  void createMemory(Memory memory){

        //values is like a row in the database
        ContentValues values = new ContentValues();
        //It has key values parameters
        values.put(DbHelper.COLUMN_NOTES, memory.notes);
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_LATITUDE, memory.latitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);

        //We get our database. We either make it readable or writable.
        //We are going to create a row in the database so we need to create a writable database.
       memory.id = mDbHelper.getWritableDatabase().insert(DbHelper.MEMORIES_TABLE, null, values);
    }

    public List<Memory> getAllMemories(){

        //Reading values from the database
        //A cursor represents a place in the database with some data. We use a cursor and move it forward to go through the data
        Cursor cursor = allMemoriesCursor();

        return cursorToMemories(cursor);

    }

    //Retrun the cursor
    public Cursor allMemoriesCursor(){

        //This is the query that returns the cursor
        return mDbHelper.getReadableDatabase().query(DbHelper.MEMORIES_TABLE, allColumns, null, null, null, null, null);
    }

    //Works with the cursor
    public List<Memory> cursorToMemories(Cursor cursor){

        List<Memory> memories = new ArrayList<>();

        cursor.moveToFirst();

        //While cursor has more data to go through
        while (!cursor.isAfterLast()){
            Memory memory = cursorToMemory(cursor);
            memories.add(memory);
            cursor.moveToNext();
        }

        //Be sure that you close the cursor when you are done with it
        //We delete this because our memoriesLoader and our DbCursorLoader is now in charge of handleing when
        //to closet it in the life cycle of the activity
        //cursor.close();

        return memories;
    }

    public void updateMemory(Memory memory){

        //values is like a row in the database
        ContentValues values = new ContentValues();
        //It has key values parameters
        values.put(DbHelper.COLUMN_NOTES, memory.notes);
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_LATITUDE, memory.latitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);

        //Arguments of the where
        String[] whereArgs = {String.valueOf(memory.id)};

        //The question mark is just a place holder, whatever we pass like an argument here, is what we need to put in the question mark. It makes the query a little more generic and safer
        //The two last parameters are the "WHERE-id = 123"
        mDbHelper.getWritableDatabase().update(
                mDbHelper.MEMORIES_TABLE,
                values,
                mDbHelper.COLUMN_ID + "=?",
                whereArgs
        );
    }

    public void deleteMemory(Memory memory){

        //Arguments of the where
        String[] whereArgs = {String.valueOf(memory.id)};

        //The question mark is just a place holder, whatever we pass like an argument here, is what we need to put in the question mark. It makes the query a little more generic and safer
        //The two last parameters are the "WHERE-id = 123"
        mDbHelper.getWritableDatabase().delete(
                mDbHelper.MEMORIES_TABLE,
                mDbHelper.COLUMN_ID + "=?",
                whereArgs
        );
    }

    private Memory cursorToMemory(Cursor cursor){

        //Index into the cursor
        Memory memory = new Memory();

        //The number represents the index of allColumns String array
        memory.id = cursor.getLong(0);
        memory.city = cursor.getString(1);
        memory.country = cursor.getString(2);
        memory.latitude = cursor.getDouble(3);
        memory.longitude = cursor.getDouble(4);
        memory.notes = cursor.getString(5);

        return memory;
    }
}
