package Thread;

import com.company.Main;

import java.util.ArrayList;

import static Thread.device.checkConditions;

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
            synchronized (Device.device){
                for (int i = 0; i< Main.SENSORS; i++){
                    isReady = isReady && !Device.device[i].sensorData.isEmpty();
                }
                if(isReady){
                    ArrayList<Integer> sensorData = new ArrayList<>();
                    for(int i=0; i<Main.SENSORS; i++){
                        sensorData.add(Device.device[i].sensorData.removeFirst());
                    }
                    checkConditions(sensorData);
                    System.out.print(Device.count + ": ");
                    for(int i=0; i<Main.SENSORS; i++){
                        System.out.print(sensorData.get(i) + " ");
                    }
                    System.out.print("\n");

                    Device.count++;
                }
            }
            isReady = true;
        }
    }
}