package com.noahedu.common.search;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：InsertionSearch$
 * @file describe：插值查找算法
 * @anthor :daisg
 * @create time 2020/4/18$ 16:49$
 */
public class InsertionSearch  implements IArraySearch{
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        return InsertionSearch(sourceArray,target,0,sourceArray.length-1);
    }

    private int InsertionSearch(int sourceArray[], int target, int left, int right){
        //注意:target < sourceArray[0] 和 target > sourceArray[arr.length - 1] 必须需要
        // 否则我们得到的 mid 可能越界
        if (left > right || target < sourceArray[0] || target > sourceArray[sourceArray.length - 1]) {
             return -1;
         }
        // 求出 mid, 自适应
        int mid = left + (right - left) * (target - sourceArray[left]) / (sourceArray[right] - sourceArray[left]);
        int midVal = sourceArray[mid];

        if (target > midVal) { // 说明应该向右边递归
            return InsertionSearch(sourceArray, target,mid + 1, right);
        } else if (target < midVal) { // 说明向左递归查找
            return InsertionSearch(sourceArray, target,left, mid - 1);
        } else {
        return mid;
    }
}
}
