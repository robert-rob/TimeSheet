package com.protkh.timesheetplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandlerEmp extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "empManager";

    private static final String TABLE_EMPLOYEES = "employees";

    private static final String KEY_EMPID = "empid";
    private static final String KEY_NAME = "empname";

    private static DatabaseHandlerEmp singleton;

    public DatabaseHandlerEmp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandlerEmp getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandlerEmp(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES + "("
                + KEY_EMPID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);

        onCreate(db);
    }

    public void addEmployee(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);

        db.insert(TABLE_EMPLOYEES, null, values);
        db.close();
    }

    public List<Employee> getAllEmployeess() {
        List<Employee> employeeList = new ArrayList<Employee>();
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEES + " ORDER BY " + KEY_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setEmpID(Integer.parseInt(cursor.getString(0)));
                employee.setEmpName(cursor.getString(1));

                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
        return employeeList;
    }

    public int getEmployeesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EMPLOYEES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public void deleteEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEES, KEY_EMPID + " = ?",
                new String[]{String.valueOf(employee.getEmpID())});
        db.close();
    }
}    
