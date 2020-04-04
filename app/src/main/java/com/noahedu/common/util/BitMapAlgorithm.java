/**
 * © 2019 www.youxuepai.com
 * @file name：BitMapAlgorithm.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-6-27下午4:05:49
 * @version 1.0
 */
package com.noahedu.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：BitMapAlgorithm
 *  描述：简单描述该类的作用
 * @class name：BitMapAlgorithm
 * @anthor : daisg
 * @time 2019-6-27下午4:05:49
 * @version V1.0
 */
public class BitMapAlgorithm {	

  //保存数据的
    private byte[] bits;
    
    //能够存储多少数据
    private int capacity;    
    
    public BitMapAlgorithm(int capacity){
        this.capacity = capacity;
        
        //1bit能存储32个数据，那么capacity数据需要多少个bit呢，capacity/32+1,右移3位相当于除以8
        bits = new byte[(capacity >>5 )+1];
    }
    
    /**
     * 设置所在的bit位为1
     * @param n
     */
    public void addValue(int num){

        // num/32得到byte[]的index
        int row = num >> 5; 
        
        // num%32得到在byte[index]的位置
        int position = num & 0x1F; 
        //相当于 n % 32 求十进制数在数组a[i]中的下标
        bits[row] |= 1 << position;
    }



    public void clear(int num){
        // num/32得到byte[]的index
        int row = num >> 5; 
        
        // num%32得到在byte[index]的位置
        int position = num & 0x1F; 
        
        //将1左移position后，那个位置自然就是1，然后对取反，再与当前值做&，即可清除当前的位置了.
        bits[row] &= ~(1 << position); 

    }
    

    // 判断所在的bit为是否为1
    public boolean contains(int n){
        int row = n >> 5;
        int position = n & 0x1F; 
        return (bits[row] & ( 1 << position)) != 0;
    }
    
    
    public void display(int row){
        System.out.println("BitMap位图展示");
        for(int i=0;i<row;i++){
            List<Integer> list = new ArrayList<Integer>();
            int temp = bits[i];
            for(int j=0;j<32;j++){
                list.add(temp & 1);
                temp >>= 1;
            }
            System.out.println("a["+i+"]" + list);
        }
    }

    public static void main(String[] args){
        int num[] = {1,5,30,32,64,56,159,120,21,17,35,45};
        BitMapAlgorithm map = new BitMapAlgorithm(1000000);
        for(int i=0;i<num.length;i++){
            map.addValue(num[i]);
        }

        int temp = 120;
        if(map.contains(temp)){
            System.out.println("temp:" + temp + "has already exists");
        }
        map.display(5);
    }

}
