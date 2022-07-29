package com.kob.backend.test;

import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        Stack<Integer> stack = new Stack<>();
        String op;
        int oq;
        while (m-- > 0) {
            op = sc.next();

            if ("push".equals(op)) {
                oq = sc.nextInt();
                stack.push(oq);
            } else if ("pop".equals(op)) {
                stack.pop();
            } else if ("empty".equals(op)) {
                if (stack.isEmpty()){
                    System.out.println("YES");
                }else {
                    System.out.println("NO");
                }
            } else if ("query".equals(op)) {
                System.out.println(stack.peek());
            }
        }
    }
}