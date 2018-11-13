package com.company;

public class Utils {
    public static String deleteSpaces(String str) {
        int start = 0, end = str.length() - 1;
        while (str.charAt(start) == ' ')
            ++start;
        while (str.charAt(end) == ' ')
            --end;
        if (end < start)
            return "";
        return str.substring(start, end + 1);
    }
}
