package com.test.kosta.goldrone_userapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.test.kosta.goldrone_userapplication.DB.DataBaseContract.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kosta on 2016-07-14.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "drone.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold stations.  A location consists of the string supplied in the
        // station id, the station name, and the latitude and longitude
        final String SQL_CREATE_DRONE_TABLE = "CREATE TABLE " + DroneEntry.TABLE_NAME + " (" +
                DroneEntry.NODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DroneEntry.COLUMN_DRONEID + " TEXT NOT NULL , " +
                DroneEntry.COLUMN_LAT + " TEXT NOT NULL, " +
                DroneEntry.COLUMN_LON + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry.COLUMN_LAT + " TEXT NOT NULL, " +
                UserEntry.COLUMN_LON + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_DRONE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);

   }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DroneEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public List<DroneGPS> getDroneGpsList(){

        List<DroneGPS> droneList = new ArrayList<DroneGPS>();

        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM drone";

        Cursor cursor = db.rawQuery(countQuery,null);

        if (cursor.moveToFirst()) {
            do {

                DroneGPS drone = new DroneGPS();
                drone.setNode_id(Integer.parseInt(cursor.getString(0)));
                drone.setId(Integer.parseInt(cursor.getString(1)));
                drone.setLat(Double.parseDouble(cursor.getString(2)));
                drone.setLon(Double.parseDouble(cursor.getString(3)));
                droneList.add(drone);

            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        return droneList;
    }

    public UserGPS getUserGPS(){

        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM user";

        UserGPS usr = null;
        Cursor cursor = db.rawQuery(countQuery,null);

        if (cursor.moveToFirst()) {
            do {

                usr = new UserGPS();
                usr.setLat(Double.parseDouble(cursor.getString(0)));
                usr.setLon(Double.parseDouble(cursor.getString(1)));

            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        return usr;
    }

/*    public DroneGPS getDroneGPS(){

        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM drone where nodeid = 1;";

        DroneGPS drone = null;
        Cursor cursor = db.rawQuery(countQuery,null);

        if (cursor.moveToFirst()) {
            do {
                drone = new DroneGPS();

                drone.setNode_id(Integer.parseInt(cursor.getString(0)));
                drone.setId(Integer.parseInt(cursor.getString(1)));
                drone.setLat(Double.parseDouble(cursor.getString(2)));
                drone.setLon(Double.parseDouble(cursor.getString(3)));

            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        return drone;
    }*/

    public void addDroneGPS(DroneGPS drone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DroneEntry.COLUMN_DRONEID, drone.id);
        values.put(DroneEntry.COLUMN_LAT, drone.lat);
        values.put(DroneEntry.COLUMN_LON, drone.lon);

        Log.e("DBHANDLER", "" + drone.id);

        db.insert(DroneEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void addUserGPS(UserGPS usr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserEntry.COLUMN_LAT, usr.lat);
        values.put(UserEntry.COLUMN_LON, usr.lon);

        Log.e("DBHANDLER", "usr");

        db.insert(UserEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteAll() {

        //
        Log.e("deleteAll","delete!");
        //

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DroneEntry.TABLE_NAME,null,null);
        db.delete(UserEntry.TABLE_NAME,null,null);

        db.execSQL("DROP TABLE " + DroneEntry.TABLE_NAME) ;
        db.execSQL("DROP TABLE " + UserEntry.TABLE_NAME) ;
        onUpgrade(db,2,2);
    }

/*    public void deleteUserAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserEntry.TABLE_NAME,null,null);
        db.close();
    }*/

}