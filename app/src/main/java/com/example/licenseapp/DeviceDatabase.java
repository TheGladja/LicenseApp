package com.example.licenseapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeviceDatabase extends SQLiteOpenHelper {
    private static final String DEVICES_TABLE_NAME = "DEVICES_TABLE";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_BATTERY_CAPACITY = "BATTERY_CAPACITY";
    private static final String COLUMN_PRODUCER = "PRODUCER";
    private static final String COLUMN_MODEL = "MODEL";
    private static final String COLUMN_IMAGE_URL = "IMAGE_URL";
    private static final String COLUMN_SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String COLUMN_LONG_DESCRIPTION = "LONG_DESCRIPTION";
    private static final String COLUMN_IS_EXPANDED = "IS_EXPANDED";

    public DeviceDatabase(@Nullable Context context) {
        super(context, "devices_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "CREATE TABLE " + DEVICES_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_BATTERY_CAPACITY + " INT, " + COLUMN_PRODUCER + " TEXT, " + COLUMN_MODEL + " TEXT, " + COLUMN_IMAGE_URL + " TEXT, " + COLUMN_SHORT_DESCRIPTION + " TEXT, " + COLUMN_LONG_DESCRIPTION + " TEXT)";
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addDevice(Device device){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_BATTERY_CAPACITY, device.getBattery());
        contentValues.put(COLUMN_PRODUCER, device.getProducer());
        contentValues.put(COLUMN_MODEL, device.getModel());
        contentValues.put(COLUMN_IMAGE_URL, device.getImgUrl());
        contentValues.put(COLUMN_SHORT_DESCRIPTION, device.getShortDesc());
        contentValues.put(COLUMN_LONG_DESCRIPTION, device.getLongDesc());
        contentValues.put(COLUMN_IS_EXPANDED, device.isExpanded());

        long insert = database.insert(DEVICES_TABLE_NAME, null, contentValues);

        if(insert == -1){
            return false;
        }
        return true;
    }

    public boolean deleteAllDevices(){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryDeleteAllDevices = "DELETE FROM " + DEVICES_TABLE_NAME;

        Cursor cursor = database.rawQuery(queryDeleteAllDevices, null);

        if(cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    public boolean deleteDevice(Device device) {
        SQLiteDatabase database = this.getWritableDatabase();

        // Define the WHERE clause for deletion
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(device.getId()) };

        // Perform the deletion
        int deletedRows = database.delete(DEVICES_TABLE_NAME, selection, selectionArgs);

        // Check the number of rows affected by the deletion
        return deletedRows > 0;
    }


    public List<Device> getAllDevices(){
        List<Device> returnList = new ArrayList<>();

        //get data from the database
        String querySelectAllDevices = "SELECT * FROM " + DEVICES_TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(querySelectAllDevices, null);

        if(cursor.moveToFirst()){
            //loop through the cursor (result set) and create new graph set objects. Put them into the return lists
            do{
                int deviceId = cursor.getInt(0);
                int deviceBatteryCapacity = cursor.getInt(1);
                String deviceProducer = cursor.getString(2);
                String deviceModel = cursor.getString(3);
                String deviceImageUrl = cursor.getString(4);
                String deviceShortDesc = cursor.getString(5);
                String deviceLongDesc = cursor.getString(6);

                Device newDevice = new Device(deviceId, deviceBatteryCapacity, deviceProducer, deviceModel, deviceImageUrl, deviceShortDesc, deviceLongDesc);
                returnList.add(newDevice);
            }while(cursor.moveToNext());
        }else{
            //failure. do not add anything to the list
        }

        cursor.close();
        database.close();
        return returnList;
    }
}
