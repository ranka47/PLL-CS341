import sun.awt.ExtendedKeyCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by desh on 8/9/16.
 */

public class Calculator extends JFrame {
    private JTextField tfResult;
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
    private JLabel lblPlus;
    private JLabel lblMinus;
    private JLabel lblMultiply;
    private JLabel lblDivide;
    private JPanel pRoot;
    private JLabel lblEquals;

    private double total = 0;
    private volatile double current;
    private double cur_total = 0;
    private volatile int cur_op = -1;
    private int operation;
    private int flag = 0;


    public Calculator() {
        tfResult.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                final KeyEvent key = e;
                SwingWorker<String,Void> keyWorker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        char c = key.getKeyChar();
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

        start();
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new Calculator().pRoot);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void start()    {

        SwingWorker<Boolean, Integer> numWorker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                int i=1, j = 1;
                while (j < 5000) {
                    current = i;
                    publish(i);
                    Thread.sleep(800);
                    i = (i + 1) % 10;
                    j++;
                }
                return true;
            }

            @Override
            protected void process(List<Integer> chunks) {
                Integer n = chunks.get(chunks.size() - 1);
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

            @Override
            protected void done() {
                tfResult.setText("" + total);

            }


        };
        numWorker.execute();

        SwingWorker<Boolean, Integer> opWorker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
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
                Integer n = chunks.get(chunks.size() - 1);

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

            @Override
            protected void done() {
                tfResult.setText("" + total);
            }
        };
        opWorker.execute();

    }
}
