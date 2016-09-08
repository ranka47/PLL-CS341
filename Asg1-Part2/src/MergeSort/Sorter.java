package MergeSort;

import java.util.ArrayList;

/**
 * Created by sumeet.ranka47 on 09-09-2016.
 */
public class Sorter extends Thread {
    private ArrayList<Integer> a;
    private int threadCount;

    public Sorter(ArrayList<Integer> a, int threadCount) {
        this.a = a;
        this.threadCount = threadCount;
        start();
    }

    public void run() {
        parallelMergeSort(a, threadCount);
    }

    public static void parallelMergeSort(ArrayList<Integer> a, int threadCount) {
        if (threadCount <= 1) {
            mergeSort(a);
        } else if (a.size() >= 2) {
            // split array in half
            ArrayList<Integer> left  = new ArrayList<>(a.subList(0, a.size() / 2));
            ArrayList<Integer> right = new ArrayList<>(a.subList(a.size() / 2, a.size()));

            // sort the halves
            // mergeSort(left);
            // mergeSort(right);
            Thread lThread = new Thread(new Sorter(left,  threadCount / 2));
            Thread rThread = new Thread(new Sorter(right, threadCount / 2));
            lThread.start();
            rThread.start();

            try {
                lThread.join();
                rThread.join();
            } catch (InterruptedException ie) {}

            // merge them back together
            merge(left, right, a);
        }
    }

    // Arranges the elements of the given array into sorted order
    // using the "merge sort" algorithm, which splits the array in half,
    // recursively sorts the halves, then merges the sorted halves.
    // It is O(N log N) for all inputs.
    public static void mergeSort(ArrayList<Integer> a) {
        if (a.size() >= 2) {
            // split array in half
            ArrayList<Integer> left  = new ArrayList<> (a.subList(0, a.size() / 2));
            ArrayList<Integer> right = new ArrayList<> (a.subList(a.size() / 2, a.size()));

            // sort the halves
            mergeSort(left);
            mergeSort(right);

            // merge them back together
            merge(left, right, a);
        }
    }

    // Combines the contents of sorted left/right arrays into output array a.
    // Assumes that left.length + right.length == a.length.
    public static void merge(ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> a) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < a.size(); i++) {
            if (i2 >= right.size() || (i1 < left.size() && left.get(i1) < right.get(i2))) {
                a.set(i, left.get(i1));
                i1++;
            } else {
                a.set(i, right.get(i2));
                i2++;
            }
        }
    }







    // Swaps the values at the two given indexes in the given array.
    public static final void swap(ArrayList<Integer> a, int i, int j) {
        if (i != j) {
            int temp = a.get(i);
            a.set(i, a.get(j));
            a.set(j, temp);
        }
    }

    // Randomly rearranges the elements of the given array.
    public static void shuffle(ArrayList<Integer> a) {
        for (int i = 0; i < a.size(); i++) {
            // move element i to a random index in [i .. length-1]
            int randomIndex = (int) (Math.random() * a.size() - i);
            swap(a, i, i + randomIndex);
        }
    }

    // Returns true if the given array is in sorted ascending order.
    public static boolean isSorted(ArrayList<Integer> a) {
        for (int i = 0; i < a.size() - 1; i++) {
            if (a.get(i) > a.get(i+1)) {
                return false;
            }
        }
        return true;
    }
}