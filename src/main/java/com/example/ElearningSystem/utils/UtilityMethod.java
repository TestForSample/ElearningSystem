package com.example.ElearningSystem.utils;

import java.util.Random;

public interface UtilityMethod {

    static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    static Random rand = new Random();

     String getRandomString(int length);

}
