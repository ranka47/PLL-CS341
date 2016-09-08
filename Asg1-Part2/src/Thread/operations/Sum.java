package Thread.operations;

import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 08-09-2016.
 */
public class Sum extends Thread{
    private ArrayList<Integer> data;
    public Sum(ArrayList<Integer> data){
        this.data = data;
        start();
    }

    public long sum(){
        long sum = 0;
        for(int i=0; i<data.size(); i++){
            sum = sum + data.get(i);
        }
        return sum;
    }

    @Override
    public void run(){
        if(sum() > 100)            System.out.println("State detected from Sum");
        else            System.out.println("State not detected from Sum");
    }
}
