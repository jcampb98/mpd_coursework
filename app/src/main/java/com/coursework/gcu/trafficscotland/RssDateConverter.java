package com.coursework.gcu.trafficscotland;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class RssDateConverter {
    public long numberOfDays(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        // add 1 for the start date
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start)) + 1;
    }

    public static Calendar convertRssDateToObj(String date) {
        String shortDate = convertLongDateToShort(date);
        Calendar c = convertStringToDate(shortDate);
        return c;
    }

    // Method to parse a date from the XML long version Wednesday,  12 Febuary 2020 - 00:00
    // to the short version 12/02/2020
    public static String convertLongDateToShort(String dateString) {
        String regex = "\\d+";
        String result = "";
        String[] words = dateString.split(" ");
        for (String word : words) {
            if (word.matches(regex)) {
                result+= word+ "/";
            } else if(!getNumOfMonth(word).isEmpty()) {
                result+= getNumOfMonth(word) + "/";
            }
        }
        return result.substring(0, result.length()-1); // take the last / off the end
    }

    // Converts a date string in the format 01/02/2020 to a Calendar object
    // TODO: Change the name to convertShortDateToCalendar
    public static Calendar convertStringToDate(String dateString) {
        String[] numbers = dateString.split("/");
        int day = Integer.parseInt(numbers[0]);
        int month = Integer.parseInt(numbers[1]);
        int year = Integer.parseInt(numbers[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }

    public static String getNumOfMonth(String month) {
        switch(month) {
            case "January":
                return "01";
            case "February":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                return "";
        }
    }
}
