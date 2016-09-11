package com.company;
import Thread.Device;

/*
Designed in the manner of considering a Device class that incorporates SENSORS number of sensor-class objects which
are actually threads. They generate data and insert at back of LinkedList associated with each sensor-class object.
An independent isReady thread is spawned from the Device object which is responsible for checking the conditions for
calculating and comparing the three operations' values. Main thread waits for this thread (IsReady) to complete and then
returns.
IDEA: It applies the required operations (SUM, AVG, MUL in this case)on the data collected by each of the sensors at
a particular instance and then compare it with their corresponding threshold value. If a thread is slow as compared to
other threads it waits for the them to generate atleast single amount of data before going for the data of the next
instance.
 */
public class Main {

    public static final Integer DATALIMIT = 30; //Limit on the data that can be generated
    public static final int SENSORS = 10;   //Number of the sensors in the device
    public static void main(String[] args) {

        Device Device = new Device();   //Device class object is created and its constructor is called
        try {
            Device.flag.join(); //Waits for the isReady thread of the object 'Device' to return
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception for thread: " + Thread.currentThread().getName());
        }
        System.out.println("The End!"); //Declaration of the end of the program
    }
}
