package com.nt.rsbcontactstestappmb.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + "("
                + Params.KEY_ID + " INTEGER PRIMARY KEY,"
                + Params.KEY_NAME + " TEXT, "
                + Params.KEY_PHONE + " TEXT,"
                + Params.KEY_PHOTO + " BLOB,"
                + Params.KEY_FAV + " INTEGER,"
                + Params.KEY_DELETED + " INTEGER,"
                + "UNIQUE (" + Params.KEY_PHONE + ") ON CONFLICT ABORT )";
        Log.d("db", "Query being run is : " + create);
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_NAME, contact.getName());
        values.put(Params.KEY_PHONE, contact.getPhone());
        values.put(Params.KEY_PHOTO, contact.getByteBuffer());
        values.put(Params.KEY_FAV, "" + contact.getFav());
        values.put(Params.KEY_DELETED, "" + contact.getFav());

        db.insertWithOnConflict (Params.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE );
        Log.d("db", "Successfully inserted");
        db.close();
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate the query to read from the database
        String select = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_DELETED +"= 0" + " ORDER BY " + Params.KEY_NAME + " ASC";
        Cursor cursor = db.rawQuery(select, null);

        //Loop through now
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setByteBuffer(cursor.getBlob(3));
                contact.setFav(Integer.parseInt(cursor.getString(4)));
                contact.setDeleted(Integer.parseInt(cursor.getString(5)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_NAME, contact.getName());
        values.put(Params.KEY_PHONE, contact.getPhone());
        values.put(Params.KEY_PHOTO, contact.getByteBuffer());
        values.put(Params.KEY_FAV, contact.getFav());
        values.put(Params.KEY_DELETED, contact.getDeleted());

        //Lets update now
        Log.v("contact_id", ""+contact.getId());
        int i = db.update(Params.TABLE_NAME, values, Params.KEY_ID + "='" + contact.getId() + "'", null);
        db.close();
        return i;
    }

//    public int updateContactPhoto(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(Params.KEY_NAME, contact.getName());
//        values.put(Params.KEY_PHONE, contact.getPhone());
//        values.put(Params.KEY_PHOTO, contact.getByteBuffer());
//        values.put(Params.KEY_FAV, contact.getFav());
//        values.put(Params.KEY_DELETED, contact.getDeleted());
//
//        //Lets update now
//        Log.v("contact_id", ""+contact.getId());
//        int i = db.update(Params.TABLE_NAME, values, Params.KEY_ID + "='" + contact.getId() + "'", null);
//        db.close();
//        return i;
//    }

    public List<Contact> getFavContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate the query to read from the database
        String select = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_FAV +"= 1" + " ORDER BY " + Params.KEY_NAME + " ASC";
        Cursor cursor = db.rawQuery(select, null);

        //Loop through now
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setByteBuffer(cursor.getBlob(3));
                contact.setFav(Integer.parseInt(cursor.getString(4)));
                contact.setDeleted(Integer.parseInt(cursor.getString(5)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    public List<Contact> getDeletedContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate the query to read from the database
        String select = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_DELETED +"= 1" + " ORDER BY " + Params.KEY_NAME + " ASC";
        Cursor cursor = db.rawQuery(select, null);

        //Loop through now
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setByteBuffer(cursor.getBlob(3));
                contact.setFav(Integer.parseInt(cursor.getString(4)));
                contact.setDeleted(Integer.parseInt(cursor.getString(5)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

}
