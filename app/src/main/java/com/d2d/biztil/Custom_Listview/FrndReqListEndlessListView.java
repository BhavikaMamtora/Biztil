package com.d2d.biztil.Custom_Listview;

/*
 * Copyright (C) 2012 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.d2d.biztil.Model.FrndRequest;
import com.d2d.biztil.Model.Member_model;

import java.util.ArrayList;

public class FrndReqListEndlessListView extends ListView implements
        OnScrollListener {

    public boolean shouldLoadFurthur = true;
    public boolean shouldAddFooterView = true;
    private View                      footer;
    private boolean                   isLoading;
    private FrndReqEndlessListener listener;
    private Frnd_Request_Adapter            adapter;

    public FrndReqListEndlessListView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
    }

    public FrndReqListEndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public FrndReqListEndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    public void setListener(FrndReqEndlessListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null)
            return;

        if (getAdapter().getCount() == 0)
            return;

        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount && !isLoading && shouldLoadFurthur == true) {
            // It is time to add new data. We call the listener
            if (shouldAddFooterView == true) {
                this.addFooterView(footer);
            }


            isLoading = true;
            listener.loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void setLoadingView(int resId) {
        LayoutInflater inflater = (LayoutInflater) super.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(resId, null);
        if (shouldAddFooterView == true) {
            this.addFooterView(footer);
        }

    }

    public void setAdapter(Frnd_Request_Adapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        if (shouldAddFooterView == true) {
            this.removeFooterView(footer);
        }

    }

    public void addNewData(
            ArrayList<FrndRequest> member_list_array) {

        if (shouldAddFooterView == true) {
            this.removeFooterView(footer);
        }

        adapter.addAllItem(member_list_array);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public FrndReqEndlessListener setListener() {
        return listener;
    }

    public static interface FrndReqEndlessListener {
        public void loadData();
    }

}