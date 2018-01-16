package com.protkh.timesheetplus;

public class Entry {

    private int ID;
    private int EmpID;
    private String EmpName;
    private String Status;
    private String Date;
    private String Time;

    public Entry() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int EmpID) {
        this.EmpID = EmpID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public boolean checkValidEntry() {
        if (ID == -1 && Status.equalsIgnoreCase("") && Date.equalsIgnoreCase("") && Time.equalsIgnoreCase("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        if (checkValidEntry()) {
            return EmpName + "\nLast Record:\n" + Date + " - " + Status + " - " + Time;
        } else {
            return EmpName + "\nHas no record";
        }
    }

}
