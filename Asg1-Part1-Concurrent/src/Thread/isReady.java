package Thread;

import com.company.Main;

import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 30-08-2016.
 * This thread runs till all the instances of the data are generated and checked. It checks whether all the sensors
 * have produced atleast a single amount of data and then extracts them for checking the conditions. To check the
 * conditions it calls the checkConditions(...) function associated with Device class to spawn thread for each operation.
 * This thread waits for each of the operation to complete before checking the next instance of data. After this thread
 * returns the main thread also returns.
 */
public class IsReady extends Thread {

    private Device Device;
    public static boolean isReady = true;
    public IsReady(Device temp){
        Device = temp;
        start();
    }

    @Override
    public void run(){
        while(Device.count < Main.DATALIMIT){
            //Checking if all sensors have generated atleast a single instance of data
            for (int i = 0; i< Main.SENSORS; i++){
                isReady = isReady && !Device.device[i].sensorData.get().isEmpty();
            }

            //If yes i.e. single instance of data is present then the processing starts
            if(isReady){
                ArrayList<Integer> sensorData = new ArrayList<>();
                for(int i=0; i<Main.SENSORS; i++){
                    //No requirement of synchronised block as operations are done atomically.
                    sensorData.add(Device.device[i].sensorData.get().removeFirst());
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
