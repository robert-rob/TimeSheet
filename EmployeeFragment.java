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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class EmployeeFragment extends Fragment {

    private DatabaseHandler database;
    private DatabaseHandlerEmp databaseEmp;
    private Context mContext;

    private List<Employee> empList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_employees, container, false);
        mContext = getActivity();
        databaseEmp = DatabaseHandlerEmp.getInstance(mContext);
        database = DatabaseHandler.getInstance(mContext);

        empList = databaseEmp.getAllEmployeess();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.empListView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(empList);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(mContext,
                mRecyclerView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                removeItemFromList(position);
            }
        }));

        ImageButton addButton = (ImageButton) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("Add Employee");
                alert.setMessage("Enter Employee's name");

                final EditText input = new EditText(mContext);
                input.setSingleLine();
                alert.setView(input);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getEditableText().toString();
                        boolean duplicate = false;

                        for (Employee employee : empList) {
                            if (employee.getEmpName().equals(value)) {
                                duplicate = true;
                                break;
                            }
                        }

                        if (duplicate) {
                            Toast.makeText(mContext, "Error: " + value + " already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseEmp.addEmployee(value);
                            empList = databaseEmp.getAllEmployeess();
                            // specify an adapter (see also next example)
                            mAdapter = new MyAdapter(empList);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });

        return rootView;
    }

    protected void removeItemFromList(int position) {
        final int deletePosition = position;
        Employee deleteEmployee = empList.get(deletePosition);

        AlertDialog.Builder alert = new AlertDialog.Builder(
                mContext);

        alert.setTitle("Delete");
        alert.setMessage("\nDo you want delete " + deleteEmployee.getEmpName() + "?\n");
        alert.setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Employee deleteEmployee = empList.get(deletePosition);
                databaseEmp.deleteEmployee(deleteEmployee);
                database.deleteRecordByEmployee(deleteEmployee);
                //Employee item = adapter.getItem(deletePosition);
                //empList.remove(item);
                empList.remove(deletePosition);
                mAdapter.notifyDataSetChanged();
                //adapter.notifyDataSetInvalidated();
            }
        });
        alert.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();
    }

}
