package com.noahedu.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.noahedu.demo.R;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：QuickAdapter$
 * @file describe：RecyclerView、ListView、GridView通用的适配器
 * @anthor :daisg
 * @create time 2020/4/3$ 10:42$
 */
public abstract class QuickAdapter<T> extends BaseAdapter {
    private List<T> mData;
    private int mLayoutId;
    private QuickMultiSupport<T> mSupport;
    private boolean isRecycler;
    private int mPosition;
    private Context context;

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public QuickAdapter(Context ctx,int layoutId) {
        this(ctx,layoutId,null,null);
    }

    public QuickAdapter(Context ctx,int layoutId,List<T> data) {
        this(ctx,layoutId,data,null);
    }

    public QuickAdapter(Context ctx,List<T> data, QuickMultiSupport<T> support) {
        this(ctx,0,data,support);
    }

    public QuickAdapter(Context ctx,int layoutId, QuickMultiSupport<T> support) {
        this(ctx,layoutId,null,support);
    }

    public QuickAdapter(Context ctx,QuickMultiSupport<T> support) {
        this(ctx,0,null,support);
    }

    public QuickAdapter(Context ctx,int layoutId, List<T> data,QuickMultiSupport<T> support) {
        this.context = ctx;
        this.mLayoutId = layoutId;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mSupport = support;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if(mData != null) {
            return mData.get(position);
        }
        return  null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuickViewHolder holder;
        if (convertView == null) {
            int layoutId = mLayoutId;
            // 多条目的
            if (mSupport != null) {
                layoutId = mSupport.getLayoutId(mData.get(position));
            }
            // 创建ViewHolder
            holder = createListHolder(parent, layoutId);
        } else {
            holder = (QuickViewHolder) convertView.getTag();
            // 防止失误，还要判断
            if (mSupport != null) {
                int layoutId = mSupport.getLayoutId(mData.get(position));
                // 如果布局ID不一样，又重新创建
                if (layoutId != holder.getLayoutId()) {
                    // 创建ViewHolder
                    holder = createListHolder(parent, layoutId);
                }
            }

        }
        // 绑定View的数据
        convert(holder, mData.get(position), position);
        return holder.itemView;
    }

    /**
     * 创建ListView的Holer
     */
    @NonNull
    private QuickViewHolder createListHolder(ViewGroup parent, int layoutId) {
        QuickViewHolder holder;
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        holder = new QuickViewHolder(itemView, layoutId);
        itemView.setTag(holder);
        return holder;
    }

    /**
     * ViewType的数量
     */
    @Override
    public int getViewTypeCount() {
        // 多条目的
        if (mSupport != null) {
            return mSupport.getViewTypeCount() + super.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    /**
     * 这个方法是共用的
     */
    @Override
    public int getItemViewType(int position) {
        mPosition = position;
        // 多条目的
        if (mSupport != null) {
            return mSupport.getItemViewType(mData.get(position));
        }
        return super.getItemViewType(position);
    }


    // RecyclerView=================================================================================
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        isRecycler = true;
        // 如果是多条目，viewType就是布局ID
        View view;
        if (mSupport != null) {
            Object tagPosition = parent.getTag(R.id.view_position);
            int layoutId = mSupport.getLayoutId(mData.get(mPosition));
            // 如果是滚动布局
            if (tagPosition != null) {
                int position = (int) tagPosition;
                layoutId = mSupport.getLayoutId(mData.get(position));
            }
            view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(mLayoutId, parent, false);
        }

        QuickViewHolder holder = new QuickViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuickViewHolder) {
            convert((QuickViewHolder) holder, mData.get(position), position);
        }

    }

    @Override
    public int getItemCount() {
        if(mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mSupport == null || recyclerView == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            // 如果设置合并单元格就占用SpanCount那个多个位置
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSupport.isSpan(mData.get(position))) {
                        return gridLayoutManager.getSpanCount();
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (mSupport == null) {
            return;
        }
        int position = holder.getLayoutPosition();
        // 如果设置合并单元格
        if (mSupport.isSpan(mData.get(position))) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    // RecyclerView=================================================================================


    /**
     * 绑定View的数据
     */
    protected abstract void convert(QuickViewHolder holder, T item, int position);


    //==========================================数据相关================================================
    public void add(T elem) {
        if(mData != null) {
            mData.add(elem);
        }
        notifyData();

    }
    public void addAll(List<T> data) {
        if(mData != null) {
            mData.addAll(data);
        }
        notifyData();
    }

    public void addFirst(T elem) {
        if(mData != null) {
            mData.add(0, elem);
        }
        notifyData();
    }

    public void set(T oldElem, T newElem) {
        if(mData != null) {
            set(mData.indexOf(oldElem), newElem);
        }
        notifyData();
    }

    public void set(int index, T elem) {
        if(mData != null) {
            mData.set(index, elem);
        }
        notify();
    }

    public void remove(T elem) {
        if(mData != null) {
            mData.remove(elem);
        }
        notifyData();
    }

    public void remove(int index) {
        if(mData != null) {
            mData.remove(index);
        }
        notifyData();
    }

    public void replaceAll(List<T> elem) {
        if(mData != null) {
            mData.clear();
            mData.addAll(elem);
        }
        notifyData();
    }

    /**
     * 清除
     */
    public void clear() {
        if(mData != null) {
            mData.clear();
        }
        notifyData();
    }

    public void setData(List<T> data) {
        this.mData = data;
        notifyData();
    }

    private void notifyData() {
        if (isRecycler) {
            notifyDataSetChanged();
        } else {
            notifyListDataSetChanged();
        }
    }

    public boolean contains(T elem) {
        return mData.contains(elem);
    }


    public List<T> getData() {
        return mData;
    }

}
