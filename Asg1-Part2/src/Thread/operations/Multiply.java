package Thread.operations;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 08-09-2016.
 */
public class Multiply extends Thread{
    private ArrayList<Integer> data;
    public Multiply(ArrayList<Integer> data){
        this.data = data;
        start();
    }

    public BigInteger multiply(){
        BigInteger mul = new BigInteger("1");
        for(int i=0; i<data.size(); i++){
            mul = mul.multiply(BigInteger.valueOf(data.get(i)));
        }
        return mul;
    }

    @Override
    public void run(){
        if(multiply().compareTo(BigInteger.valueOf(100000)) > 0)            System.out.println("State detected from Multiply");
        else            System.out.println("State not detected from Multiple");
    }
}
