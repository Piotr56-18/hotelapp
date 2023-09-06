package com.piotr.springboot.hotelapp.util;
import java.time.format.DateTimeFormatter;
public class SystemUtils {
    //Gender
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    //BedTypes
    public static final String SINGLE = "Single";
    public static final String DOUBLE = "Double";
    public static final String KING_SIZE = "King Size";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static final int HOTEL_NIGHT_START_HOUR = 15;
    public static final int HOTEL_NIGHT_START_MINUTE = 0;
    public static final int HOTEL_NIGHT_END_HOUR = 10;
    public static final int HOTEL_NIGHT_END_MINUTE = 0;
}
