package com.d2d.biztil.Custom_Listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2d.biztil.Helper.RoundedImageView;
import com.d2d.biztil.Model.FrndRequest;
import com.d2d.biztil.Model.Member_model;
import com.d2d.biztil.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bhavika on 8/10/2016.
 */
public
class Frnd_Request_Adapter extends BaseAdapter {
    public LayoutInflater inflater = null;
    ArrayList<FrndRequest> tr_array;
    Context                context;

    public Frnd_Request_Adapter(Activity mainActivity, ArrayList<FrndRequest> tr_array) {
        // TODO Auto-generated constructor stub
        this.tr_array = tr_array;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                                                   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public
    int getCount() {
        // TODO Auto-generated method stub
        return tr_array.size();
    }

    @Override
    public
    Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public
    long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public
    void addAllItem(ArrayList<FrndRequest> addAlldata) {
        tr_array.addAll(addAlldata);
        notifyDataSetChanged();
    }

    @Override
    public
    View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View   rowView;
        rowView = inflater.inflate(R.layout.frg_frnd_req_list_item, null);

        // Initialization
        holder.frg_frnreq_name_tv = (TextView) rowView.findViewById(R.id.frg_frnreq_name_tv);
        holder.frg_frnreq_profile_pic_iv = (RoundedImageView) rowView.findViewById(R.id.frg_frnreq_profile_pic_iv);

        holder.frg_frnreq_accept_btn = (Button) rowView.findViewById(R.id.frg_frnreq_accept_btn);
        holder.frg_frnreq_decline_btn = (Button) rowView.findViewById(R.id.frg_frnreq_decline_btn);


        // data set

        holder.frg_frnreq_name_tv.setText(tr_array.get(position).getMembername());

        holder.frg_frnreq_profile_pic_iv.setTag(tr_array
                                              .get(position).getProfilepic());
        ImageView image = holder.frg_frnreq_profile_pic_iv;
        Picasso.with(context)
               .load(tr_array
                             .get(position).getProfilepic())
               .into(image);


        return rowView;
    }

    public
    class Holder {
        TextView frg_frnreq_name_tv;
        RoundedImageView frg_frnreq_profile_pic_iv;
        Button frg_frnreq_accept_btn,frg_frnreq_decline_btn;

    }

}