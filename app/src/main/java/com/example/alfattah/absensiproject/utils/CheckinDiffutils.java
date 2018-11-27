package com.example.alfattah.absensiproject.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.alfattah.absensiproject.Model.ChekinModel;

import java.util.List;

public class CheckinDiffutils extends DiffUtil.Callback {

    private List<ChekinModel> checkinNewsList;
    private List<ChekinModel> checkinOldList;

    public CheckinDiffutils(List<ChekinModel> checkinNewsList, List<ChekinModel> checkinOldList) {
        this.checkinNewsList = checkinNewsList;
        this.checkinOldList = checkinOldList;
    }

    @Override
    public int getOldListSize() {
        return checkinOldList.size();
    }

    @Override
    public int getNewListSize() {
        return checkinNewsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
       return checkinOldList.get(oldItemPosition).getUid() == checkinNewsList.get(newItemPosition).getUid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ChekinModel oldcheckin = checkinOldList.get(oldItemPosition);
        final ChekinModel newscheckin = checkinOldList.get(newItemPosition);
        return  oldcheckin.getUid().equals(newscheckin.getUid());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
