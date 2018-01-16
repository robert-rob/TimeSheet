package com.protkh.timesheetplus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {

    static View rootView = null;
    public static Context context;
    private static String date = "";
    private static List<Record> recordList = new ArrayList<Record>();
    private static Button dateView;
    private static DatabaseHandler database;
    private static RecyclerView recordView;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    private static int mPosition = 0;
    private SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

    //Update list of records from Database
    private static void updateList() {
        recordList = database.getRecordsByDate(date);
        adapter = new MyAdapterRecord(recordList);
        recordView.setAdapter(adapter);
    }

    //Create History Page View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        database = DatabaseHandler.getInstance(getActivity());

        context = getActivity();
        Calendar c = Calendar.getInstance();
        date = dayFormat.format(c.getTime());
        dateView = (Button) rootView.findViewById(R.id.date);
        dateView.setText(date);

        ImageButton clear = (ImageButton) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Clear Day's History");
                alert.setMessage("\nAre you sure?\n");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        database.deleteRecordByDate(date);
                        updateList();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();

            }
        });

        //Set Refresh Button Function
        ImageButton refresh = (ImageButton) rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
                showText("History Refreshed");
            }
        });

        //Set Date Button Function
        dateView.setOnClickListener(new View.OnClickListener() {
            private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int month, int day) {
                    String selectedDate = setDate(month, day, year);
                    date = selectedDate;
                    dateView.setText(selectedDate);
                    updateList();
                }

                private String setDate(int month, int day, int year) {
                    String date, month_string = "";
                    month++;
                    switch (month) {
                        case 1:
                            month_string += "January";
                            break;
                        case 2:
                            month_string += "Febrary";
                            break;
                        case 3:
                            month_string += "March";
                            break;
                        case 4:
                            month_string += "April";
                            break;
                        case 5:
                            month_string += "May";
                            break;
                        case 6:
                            month_string += "June";
                            break;
                        case 7:
                            month_string += "July";
                            break;
                        case 8:
                            month_string += "August";
                            break;
                        case 9:
                            month_string += "September";
                            break;
                        case 10:
                            month_string += "October";
                            break;
                        case 11:
                            month_string += "November";
                            break;
                        case 12:
                            month_string += "December";
                            break;
                        default:
                            month_string += "Jan";
                            break;
                    }
                    String leadZero = " ";
                    if (0 < day && day < 10) {
                        leadZero = " 0";
                    } else {
                        leadZero = " ";
                    }
                    date = month_string + leadZero + day + ", " + year;
                    return date;
                }

            };

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                        year, month, day);
                dialog.setTitle("Set Date");
                dialog.show();
            }
        });

        recordList = database.getRecordsByDate(date);

        recordView = (RecyclerView) rootView.findViewById(R.id.recordList);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        recordView.setLayoutManager(mLayoutManager);

        // specify an adapter 
        adapter = new MyAdapterRecord(recordList);

        recordView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        recordView.setAdapter(adapter);

        //Allows User to edit the date on a record
        recordView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(context,
                recordView, new MainActivity.ClickListener() {

            TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    List<Record> oldRecordList = database.getRecordsByDate(date);
                    Record currentRecord = recordList.get(mPosition);
                    Record updatedRecord = new Record(currentRecord.getID(),
                            currentRecord.getEmpID(), currentRecord.getEmpName(),
                            currentRecord.getStatus(), currentRecord.getDate(),
                            formatTime(hourOfDay, minute));
                    database.updateRecord(updatedRecord);
                    List<Record> newRecordList = database.getRecordsByDate(date);
                    showText(updatedRecord.toString());
                    updateList();
                }
            };

            public String formatTime(int hour, int minute) {
                boolean beforeMidday = hour < 13;
                String period = beforeMidday ? " AM" : " PM";
                int newHour = beforeMidday ? hour : hour - 12;
                String newMinute = minute > 9 ? Integer.toString(minute) :
                        "0" + Integer.toString(minute);
                return newHour + ":" + newMinute + period;
            }

            @Override
            public void onClick(View view, int position) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                mPosition = position;

                TimePickerDialog dialog = new TimePickerDialog(context, timePickerListener,
                        mHour, mMinute, false);
                dialog.setTitle("Update Time");
                dialog.show();
            }

            //Allows User to delete a record
            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Delete Record");
                alert.setMessage("\nAre you sure?\n");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Record currentRecord = recordList.get(position);
                        database.deleteRecord(currentRecord);
                        updateList();
                        showText("Record deleted");
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        }));

        return rootView;
    }

    private void showText(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
