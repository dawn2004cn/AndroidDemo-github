package com.noahedu.common.search;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：HashSearch$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/18$ 18:13$
 */
public class HashSearch implements IArraySearch {
    @Override
    public int search(int[] sourceArray, int target) throws Exception {
        // 哈希函数，除留余数法
        int hashAddress = hash(sourceArray, target);

        while (sourceArray[hashAddress] != target) {
            // 利用 开放定址法 解决冲突
            hashAddress = (++hashAddress) % sourceArray.length;
            // 查找到开放单元 或者 循环回到原点，表示查找失败
            if (sourceArray[hashAddress] == 0 || hashAddress == hash(sourceArray, target)) {
                return -1;
            }
        }
        // 查找成功，返回下标
        return hashAddress;
    }

    /**
     * 方法：哈希表插入
     */
    public static void insert(int[] hashTable, int data) {
        // 哈希函数，除留余数法
        int hashAddress = hash(hashTable, data);

        // 如果不为0，则说明发生冲突
        while (hashTable[hashAddress] != 0) {
            // 利用 开放定址法 解决冲突
            hashAddress = (++hashAddress) % hashTable.length;
        }

        // 将待插入值存入字典中
        hashTable[hashAddress] = data;
    }
    /**
     * 方法：构建哈希函数（除留余数法）
     *
     * @param hashTable
     * @param data
     * @return
     */
    public static int hash(int[] hashTable, int data) {
        return data % hashTable.length;
    }

    /**
     * 方法：展示哈希表
     */
    public static String display(int[] hashTable) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i : hashTable) {
            stringBuffer = stringBuffer.append(i + " ");
        }
        return String.valueOf(stringBuffer);
    }
}
