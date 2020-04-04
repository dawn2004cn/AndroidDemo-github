package com.noahedu.demo.adapter;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：QuickMultiSupport$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/3$ 10:46$
 */
/**
 * ListView GridView RecyclerView多条目适配
 */

public interface QuickMultiSupport<T> {
    /**
     * 获取View类型的数量
     */
    int getViewTypeCount();

    /**
     * 根据数据，获取多条目布局ID
     */
    int getLayoutId(T data);

    /**
     * 根据数据，获取ItemViewType
     */
    int getItemViewType(T data);


    /**
     * 是否合并条目-->>使用RecyclerView时，无效请用原生的RecyclerView
     */
    boolean isSpan(T data);
}
