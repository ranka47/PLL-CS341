package Thread;

import MergeSort.Sorter;
import com.company.Main;

import java.util.ArrayList;


/**
 * Created by sumeet.ranka47 on 30-08-2016.
 */
public class isReady extends Thread {

    private device Device;
    public static boolean isReady = true;
    public isReady(device temp){
        Device = temp;
        start();
    }

    @Override
    public void run(){
        while(Device.count < Main.DATALIMIT){
            for (int i = 0; i< Main.SENSORS; i++){
                isReady = isReady && !Device.device[i].sensorData.isEmpty();
            }
            if(isReady){
                ArrayList<Integer> sensorData = new ArrayList<>();
                for(int i=0; i<Main.SENSORS; i++){
                    synchronized (Device.device[i].sensorData){
                        sensorData.add(Device.device[i].sensorData.removeFirst());
                    }
                }
                Sorter sortThread = new Sorter(sensorData, Main.CORES);
                /*
                Waiting for sorting to complete else
                it will checkConditions using the unsorted array
                as the sorting would not have completed.
                 */
                try{
                    sortThread.join();
                }
                catch (InterruptedException e){
                    System.out.println("Interrupted Exception for thread: " + Thread.currentThread().getName());
                }

                Device.checkConditions(sensorData);
                try {
                    Device.threadAvg.join();
                    Device.threadMul.join();
                    Device.threadSum.join();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception for thread: " + Thread.currentThread().getName());
                }

                System.out.print((Device.count+1) + ": ");
                for(int i=0; i<Main.SENSORS; i++){
                    System.out.print(sensorData.get(i) + " ");
                }
                System.out.print("\n");

                Device.count++;
            }
            isReady = true;
        }
    }
}
