package com.protkh.timesheetplus;

public class Record {
    private int ID;
    private int EmpID;
    private String EmpName;
    private String Status;
    private String Date;
    private String Time;


    public Record() {

    }

    public Record(int ID, int EmpID, String EmpName, String Status, String Date, String Time) {
        this.ID = ID;
        this.EmpID = EmpID;
        this.EmpName = EmpName;
        this.Status = Status;
        this.Date = Date;
        this.Time = Time;
    }

    public Record(int EmpID, String EmpName, String Status, String Date, String Time) {
        this.EmpID = EmpID;
        this.EmpName = EmpName;
        this.Status = Status;
        this.Date = Date;
        this.Time = Time;
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

    @Override
    public String toString() {
        return EmpName + " - " + Status + " - " + Time;
    }
}
