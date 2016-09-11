package Thread;

/**
 * Created by sumeet.ranka47 on 28-08-2016.
 */

import com.company.Main;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/*
Sensor class is extended from Thread class. It creates data randomly and adds it to the tail of the link list sensordata
referenced by an AtomicReference implying that all the operation on link list will be done atomically.
Once it has geenrated DATALIMIT amount of data the thread ends successfully.
 */
public class Sensor extends Thread{
    //LinkedList of Integers referred by an AtomicReference
    public AtomicReference<LinkedList<Integer>> sensorData = new AtomicReference<>(new LinkedList<Integer>());
    //Function to generate random bit string
    public static String randomBinString(int length){
        Random rand = new Random();
        String str = "";
        for(int i=0; i<length; i++){
            str = str + Integer.toString(rand.nextInt(2));
        }
        return str;
    }

    Sensor(String threadName){
        setName(threadName);
        start();
    }

    @Override
    public void run(){
        int count = 0;  //To keep count of the amount of data that has been generated till an instance of time
        while(count < Main.DATALIMIT) {
            String sensorOutputBin = randomBinString(8);
            Integer sensorOutputInt = Integer.parseInt(sensorOutputBin, 2);

            /*
            Addition in the link list containing the data is a bit different. First get the object. Modify it and then
            add back.
            */
            LinkedList<Integer> temp = sensorData.get();
            temp.add(sensorOutputInt);
            sensorData.getAndSet(temp);
            count++;
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                System.out.println("Interrupted Exception for thread: " + Thread.currentThread().getName());
//            }
        }
    }
}
