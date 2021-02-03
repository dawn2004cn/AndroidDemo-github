package com.noahedu.common.search;

import java.util.Arrays;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：FibonacciSearch$
 * @file describe：----- 斐波那契查找------
 * 1.斐波那契实在二分查找基础上，用斐波那契数列来进行分割
 *  * 2.在斐波那契数列上找一个略大于查找元素表个数的值f(n)
 *  * 3.将查找元素表个数扩充到f(n) 如果要补充元素用最后一个元素补充
 *  * 4.完成后对f(n)个元素进行斐波那契分割,即分割成 前面f(n-1)个元素,后面f(n-2)个元素
 *  * 5.对要查找元素的那个部分进行递归
 *  * 6.就平均性能而言 优于折半查找 但是若一直在左边长半区查找则低于折半查找
 * @anthor :daisg
 * @create time 2020/4/18$ 17:05$
 */
public class FibonacciSearch implements IArraySearch {
    private static int maxsize=20;
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        int low=0;
        int high=sourceArray.length-1;
        int k=0; //斐波那契分割数值下标
        int mid=0;
        int f[]=fibonacci(); //获得斐波那契数列
        //获得斐波那契分割数值下标
        while (high>f[k]-1) {
            k++;
        }

        //利用Java工具类Arrays 构造新数组并指向 数组 a[]
        int[] temp= Arrays.copyOf(sourceArray, f[k]);

        //对新构造的数组进行 元素补充
        for (int i = high+1; i < temp.length; i++) {
            temp[i]=sourceArray[high];
        }

        while (low<=high) {
            //由于前面部分有f[k-1]个元素
            mid=low+f[k-1]-1;
            if (target<temp[mid]) {//关键字小于切割位置元素 继续在前部分查找
                high=mid-1;
                /*全部元素=前部元素+后部元素
                 * f[k]=f[k-1]+f[k-2]
                 * 因为前部有f[k-1]个元素,所以可以继续拆分f[k-1]=f[k-2]+f[k-3]
                 * 即在f[k-1]的前部继续查找 所以k--
                 * 即下次循环 mid=f[k-1-1]-1
                 * */
                k--;
            }
            else if (target>temp[mid]) {//关键字大于切个位置元素 则查找后半部分
                low=mid+1;
                /*全部元素=前部元素+后部元素
                 * f[k]=f[k-1]+f[k-2]
                 * 因为后部有f[k-2]个元素,所以可以继续拆分f[k-2]=f[k-3]+f[k-4]
                 * 即在f[k-2]的前部继续查找 所以k-=2
                 * 即下次循环 mid=f[k-1-2]-1
                 * */
                k-=2;
            }
            else {
                if (mid<=high) {
                    return mid;
                }
                else {
                    return high;
                }
            }
        }
        return -1;
    }
    //生成斐波那契数列
    public static int[] fibonacci() {
        int[] f=new int[maxsize];
        f[0]=1;
        f[1]=1;
        for (int i = 2; i < maxsize; i++) {
            f[i]=f[i-1]+f[i-2];
        }
        return f;
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] f = fibonacci();
        for (int i : f) {
            System.out.print(i + " ");
        }
        System.out.println();

        int[] data = { 1, 5, 15, 22, 25, 31, 39, 42, 47, 49, 59, 68, 88 };

        int search = 39;
        int position = 0;//fibonacciSearch(data, search);
        System.out.println("值" + search + "的元素位置为：" + position);
    }

}
