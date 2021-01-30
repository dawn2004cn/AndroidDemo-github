package com.noahedu.common.search;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BlockSearch$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/18$ 18:06$
 */
public class BlockSearch implements IArraySearch{
/*    int index[]={22,48,86};
    int st2[]={22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53};
		System.out.println("分块查找");
		System.out.println(blocksearch(index,st2,44,6));*/
//分块查找
//index代表索引数组，st2代表待查找数组，keytype代表要查找的元素，m代表每块大小
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        /*int i=sequencesearch(index,keytype);    //shunxunsearch函数返回值为带查找元素在第几块
        System.out.println("在第"+i+"块");
        if(i>=0){
            int j=m*i;   //j为第i块的第一个元素下标
            int curlen=(i+1)*m;
            for(;j<curlen;j++){
                if(st2[j]==keytype)
                    return j;
            }
        }*/
        return -1;
    }
    public static int blocksearch(int[] index,int[]st2,int keytype,int m){
        int i=sequencesearch(index,keytype);    //sequencesearch函数返回值为带查找元素在第几块
        System.out.println("在第"+i+"块");
        if(i>=0){
            int j=m*i;   //j为第i块的第一个元素下标
            int curlen=(i+1)*m;
            for(;j<curlen;j++){
                if(st2[j]==keytype)
                    return j;
            }
        }
        return -1;

    }
    public static int sequencesearch(int[]index,int key){
        if(index[0]>key){   //如果第一块中最大元素大于待查找元素
            return 0;    //返回0.即待查找元素在第0块
        }
        int i=1;
        while((index[i-1]<key)&&(index[i]>key))
            return i;
        return -1;
    }
}
