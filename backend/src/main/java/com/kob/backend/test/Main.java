package com.kob.backend.test;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int l = sc.nextInt();
        String s = sc.next();
        double[][] m = new double[13][13];
        double res=0;
        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                m[i][j] = sc.nextDouble();
                if(i==l)res+=m[i][j];
            }
        }

        if (s == "S") {
            System.out.printf("%.1f", res);
        } else {
            System.out.printf("%.1f", res / 12);
        }
    }
}