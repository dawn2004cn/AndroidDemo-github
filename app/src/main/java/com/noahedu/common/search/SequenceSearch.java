package com.noahedu.common.search;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：SequenceSearch$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/18$ 16:45$
 */
public class SequenceSearch implements IArraySearch{
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        for(int i=0; i<sourceArray.length; i++)
            if(sourceArray[i]==target)
                return i;
        return -1;
    }
}
