package com.noahedu.common.search;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BinarySearch$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/18$ 16:48$
 */
public class BinarySearch implements IArraySearch {
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        int left = 0;
        int right = sourceArray.length - 1; // 注意

        while(left <= right) {
            int mid = left + (right - left) / 2;
            if(sourceArray[mid] == target)
                return mid;
            else if (sourceArray[mid] < target)
                left = mid + 1; // 注意
            else if (sourceArray[mid] > target)
                right = mid - 1; // 注意
        }
        return -1;
    }
}
