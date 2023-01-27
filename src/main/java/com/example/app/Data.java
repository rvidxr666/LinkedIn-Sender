package com.example.app;

public class Data {

    private final String department;
    private final String message;


    public Data(String department, String message) {
        this.department = department;
        this.message = message;
    }


    public String getDepartment() {
        return department;
    }

    public String getMessage() {
        return message;
    }


}
