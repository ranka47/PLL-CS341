package Thread;
import Thread.operations.Average;
import Thread.operations.Multiply;
import Thread.operations.Sum;
import com.company.Main;

import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 29-08-2016.
 */
public class Device {
    public int count = 0;   //Keeps the count of the instances for which the conditions have been checked.
    Sensor device[] = new Sensor[Main.SENSORS]; //Array of the sensor threads
    public IsReady flag;    //isReady thread that calls the checkConditions(...) func
    public Sum threadSum;   //Thread that calculates the SUM
    public Multiply threadMul;  //Thread that calculates the AVG
    public Average threadAvg;   //Thread that calculates the MUL
    public Device(){
        for(int i=0; i<Main.SENSORS; i++){
            device[i] = new Sensor(Integer.toString(i));    //Spawning of ith thread corresponding to ith sensor
        }
        flag = new IsReady(this);   //Spawning of the isReady thread
    }

    /*
    Three threads are spawned each checking the required the conditions.
     */
    public void checkConditions(ArrayList<Integer> data){
        threadSum = new Sum(data);
        threadAvg = new Average(data);
        threadMul = new Multiply(data);
    }

}
