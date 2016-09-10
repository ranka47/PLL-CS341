import sun.awt.ExtendedKeyCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**************************************************************************
* Code for Assignment 1 Part 3b
* Contains a single class Calculator which extends the JFrame form
* Three methods: 1 constructor, 1 main and 1 helper method
* There are 4 threads working simultaneously:
* 1. A main thread for the GUI
* 2. A thread for iterating over the number labels and highlighting them
* 3. A thread for iterating over the operation labels and highlighting them
* 4. A new thread is instantiated at every key event by the listener
***************************************************************************/

public class Calculator extends JFrame {

    /* The following are declared automatically on adding elements to the JFrame form
    -------------------------------------------------------------------------------*/
    //Text Field for displaying result
    private JTextField tfResult;

    //Labels for the 10 digits (Labeli = digit i)
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label4;
    private JLabel Label5;
    private JLabel Label7;
    private JLabel Label3;
    private JLabel Label6;
    private JLabel Label8;
    private JLabel Label9;
    private JLabel Label0;

    //Labels for the operations: +,-,*,/,=
    private JLabel lblPlus;
    private JLabel lblMinus;
    private JLabel lblMultiply;
    private JLabel lblDivide;
    private JLabel lblEquals;

    //Root panel (Intellij Idea requires root panel in JFrame to have an explicit field name)
    private JPanel pRoot;

    /*The following are global variables which are shared between the various threads
    **Some variables which are written by single thread and read by others are declared as "volatile" 
    **************************************************************************************************/

    //Variable to store the total at any instant
    private double total = 0;

    //variable to store the current highlighted digit
    private volatile double current;

    //variable to store the current number
    private double cur_total = 0;

    //variable to store the current highlighted operator as:
    // 0 - PLUS
    // 1 - MINUS
    // 2 - MULTIPLY
    // 3 - DIVIDE
    private volatile int cur_op = -1;

    //Variable to store the last selected operator
    private int operation;

    //variable to determine whether the last tapped key was for number or operator as:
    //0 -> operator
    //1 -> number
    //This is used to set cur_total to 0 after a calculation
    private int flag = 0;

    /*Constructor for the class, used for initialization*/
    public Calculator() {

        //Key Listener added to the result Text Field.
        //Listens for keystroke and creates a new SwingWorker if key is typed
        //Note: It can be added to any element on the JFrame.
        tfResult.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {

                //"final" variable is required to pass as argument to the SwingWorker
                final KeyEvent key = e;

                /*SwingWorker class for key event overrides 2 methods:
                *1. doInBackground() - This checks the key for Enter and calculates
                *resulting value. This is done in background so that GUI does not 
                *crash due to large computaiton.
                *2. done() - It gets the value from doInBackground() and sets it to the
                *result TextField at the next Event Dispatch Task
                ***********************************************************************/
                SwingWorker<String,Void> keyWorker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        char c = key.getKeyChar();

                        /********LOGIC for computation:************************************
                        *If ENTER key is typed i.e., number is selected-
                        *   If previous selection was an operator, set cur_total to 0   
                        *   Get current digit and increment cur_total accordingly
                        *   print cur_total in text field
                        *
                        *If SPACE key is typed i.e., operator is selected-
                        *   - If PLUS,MINUS,MULTIPLY,DIVIDE was highlighted at selection time,
                        *   set value of "operation" as cur_op and set total as cur_total,
                        *   since now first number is selected and second selection will start.
                        *   - If EQUALS was highlighted, perform the calculation and return
                        *   value of "total". Also set cur_total as total since it now becomes
                        *   the first operand for further computation. 
                        ******************************************************************/
                        if (c == KeyEvent.VK_ENTER) {
                            if(flag==0) cur_total = 0;
                            cur_total = cur_total*10 + current;
                            flag = 1;
                            return cur_total+"";
                        }
                        if (c == KeyEvent.VK_SPACE) {
                            switch (cur_op) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                    total = cur_total;
                                    cur_total = 0;
                                    operation = cur_op;
                                    break;
                                case 4:
                                    switch (operation) {
                                        case 0:
                                            total = total + cur_total;
                                            break;
                                        case 1:
                                            total = total - cur_total;
                                            break;
                                        case 2:
                                            total = total * cur_total;
                                            break;
                                        case 3:
                                            total = total / cur_total;
                                            break;
                                    }
                                    cur_total = total;
                                    return total+"";
                            }
                            flag = 0;
                        }
                        return null;
                    }

