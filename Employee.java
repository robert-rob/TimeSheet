package com.protkh.timesheetplus;

public class Employee {
    private int EmpID;
    private String EmpName;

    public Employee() {

    }

    public Employee(String EmpName) {
        this.EmpName = EmpName;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    @Override
    public String toString() {
        return EmpName;
    }
}
