
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
