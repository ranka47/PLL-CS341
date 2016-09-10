import sun.awt.ExtendedKeyCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/********************************************************************
* Code for Assignment 1 Part 3a
* Contains a single class Calculator which extends the JFrame form
* Three methods: 1 constructor, 1 main and 1 helper method
* There are 3 threads working simultaneously:
* 1. A main thread for the GUI
* 2. A thread for iterating over the labels and highlighting them
* 3. A new thread is instantiated at every key event by the listener
*********************************************************************/

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

    //Labels for the operations: +,-,*,/
    private JLabel lblPlus;
    private JLabel lblMinus;
    private JLabel lblMultiply;
    private JLabel lblDivide;

    //Root panel (Intellij Idea requires root panel in JFrame to have an explicit field name)
    private JPanel pRoot;
    /*------------------------------------------------------------------------------*/

    /*The following are global variables which are shared between the various threads
    **Some variables which are written by single thread and read by others are declared as "volatile" 
    **************************************************************************************************/

    //Variable to store the total at any instant
    private double total;

    //Variable to store the current selected digit
    private volatile int current;

    //Variable to store the selected value of operator as:
    // 0 - PLUS
    // 1 - MINUS
    // 2 - MULTIPLY
    // 3 - DIVIDE
    private volatile int operation = -1;

    /*Booleans to store which panel should be highlighted next
    **Example: if numbers are to be highlighted,
    **numSelect should be true and opSelect should be false
    **Using 2 booleans is redundant really, but still used for clarity
    ********************************************************************/
    private volatile boolean numSelect = true;
    private volatile boolean opSelect = false;
    /******************************************************************/

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

                        if(c == KeyEvent.VK_ENTER)  {

                        	/********LOGIC for computation:************************************
                        	*If numSelect is true i.e., number is being selected-
                        	*	Get value of operation and perform the corresponding operation
                        	*	Set numSelect as false and opSelect as true
                        	*
                        	*If opSelect is true i.e., operation is being selected-
                        	*	Just invert numSelect and opSelect to start selecting numbers

                        	NOTE: Initially operation value is -1 , so we just set total as the 
                        	value of current digit. In subsequent iterations, operations are 
                        	performed according to the value of "operation" variable.
                        	******************************************************************/
                            if (numSelect) {
                                numSelect = false;
                                opSelect = true;

                                switch (operation) {
                                    case 0:
                                        total = total + current;
                                        break;
                                    case 1:
                                        total = total - current;
                                        break;
                                    case 2:
                                        total = total * current;
                                        break;
                                    case 3:
                                        total = total / current;
                                        break;
                                    default:
                                        total = current;
                                }
                                // returns the total value as a string
                                return total+"";
                            }
                            else if(opSelect)   {
                                opSelect = false;
                                numSelect = true;
                            }
                        }
                        return null;
                    }

                    //This method sets the value of result field after the calculation
                    @Override
                    protected void done()   {
                        String s = new String();
                        try {
                            s = get();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        }
                        if(s != null)	tfResult.setText(s);
                    }
                };
                keyWorker.execute();
            }
        });
	
		/*After defining the KeyEvent thread, the constructor calls the 
		*start() method to intialize and execute the panel highlighting thread
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
    	*1. doInBackground() - it periodically iterates over all the label numbers
    	*and adds the current label number to a Integer list.
    	*2. process() - it gets the latest label number from the Integer list and 
    	*highlights the corresponding label in the panel.
    	*************************************************************************/
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

            @Override
            protected Void doInBackground() throws Exception {
                int i,j=1;

                /*****LOGIC for highlighting***********************************
                *If numSelect is true, iterate over numbers i.e., 1,2,...,9,0
                *Else iterate over operations i.e., 0,1,2,3
                NOTE: j<5000 is used since infinite loop causes application to 
                crash sometimes.
                **************************************************************/
                while(j<5000) {

                	//Iterating over number panel
                    i = 1;
                    while(numSelect) {
                    	current = i;			//Set current digit to i
                        publish(i);				//Pushes current i to the Integer list
                        Thread.sleep(1000);		//Sleep for 1 second
                        i = (i+1)%10;
                    }

                    //Iterating over operation panel
                    i = 0;
                    while(opSelect) {
                        operation = i;			//Set current operation to i
                        publish(i);				//Pushes current i to the Integer list
                        Thread.sleep(1000);		//Sleep for 1 second
                        i = (i + 1) % 4;
                    }

                    j++;
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
            	//Gets the current value of label number from the Integer list
                Integer n = chunks.get(chunks.size() - 1);

                //First all label highlights are removed and then label corresponding to number i is highlighted.
                if(numSelect) {
                    deselectNum();
                    switch(n)   {
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
                //First all label highlights are removed and then label corresponding to operation i is highlighted.
                else if(opSelect) {
                    deselectOp();
                    switch (n)  {
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
                    }
                }

            }

            //Helper method to remove highlighting from all number labels.
            private void deselectNum()  {
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

            //Helper method to remove highlighting from all operator labels.
            private void deselectOp()   {
                lblPlus.setBackground(Color.BLACK);
                lblPlus.setOpaque(false);
                lblMinus.setBackground(Color.BLACK);
                lblMinus.setOpaque(false);
                lblMultiply.setBackground(Color.BLACK);
                lblMultiply.setOpaque(false);
                lblDivide.setBackground(Color.BLACK);
                lblDivide.setOpaque(false);
            }
        };
        worker.execute();
    }
}
