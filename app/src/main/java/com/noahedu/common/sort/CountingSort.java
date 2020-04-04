package com.noahedu.common.sort;

import java.util.Arrays;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：CountingSort$
 * @file describe：计数排序
 * @anthor :daisg
 * @create time 2020/3/21$ 10:39$
 */
public class CountingSort implements IArraySort {
    @Override
    public int[] sort(int[] sourceArray) throws Exception {

        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int maxValue = getMaxValue(arr);

        return countingSort(arr, maxValue);
    }
    private int[] countingSort(int[] arr, int maxValue) {
        int bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];

        for (int value : arr) {
            bucket[value]++;
        }

        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                arr[sortedIndex++] = j;
                bucket[j]--;
            }
        }
        return arr;
    }

    private int getMaxValue(int[] arr) {
        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }
}
