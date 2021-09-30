package com.implodingduck.kafkasample.models;

public class MyEvent {
    private String message;

    public MyEvent(){

    }

    public MyEvent(String message){
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
