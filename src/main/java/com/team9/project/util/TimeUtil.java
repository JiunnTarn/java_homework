package com.team9.project.util;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static Date formatDateTime(String timeSrc, String f) {
        SimpleDateFormat sdFormat = new SimpleDateFormat(f);
        sdFormat.setLenient(true);
        try {
            if (timeSrc == null || timeSrc.trim().equals("")) {
                return null;
            }

            return sdFormat.parse(timeSrc);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            return null;
        }
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        if (dateTimeStr == null || "".equals(dateTimeStr)) {
            return null;
        }
        boolean hasTime = dateTimeStr.length() > 10;
        String date, time = "";
        Date dateTime = new Date();
        if (hasTime) {
            String[] d = dateTimeStr.split(" ");
            date = d[0];
            time = d[1];
        } else {
            date = dateTimeStr;
        }

        if (date.split("-").length == 3) {
            dateTime.setYear(Integer.parseInt(date.split("-")[0]) - 1900);
            dateTime.setMonth(Integer.parseInt(date.split("-")[1]) - 1);
            dateTime.setDate(Integer.parseInt(date.split("-")[2]));
        } else if (date.split("/").length == 3) {
            dateTime.setYear(Integer.parseInt(date.split("/")[0]) - 1900);
            dateTime.setMonth(Integer.parseInt(date.split("/")[1]) - 1);
            dateTime.setDate(Integer.parseInt(date.split("/")[2]));
        } else if (date.split("\\.").length == 3) {
            dateTime.setYear(Integer.parseInt(date.split("\\.")[0]) - 1900);
            dateTime.setMonth(Integer.parseInt(date.split("\\.")[1]) - 1);
            dateTime.setDate(Integer.parseInt(date.split("\\.")[2]));
        } else if (date.length() == 8) {
            dateTime.setYear(Integer.parseInt(date.substring(0,4))-1900);
            dateTime.setMonth(Integer.parseInt(date.substring(5,6))-1);
            dateTime.setDate(Integer.parseInt(date.substring(7,8)));
        } else {
            throw new ParseException("日期格式不对", 1);
        }

        if (hasTime) {
            if (time.split(":").length == 2) {
                dateTime.setHours(Integer.parseInt(time.split(":")[0]));
                dateTime.setMinutes(Integer.parseInt(time.split(":")[1]));
            } else if (time.split(":").length == 3) {
                dateTime.setHours(Integer.parseInt(time.split(":")[0]));
                dateTime.setMinutes(Integer.parseInt(time.split(":")[1]));
                dateTime.setSeconds(Integer.parseInt(time.split(":")[2]));
            } else {
                throw new ParseException("时间格式不对", 1);
            }
        } else {
            dateTime.setHours(0);
            dateTime.setMinutes(0);
            dateTime.setSeconds(0);
        }
        return dateTime;
    }


    public static String parseStr(Date timeSrc, String f) {
        if (timeSrc == null)
            return null;
        SimpleDateFormat sdFormat = new SimpleDateFormat(f);
        String result = sdFormat.format(timeSrc);
        if (result != null)
            return result;
        else
            return "";
    }

    public static Date nextDay(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, 1);
            return c1.getTime();
        } else
            return null;
    }

    public static Date nextDay(Date date, int num) {
        if (date != null) {
            if (num == 0)
                return date;
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, num);
            return c1.getTime();
        } else
            return null;
    }

    public static Date prevDay(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, -1);
            return c1.getTime();
        } else
            return null;

    }

    public static Date prevDay(Date date, int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, -n);
            return c1.getTime();
        } else
            return null;
    }

    public static Date nextWeek(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, 7);
            return c1.getTime();
        } else
            return null;
    }

    public static Date prevWeek(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, -7);
            return c1.getTime();
        } else
            return null;
    }

    public static Date nextMonth(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, 1);
            return c1.getTime();
        } else
            return null;
    }

    public static Date nextMonth(Date date, int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, n);
            return c1.getTime();
        } else
            return null;
    }

    public static Date prevMonth(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, -1);
            return c1.getTime();
        } else
            return null;
    }

    public static Date prevMonth(Date date, int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, -n);
            return c1.getTime();
        } else
            return null;
    }

    public static Date nextYear(Date date, int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.YEAR, n);
            return c1.getTime();
        } else
            return null;
    }

    public static Date prevYear(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.YEAR, -1);
            return c1.getTime();
        } else
            return null;
    }

    public static int getCurrentWeekDay() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        date = nextDay(date);
        date = nextDay(date);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static void main(String args[]) {
        System.out.println(getCurrentWeekDay());

    }
}
