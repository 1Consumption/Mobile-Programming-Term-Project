package org.androidtown.seobang_term_project.utils;

import java.util.ArrayList;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class QuickSortArrayList {
    public static void sort(ArrayList<String> data, int l, int r) {
        int left = l;
        int right = r;
        //    int pivot = Integer.parseInt(data[(l + r) / 2].substring(data[(l + r) / 2].indexOf("+") + 1, data[(l + r) / 2].indexOf("&")));
        if (data.get(l).indexOf("w") != -1) {
            int pivot = Integer.parseInt(data.get((l + r) / 2).substring(0, data.get((l + r) / 2).indexOf("w")));
            do {
                while (Integer.parseInt(data.get(left).substring(0, data.get((left)).indexOf("w"))) < pivot)
                    left++;
                while (Integer.parseInt(data.get(right).substring(0, data.get((right)).indexOf("w"))) > pivot)
                    right--;
                if (left <= right) {
                    String temp = data.get(left);
                    data.set(left, data.get(right));
                    data.set(right, temp);
                    left++;
                    right--;
                }
            } while (left <= right);

            if (l < right) sort(data, l, right);
            if (r > left) sort(data, left, r);
        }
    }
}