                    //This method sets the value of result field after the calculation
                    @Override
                    protected void done() {
                        String s = new String();
                        try {
                            s = get();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        }
                        tfResult.setText(s);
                    }
                };
                keyWorker.execute();
            }
        });
        
        /*After defining the KeyEvent thread, the constructor calls the 
        *start() method to intialize and execute the threads for highlighting
        *numbers and operators.
        **********************************************************************/
        start();
    }

    // The 'main' function initializes the JFrame
    public static void main(String args[]) {
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new Calculator().pRoot);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*Initializes and executes the thread for highlighting the labels periodically*/
    private void start()    {

        /*This SwingWorker overrides two methods as follows:
        *1. doInBackground() - it periodically iterates over all the numbers
        *and adds the current number to a Integer list.
        *2. process() - it gets the latest number from the Integer list and 
        *highlights the corresponding label in the number panel.
        *************************************************************************/
        SwingWorker<Boolean, Integer> numWorker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                /*****LOGIC for highlighting***********************************
                *Simply iterate over all numbers periodically with a pause of 
                *0.8 seconds.
                NOTE: j<5000 is used since infinite loop causes application to 
                crash sometimes.
                **************************************************************/
                int i=1, j = 1;
                while (j < 5000) {
                    current = i;            //Set current digit to i
                    publish(i);             //Pushes current i to the Integer list
                    Thread.sleep(800);      //Sleep for 0.8 second
                    i = (i + 1) % 10;
                    j++;
                }
                return true;
            }

            @Override
            protected void process(List<Integer> chunks) {
                //Gets the current value of number from the Integer list
                Integer n = chunks.get(chunks.size() - 1);

                //First all label highlights are removed and then label corresponding to number i is highlighted.
                deselectNum();
                switch (n) {
                    case 1:
                        Label1.setBackground(Color.GREEN);
                        Label1.setOpaque(true);
                        break;
                    case 2:
                        Label2.setBackground(Color.GREEN);
                        Label2.setOpaque(true);
                        break;
                    case 3:
                        Label3.setBackground(Color.GREEN);
                        Label3.setOpaque(true);
                        break;
                    case 4:
                        Label4.setBackground(Color.GREEN);
                        Label4.setOpaque(true);
                        break;
                    case 5:
                        Label5.setBackground(Color.GREEN);
                        Label5.setOpaque(true);
                        break;
                    case 6:
                        Label6.setBackground(Color.GREEN);
                        Label6.setOpaque(true);
                        break;
                    case 7:
                        Label7.setBackground(Color.GREEN);
                        Label7.setOpaque(true);
                        break;
                    case 8:
                        Label8.setBackground(Color.GREEN);
                        Label8.setOpaque(true);
                        break;
                    case 9:
                        Label9.setBackground(Color.GREEN);
                        Label9.setOpaque(true);
                        break;
                    case 0:
                        Label0.setBackground(Color.GREEN);
                        Label0.setOpaque(true);
                        break;
                }
            }

            //Helper method to remove highlighting from all number labels.
            private void deselectNum() {
                Label0.setBackground(Color.BLACK);
                Label0.setOpaque(false);
                Label1.setBackground(Color.BLACK);
                Label1.setOpaque(false);
                Label2.setBackground(Color.BLACK);
                Label2.setOpaque(false);
                Label3.setBackground(Color.BLACK);
                Label3.setOpaque(false);
                Label4.setBackground(Color.BLACK);
                Label4.setOpaque(false);
                Label5.setBackground(Color.BLACK);
                Label5.setOpaque(false);
                Label6.setBackground(Color.BLACK);
                Label6.setOpaque(false);
                Label7.setBackground(Color.BLACK);
                Label7.setOpaque(false);
                Label8.setBackground(Color.BLACK);
                Label8.setOpaque(false);
                Label9.setBackground(Color.BLACK);
                Label9.setOpaque(false);
            }


        };
        numWorker.execute();

        /*This SwingWorker overrides two methods as follows:
        *1. doInBackground() - it periodically iterates over all the operators
        *and adds the current operator to a Integer list.
        *2. process() - it gets the latest operator from the Integer list and 
        *highlights the corresponding label in the operator panel.
        *************************************************************************/
        SwingWorker<Boolean, Integer> opWorker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                /*****LOGIC for highlighting***********************************
                *Simply iterate over all operators periodically with a pause of 
                *1.0 seconds. This time is taken as different from the
                *Number thread sleep time to clearly differentiate between the
                *two threads.
                NOTE: j<5000 is used since infinite loop causes application to 
                crash sometimes.
                **************************************************************/
                int i=0, j = 1;
                while (j < 5000) {
                    cur_op = i;
                    publish(i);
                    Thread.sleep(1000);
                    i = (i + 1) % 5;
                    j++;
                }
                return true;
            }

            @Override
            protected void process(List<Integer> chunks) {
                //Gets the current value of operator from the Integer list
                Integer n = chunks.get(chunks.size() - 1);

                //First all label highlights are removed and then label corresponding to operator i is highlighted.
                deselectOp();
                switch (n) {
                    case 0:
                        lblPlus.setBackground(Color.RED);
                        lblPlus.setOpaque(true);
                        break;
                    case 1:
                        lblMinus.setBackground(Color.RED);
                        lblMinus.setOpaque(true);
                        break;
                    case 2:
                        lblMultiply.setBackground(Color.RED);
                        lblMultiply.setOpaque(true);
                        break;
                    case 3:
                        lblDivide.setBackground(Color.RED);
                        lblDivide.setOpaque(true);
                        break;
                    case 4:
                        lblEquals.setBackground(Color.RED);
                        lblEquals.setOpaque(true);
                        break;
                }
            }

            //Helper method to remove highlighting from all operator labels.
            private void deselectOp() {
                lblPlus.setBackground(Color.BLACK);
                lblPlus.setOpaque(false);
                lblMinus.setBackground(Color.BLACK);
                lblMinus.setOpaque(false);
                lblMultiply.setBackground(Color.BLACK);
                lblMultiply.setOpaque(false);
                lblDivide.setBackground(Color.BLACK);
                lblDivide.setOpaque(false);
                lblEquals.setBackground(Color.BLACK);
                lblEquals.setOpaque(false);
            }
        };
        opWorker.execute();

    }
}
