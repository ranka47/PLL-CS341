package Thread;

/**
 * Created by sumeet.ranka47 on 28-08-2016.
 */

import com.company.Main;

import java.util.LinkedList;
import java.util.Random;

public class sensor extends Thread{

    public LinkedList<Integer> sensorData = new LinkedList<>();
    public static String randomBinString(int length){
        Random rand = new Random();
        String str = "";
        for(int i=0; i<length; i++){
            str = str + Integer.toString(rand.nextInt(2));
        }
        return str;
    }

    sensor(String threadName){
        setName(threadName);
        start();
    }

    @Override
    public void run(){
        int count = 0;
        while(count < Main.DATALIMIT) {
            String sensorOutputBin = randomBinString(8);
            Integer sensorOutputInt = Integer.parseInt(sensorOutputBin, 2);
            sensorData.add(sensorOutputInt);
            count++;
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                System.out.println("Interrupted Exception for thread: " + Thread.currentThread().getName());
//            }
        }
    }
}