package com.example.cs160_sp18.prog3;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// custom class made for storing a message. you can update this class

// this class is to know how long ago the message was posted
public class Comment {

    public String text;
    public String username;
    public Date date;

    Comment(String text, String username, Date date) {
        this.text = text;
        this.username = username;
        this.date = date;
    }

    // returns a string indicating how long ago this post was made
    protected String elapsedTimeString() {

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//        Log.d("Showing from ", "comment class: \n" + date);

//        Date date = new Date();
        return dateFormat.format(date);

//        long diff = new Date().getTime() - date.getTime();
//        long seconds = diff / 1000;
//        long minutes = seconds / 60;
//        long hours = minutes / 60;
//        long days = hours / 24;
//        int daysInt = Math.round(days);
//        int hoursInt = Math.round(hours);
//        int minutesInt = Math.round(minutes);
//        if (daysInt == 1) {
//            return "1 day";
//        } else if (daysInt > 1) {
//            return Integer.toString(daysInt) + " days";
//        } else if (hoursInt == 1) {
//            return "1 hour";
//        } else if (hoursInt > 1) {
//            return Integer.toString(hoursInt) + " hours";
//        } else if(minutesInt == 1){
//            return "1 minute";
//        }
//          else if(minutesInt > 1){
//              return Integer.toString(minutesInt) + " minutes";
//        }
//          else {
//            return "less than an hour";
//        }
    }
}

