package com.example.app;

public class Data {

    private final String department;
    private final String topic;
    private final String message;
    private final String country;
    private final String city;



    public Data(String department, String topic,
                String message, String country,
                String city) {
        this.department = department;
        this.topic = topic;
        this.message = message;
        this.country = country;
        this.city = city;
    }


    public String getDepartment() {
        return department;
    }

    public String getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }


}
