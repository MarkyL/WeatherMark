package com.mark.weathermark.view.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.weathermark.enums.UnitsType;
import com.mark.weathermark.view.adapter.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<ItemType, ViewHolderType extends BaseViewHolder>
        extends RecyclerView.Adapter<ViewHolderType> {

    List<ItemType> mDataSet;

    Context mContext;

    // default displaying unit type
    UnitsType mCurrentUnitsType = UnitsType.METRIC;

    public BaseAdapter(Context context) {
        mDataSet = new ArrayList<>();
        setHasStableIds(true);
        this.mContext = context;
    }

    public void updateDataItem(final ItemType item, final int position) {
        mDataSet.set(position, item);
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderType holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void add(ItemType item) {
        mDataSet.add(item);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public void addAll(List<ItemType> items) {
        for (ItemType item : items) {
            add(item);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    private ItemType getItem(int position) {
        return mDataSet.get(position);
    }

    private void remove(ItemType movie) {
        int position = mDataSet.indexOf(movie);
        if (position > -1) {
            mDataSet.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, 1);
        }
    }
}
