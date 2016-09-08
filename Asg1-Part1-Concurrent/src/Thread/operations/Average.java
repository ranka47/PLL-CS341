package Thread.operations;

import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 08-09-2016.
 */
public class Average extends Thread{
    private ArrayList<Integer> data;
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

    @Override
    public void run(){
        if(average() > 100)            System.out.println("State detected from Average");
        else            System.out.println("State not detected from Average");
    }
}
