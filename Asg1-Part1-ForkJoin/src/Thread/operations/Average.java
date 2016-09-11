package Thread.operations;

import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 08-09-2016.
 * This thread is spawned from isReady thread. It is responsible for calculating the average and compare with the specified
 threshold.
 */
public class Average extends Thread{
    private ArrayList<Integer> data;    //Data at a particular instant; Received from isReady thread.
    public Average(ArrayList<Integer> data){
        this.data = data;
        start();
    }

    public double average(){
        long sum = 0;
        for(int i=0; i<data.size(); i++){
            sum = sum + data.get(i);
        }
        return sum/(double)(data.size());
    }
    /*
        Calculates the average, compares it with threshold and the thread (AVG) finally ends after printing the required output
     */
    @Override
    public void run(){
        if(average() > 100)            System.out.println("State detected from Average");
        else            System.out.println("State not detected from Average");
    }
}
