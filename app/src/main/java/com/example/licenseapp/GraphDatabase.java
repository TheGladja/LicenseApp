package com.example.licenseapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GraphDatabase extends SQLiteOpenHelper {
    private static final String GRAPH_TABLE_NAME = "GRAPH_TABLE";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_ENERGY = "ENERGY";

    public GraphDatabase(@Nullable Context context) {
        super(context, "graph_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "CREATE TABLE " + GRAPH_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT, " + COLUMN_ENERGY + " INT)";
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addGraphSet(GraphModel graphModel){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_DATE, graphModel.getDate());
        contentValues.put(COLUMN_ENERGY, graphModel.getEnergy());

        long insert = database.insert(GRAPH_TABLE_NAME, null, contentValues);

        if(insert == -1){
            return false;
        }
        return true;
    }

    public boolean deleteAllGraphSets(){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryDeleteAllGraphSets = "DELETE FROM " + GRAPH_TABLE_NAME;

        Cursor cursor = database.rawQuery(queryDeleteAllGraphSets, null);

        if(cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    public boolean deleteGraphSet(GraphModel graphModel){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryDeleteGraphSet = "DELETE FROM " + GRAPH_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + graphModel.getId();

        Cursor cursor = database.rawQuery(queryDeleteGraphSet, null);

        if(cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    public List<GraphModel> getAllGraphSets(){
        List<GraphModel> returnList = new ArrayList<>();

        //get data from the database
        String querySelectAllGraphSets = "SELECT * FROM " + GRAPH_TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(querySelectAllGraphSets, null);

        if(cursor.moveToFirst()){
            //loop through the cursor (result set) and create new graph set objects. Put them into the return lists
            do{
                int graphSetId = cursor.getInt(0);
                String graphSetDate = cursor.getString(1);
                int graphSetEnergy = cursor.getInt(2);

                GraphModel newGraphModel = new GraphModel(graphSetId, graphSetDate, graphSetEnergy);
                returnList.add(newGraphModel);
            }while(cursor.moveToNext());
        }else{
            //failure. do not add anything to the list
        }

        cursor.close();
        database.close();
        return returnList;
    }
}
