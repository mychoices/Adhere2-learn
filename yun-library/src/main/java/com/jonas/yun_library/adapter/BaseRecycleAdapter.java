package com.jonas.yun_library.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonas.yun_library.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赟.
 * @date 2016/1/27
 * @des [recycleview适配器 基类,支持多种布局]
 * @since [产品/模版版本]
 *
 * <p> 动画参考  https://github.com/wasabeef/recyclerview-animators
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    private final static String TAG = BaseRecycleAdapter.class.getSimpleName();
    private SparseArray<Integer> mItemLayoutIds = new SparseArray<>();
    private List<T> mData = new ArrayList<>();

    /**
     * <p>需要重写{@link #getItemViewType(int)} </p>
     * @param data
     * @param itemLayoutId <p color="white">数组下标 作为item类型</p>
     */
    public BaseRecycleAdapter(@NonNull List<T> data, int... itemLayoutId) {
        for (int i = 0; i < itemLayoutId.length; i++) {
            mItemLayoutIds.append(i, itemLayoutId[i]);
        }
        mData = data;
    }

     /**
     * <p>需要重写{@link #setItemLayouts(SparseArray)}和{@link #getItemViewType(int)} </p>
     * @param data
     */
    public BaseRecycleAdapter(@NonNull List<T> data) {
        mData = data;
        setItemLayouts(mItemLayoutIds);
    }

    /**
     * 设置 type 和 布局 需要同时复写getItemViewType
     * <p color="red">itemLayoutIds.append(type,itemLayoutId);
     *
     * @param itemLayoutIds
     */
    protected void setItemLayouts(SparseArray<Integer> itemLayoutIds) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * @param viewType 和getItemViewType 相对应
     * @return
     */
    private int getItemTypeLayout(int viewType) {
        return mItemLayoutIds.get(viewType);
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(getItemTypeLayout(viewType), parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        convert(holder, position, mData.get(position));
    }

    public abstract void convert(RecyclerHolder holder, int position, T itemData);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void notifyDataChange(@NonNull List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void refreshDataChange(@NonNull List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void notifyDataChange(int position, @NonNull T data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        if (position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
        } else {
            if (BuildConfig.DEBUG) Log.e(TAG, "position out of bounde of mData.size()");
        }
    }

    public void addItem(T data, int position) {
        if (position > mData.size()) {
            if (BuildConfig.DEBUG) Log.e(TAG, position + " > mData.size():" + mData.size());
            return;
        }
        mData.add(position, data);
        notifyItemInserted(position);
    }
}
