package Thread;
import Thread.operations.Average;
import Thread.operations.Multiply;
import Thread.operations.Sum;
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
    public Sum threadSum;
    public Multiply threadMul;
    public Average threadAvg;
    public device(){
        for(int i=0; i<Main.SENSORS; i++){
            device[i] = new sensor(Integer.toString(i));
        }
        flag = new isReady(this);
    }

    public void checkConditions(ArrayList<Integer> data){
        threadSum = new Sum(data);
        threadAvg = new Average(data);
        threadMul = new Multiply(data);
    }

}
