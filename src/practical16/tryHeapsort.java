
//Nakedi Rampai 4343349
//Practical 16
//Heapsort: bottom up and top down heap construction + sorting

import java.io.*;
import java.util.*;

public class tryHeapsort {

    //MIN-HEAP backed by a String array (alphabetical order)
    //Index 1based: root at index 1, children of i at 2i and 2i+1

    static String[] heap;
    static int heapSize;

    //Utility helpers

    static void swap(String[] a, int i, int j) {
        String tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }

    //Restore heap property downward from position i
    static void siftDown(int i, int n) {
        while (true) {
            int smallest = i;
            int left = 2 * i;
            int right = 2 * i + 1;
            if (left <= n && heap[left ].compareTo(heap[smallest]) < 0) smallest = left;
            if (right <= n && heap[right].compareTo(heap[smallest]) < 0) smallest = right;
            if (smallest == i) break;
            swap(heap, i, smallest);
            i = smallest;
        }
    }

    //Restore heap property upward from position i (used in top-down insert)
    static void siftUp(int i) {
        while (i > 1) {
            int parent = i / 2;
            if (heap[parent].compareTo(heap[i]) > 0) {
                swap(heap, parent, i);
                i = parent;
            } else {
                break;
            }
        }
    }
    //(a)Bottom up heap construction (buildUp / heapify)
    //Start from last internal node and sift down each one.
    
    static void buildHeapBottomUp(String[] words) {
        int n = words.length;
        heap = new String[n + 1]; //1 based
        heapSize = n;
        for (int i = 0; i < n; i++) heap[i + 1] = words[i];

        //Last internal node is at n/2
        for (int i = n / 2; i >= 1; i--) {
            siftDown(i, n);
        }
    }

    //(b)Top down heap construction
    //Insert words one by one, sifting each up.
   
    static void buildHeapTopDown(String[] words) {
        int n = words.length;
        heap = new String[n + 1]; //1 based
        heapSize = 0;

        for (String w : words) {
            heapSize++;
            heap[heapSize] = w;
            siftUp(heapSize);
        }
    }

     
    //Heap sort 
    //Repeatedly extract the minimum (root) into a result array.
    //After each extraction shrink the heap and sift down.
  
    static String[] heapSort(int n) {
        String[] sorted = new String[n];
        int remaining = n;
        for (int k = 0; k < n; k++) {
            sorted[k] = heap[1]; //min = root
            heap[1] = heap[remaining]; //move last to root
            remaining--;
            if (remaining > 0) siftDown(1, remaining);
        }
        return sorted;
    }

    //File reading
    
    static String[] readWords(String filename) throws IOException {
        HashMap<String, Integer> D = new HashMap<>();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(filename), "ISO-8859-1"));
        String line;
        while ((line = reader.readLine()) != null) {
            for (String w : line.split("\\s+")) {
                if (w.isEmpty()) continue;
                w = w.replaceAll("^[^a-zA-Z']+|[^a-zA-Z']+$", "").toLowerCase();
                if (w.isEmpty()) continue;
                D.put(w, D.getOrDefault(w, 0) + 1);
            }
        }
        reader.close();
        return D.keySet().toArray(new String[0]);
    }

    //Timing helper: runs the sort REPS times and returns avg ms

    static final int REPS = 5;

    static double timeBottomUp(String[] words) {
        long total = 0;
        String[] sorted = null;
        for (int r = 0; r < REPS; r++) {
            long start = System.nanoTime();
            buildHeapBottomUp(words);
            sorted = heapSort(words.length);
            total += System.nanoTime() - start;
        }
        return total / (double) REPS / 1_000_000.0; //ms
    }

    static double timeTopDown(String[] words) {
        long total = 0;
        for (int r = 0; r < REPS; r++) {
            long start = System.nanoTime();
            buildHeapTopDown(words);
            heapSort(words.length);
            total += System.nanoTime() - start;
        }
        return total / (double) REPS / 1_000_000.0; //ms
    }

     
    //Verify a sorted array is in non decreasing order

    static boolean isSorted(String[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1].compareTo(a[i]) > 0) return false;
        }
        return true;
    }

    
    //Main method
    
    public static void main(String[] args) throws IOException {

        //SMALL TEST (≤ 20 words)
        System.out.println(" SMALL TEST (20 words)");

        String[] sample = {
            "zebra", "apple", "mango", "cherry", "date",
            "elderberry", "fig", "grape", "honeydew", "kiwi",
            "lemon", "melon", "nectarine", "orange", "papaya",
            "quince", "raspberry", "strawberry", "tangerine", "ugli"
        };

        System.out.println("Original : " + Arrays.toString(sample));

        buildHeapBottomUp(sample.clone());
        String[] sortedBU = heapSort(sample.length);
        System.out.println("Bottom-up: " + Arrays.toString(sortedBU));
        System.out.println("Sorted? " + isSorted(sortedBU));

        buildHeapTopDown(sample.clone());
        String[] sortedTD = heapSort(sample.length);
        System.out.println("Top-down : " + Arrays.toString(sortedTD));
        System.out.println("Sorted? " + isSorted(sortedTD));

        //LARGE TEST (Ulysses)
        System.out.println();
        System.out.println(" LARGE TEST (Ulysses words)");

        //Locating ulysses.text (same directory flexibility as Practical 15)
        String[] candidates = {
            "ulysses.text",
            "src/practical16/ulysses.text",
            "src/practical15/ulysses.text"
        };
        String filename = null;
        for (String c : candidates) {
            if (new File(c).exists()) { filename = c; break; }
        }
        if (filename == null) {
            //Fallback: search relative to class location
            filename = tryHeapsort.class
                .getProtectionDomain().getCodeSource().getLocation()
                .getPath().replaceAll("out/.*", "") + "src/practical16/ulysses.text";
        }

        String[] words;
        try {
            words = readWords(filename);
        } catch (FileNotFoundException e) {
            System.out.println("ulysses.text not found. Tried: " + Arrays.toString(candidates));
            System.out.println("Place ulysses.text next to tryHeapsort.java and re-run.");
            return;
        }



    
