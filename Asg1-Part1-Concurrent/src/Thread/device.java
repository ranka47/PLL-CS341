package Thread;
import com.company.Main;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 29-08-2016.
 */
public class device {
    public int count = 0;
    sensor device[] = new sensor[Main.SENSORS];
    public isReady flag;
    public device(){
        for(int i=0; i<Main.SENSORS; i++){
            device[i] = new sensor(Integer.toString(i));
        }
        flag = new isReady(this);
    }

    public static double average(ArrayList<Integer> data){
        long sum = 0;
        for(int i=0; i<data.size(); i++){
            sum = sum + data.get(i);
        }
        return sum/(double)(data.size());
    }

    public static long sum(ArrayList<Integer> data){
        long sum = 0;
        for(int i=0; i<data.size(); i++){
            sum = sum + data.get(i);
        }
        return sum;
    }

    public static BigInteger multiply(ArrayList<Integer> data){
        BigInteger mul = new BigInteger("1");
        for(int i=0; i<data.size(); i++){
            mul = mul.multiply(BigInteger.valueOf(data.get(i)));
        }
        return mul;
    }

    public static void checkConditions(ArrayList<Integer> data){
        if(average(data) > 100)            System.out.println("State detected from Average");
        else            System.out.println("State not detected from Average");

        if(sum(data) > 100)            System.out.println("State detected from Sum");
        else            System.out.println("State not detected from Sum");

        if(multiply(data).compareTo(BigInteger.valueOf(100000)) > 0)            System.out.println("State detected from Multiply");
        else            System.out.println("State not detected from Multiple");
    }

}
