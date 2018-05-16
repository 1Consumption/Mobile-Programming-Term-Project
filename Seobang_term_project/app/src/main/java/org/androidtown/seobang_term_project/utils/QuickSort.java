package org.androidtown.seobang_term_project.utils;

public class QuickSort {
    public static void sort(String[] data, int l, int r) {
        int left = l;
        int right = r;
        int pivot = Integer.parseInt(data[(l + r) / 2].substring(0, data[(l + r) / 2].indexOf("&")));

        do {
            while (Integer.parseInt(data[left].substring(0, data[left].indexOf("&"))) < pivot)
                left++;
            while (Integer.parseInt(data[right].substring(0, data[right].indexOf("&"))) > pivot)
                right--;
            if (left <= right) {
                String temp = data[left];
                data[left] = data[right];
                data[right] = temp;
                left++;
                right--;
            }
        } while (left <= right);

        if (l < right) sort(data, l, right);
        if (r > left) sort(data, left, r);
    }
}
