package com.company;

import Thread.device;
public class Main {
    public static final Integer DATALIMIT = 30;
    public static final int SENSORS = 20;
    public static void main(String[] args) {
        // write your code here

        device Device = new device();
        try {
            Device.flag.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception for thread: " + Thread.currentThread().getName());
        }
        System.out.println("The End!");
    }
}