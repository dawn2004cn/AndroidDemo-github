package com.noahedu.common.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：Solution$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/6/5$ 11:09$
 */
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}

