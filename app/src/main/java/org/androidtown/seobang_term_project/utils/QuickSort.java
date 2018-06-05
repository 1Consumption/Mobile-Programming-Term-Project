package org.androidtown.seobang_term_project.utils;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class QuickSort {
    public static void sort(String[] data, int l, int r) {
        int left = l;
        int right = r;
        //    int pivot = Integer.parseInt(data[(l + r) / 2].substring(data[(l + r) / 2].indexOf("+") + 1, data[(l + r) / 2].indexOf("&")));
        if (data[l].indexOf("+") != -1) {
            int pivot = Integer.parseInt(data[(l + r) / 2].substring(data[(l + r) / 2].indexOf("+") + 1, data[(l + r) / 2].indexOf("&")));
            do {
                while (Integer.parseInt(data[left].substring(data[left].indexOf("+") + 1, data[left].indexOf("&"))) < pivot)
                    left++;
                while (Integer.parseInt(data[right].substring(data[right].indexOf("+") + 1, data[right].indexOf("&"))) > pivot)
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
        } else if (data[l].indexOf("w") != -1) {
            int pivot = Integer.parseInt(data[(l + r) / 2].substring(0, data[(l + r) / 2].indexOf("w")));
            do {
                while (Integer.parseInt(data[left].substring(0, data[(left)].indexOf("w"))) < pivot)
                    left++;
                while (Integer.parseInt(data[right].substring(0, data[(right)].indexOf("w"))) > pivot)
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
        } else {
            int pivot = Integer.parseInt(data[(l + r) / 2]);

            do {
                while (Integer.parseInt(data[left]) < pivot)
                    left++;
                while (Integer.parseInt(data[right]) > pivot)
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
}