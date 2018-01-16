package com.protkh.timesheetplus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimeFragment extends Fragment {

    private final static String in = "In";
    private final static String out = "Out";
    private SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
    private DatabaseHandler database;
    private DatabaseHandlerEmp databaseEmp;
    private List<Entry> entryList;
    private List<Employee> employeeList;
    private RecyclerView timeListView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_time, container, false);
        database = DatabaseHandler.getInstance(getActivity());
        databaseEmp = DatabaseHandlerEmp.getInstance(getActivity());
        mContext = getActivity();

        Calendar calendar = Calendar.getInstance();
        String currentDate = dayFormat.format(calendar.getTime());

        TextView currentDateView = (TextView) rootView.findViewById(R.id.todayText);
        currentDateView.setText(currentDate);

        TextView instruction = (TextView) rootView.findViewById(R.id.instruction);
        instruction.setVisibility(View.GONE);

        entryList = new ArrayList<Entry>();
        employeeList = databaseEmp.getAllEmployeess();

        List<Record> recordList = database.getRecordsByDate(currentDate);

        for (Employee item : employeeList) {
            Entry selectedRecord = database.getLastEntry(item);
            entryList.add(selectedRecord);
        }

        timeListView = (RecyclerView) rootView.findViewById(R.id.empListView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        timeListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new MyAdapterEntry(entryList);

        timeListView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        timeListView.setAdapter(adapter);

        timeListView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(mContext,
                timeListView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                signIn(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

/*		if(employeeList.isEmpty()){
            TextView instruction = (TextView) rootView.findViewById(R.id.instruction);
			instruction.setVisibility(View.VISIBLE);
		} else {

		}*/
        return rootView;
    }

    protected void signIn(final int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(
                mContext);

        alert.setTitle("Sign In");
        alert.setMessage("\nChoose one\n");
        alert.setPositiveButton("Time Out", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Entry selectedEntry = entryList.get(position);
                Record recordEntry = getRecordFromEntry(selectedEntry, out);
                database.addRecord(recordEntry);
                //Employee item = adapter.getItem(deletePosition);
                //empList.remove(item);
                showText(recordEntry.toString());
                updateList();
                dialog.dismiss();
                //adapter.notifyDataSetInvalidated();
            }
        });
        alert.setNegativeButton("Time In", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Entry selectedEntry = entryList.get(position);
                Record recordEntry = getRecordFromEntry(selectedEntry, in);
                database.addRecord(recordEntry);
                //Employee item = adapter.getItem(deletePosition);
                //empList.remove(item);
                showText(recordEntry.toString());
                updateList();
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void showMessage(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                mContext);

        alert.setTitle("Message");
        alert.setMessage("\n" + message + "\n");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alert.show();
    }

    private Record getRecordFromEntry(Entry entry, String status) {
        Calendar cal = Calendar.getInstance();
        String currentTime = timeFormat.format(cal.getTime());
        String currentDate = dayFormat.format(cal.getTime());
        Record record = new Record(entry.getEmpID(), entry.getEmpName(), status, currentDate, currentTime);
        return record;
    }

    private void showText(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void updateList() {
        employeeList = databaseEmp.getAllEmployeess();
        entryList = new ArrayList<Entry>();

        for (Employee item : employeeList) {
            Entry selectedRecord = database.getLastEntry(item);
            entryList.add(selectedRecord);
        }

        // specify an adapter (see also next example)
        adapter = new MyAdapterEntry(entryList);
        timeListView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        timeListView.setAdapter(adapter);
    }

}
