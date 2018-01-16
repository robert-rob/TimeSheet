package com.protkh.timesheetplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "recordManager";

    private static final String TABLE_RECORDS = "records";

    private static final String KEY_ID = "id";
    private static final String KEY_EMPID = "empid";
    private static final String KEY_NAME = "empname";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    private static DatabaseHandler singleton;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMPID + " INTEGER,"
                + KEY_NAME + " TEXT," + KEY_STATUS + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);

        onCreate(db);
    }

    public void addRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMPID, record.getEmpID());
        values.put(KEY_NAME, record.getEmpName());
        values.put(KEY_STATUS, record.getStatus());
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_TIME, record.getTime());

        db.insert(TABLE_RECORDS, null, values);
        db.close();
    }

    public Record getRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECORDS, new String[]{KEY_ID, KEY_EMPID,
                        KEY_NAME, KEY_STATUS, KEY_DATE, KEY_TIME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Record record = new Record(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5));

        return record;
    }

    public List<Record> getRecordsByDate(String Date) {
        List<Record> recordList = new ArrayList<Record>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_RECORDS, new String[]{KEY_ID, KEY_EMPID,
                        KEY_NAME, KEY_STATUS, KEY_DATE, KEY_TIME}, KEY_DATE + "=?",
                new String[]{Date}, null, null, KEY_NAME + ", " + KEY_ID, null);

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setID(Integer.parseInt(cursor.getString(0)));
                record.setEmpID(Integer.parseInt(cursor.getString(1)));
                record.setEmpName(cursor.getString(2));
                record.setStatus(cursor.getString(3));
                record.setDate(cursor.getString(4));
                record.setTime(cursor.getString(5));
                // Adding record to list
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        return recordList;
    }

    public Entry getLastEntry(Employee employee) {
        Entry entry = new Entry();

        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS
                + " WHERE " + KEY_EMPID + " = " + employee.getEmpID()
                + " ORDER BY " + KEY_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast()) {
            entry.setID(Integer.parseInt(cursor.getString(0)));
            entry.setEmpID(Integer.parseInt(cursor.getString(1)));
            entry.setEmpName(cursor.getString(2));
            entry.setStatus(cursor.getString(3));
            entry.setDate(cursor.getString(4));
            entry.setTime(cursor.getString(5));
        } else {
            entry.setID(-1);
            entry.setEmpID(employee.getEmpID());
            entry.setEmpName(employee.getEmpName());
            entry.setStatus("");
            entry.setDate("");
            entry.setTime("");
        }

        return entry;
    }

    public List<Record> getAllRecords() {
        List<Record> recordList = new ArrayList<Record>();

        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setID(Integer.parseInt(cursor.getString(0)));
                record.setEmpID(Integer.parseInt(cursor.getString(1)));
                record.setEmpName(cursor.getString(2));
                record.setStatus(cursor.getString(3));
                record.setDate(cursor.getString(4));
                record.setTime(cursor.getString(5));

                recordList.add(record);
            } while (cursor.moveToNext());
        }
        return recordList;
    }

    public int getRecordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RECORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMPID, record.getEmpID());
        values.put(KEY_NAME, record.getEmpName());
        values.put(KEY_STATUS, record.getStatus());
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_TIME, record.getTime());

        return db.update(TABLE_RECORDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(record.getID())});
    }

    public void deleteRecordByDate(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, KEY_DATE + " = ?",
                new String[]{Date});
        db.close();
    }

    public void deleteRecordByEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, KEY_EMPID + " = ?",
                new String[]{String.valueOf(employee.getEmpID())});
        db.close();
    }

    public void deleteRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, KEY_ID + " = ?",
                new String[]{String.valueOf(record.getID())});
        db.close();
    }
}