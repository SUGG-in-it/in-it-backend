package com.example.initbackend.global.util;

import java.util.Random;

public class GenerateRandomNumber {
    public static Long generateRandomNumber(Long max){
        Random random = new Random(); // 랜덤 객체 생성
        random.setSeed(System.currentTimeMillis());
        return Long.valueOf(random.nextInt(Math.toIntExact(max)+1));
    }
}


