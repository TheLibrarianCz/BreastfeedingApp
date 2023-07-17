package com.example.kojeniapp.utils;

public class FlagUtils {

    public static Boolean HasFlag(int value, int flag) {
        return (value & flag) == flag;
    }
}
