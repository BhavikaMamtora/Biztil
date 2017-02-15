package com.d2d.biztil.Custom_Listview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2d.biztil.Model.Member_model;
import com.d2d.biztil.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bhavika on 8/10/2016.
 */
public
class Member_Adapter extends BaseAdapter {
    public LayoutInflater inflater = null;
    ArrayList<Member_model> tr_array;
    Context                 context;

    public Member_Adapter(Activity mainActivity, ArrayList<Member_model> tr_array) {
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
    void addAllItem(ArrayList<Member_model> addAlldata) {
        tr_array.addAll(addAlldata);
        notifyDataSetChanged();
    }

    @Override
    public
    View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View   rowView;
        rowView = inflater.inflate(R.layout.act_main, null);
//        holder.tr_provider_oid = (TextView) rowView.findViewById(R.id.tr_provider_oid);
//        holder.tr_provider_img = (ImageView) rowView.findViewById(R.id.tr_provider_img);

        //holder.tr_provider_oid.setText("Order Id: " + tr_array.get(position).getTr_orderid());
//        holder.tr_provider_img.setTag(tr_array
//                                              .get(position).getTr_providerimg());
//        ImageView image = holder.tr_provider_img;
//        Picasso.with(context)
//               .load(tr_array
//                             .get(position).getTr_providerimg())
//               .into(image);


        return rowView;
    }

    public
    class Holder {
        TextView tr_provider_oid;
        ImageView tr_provider_img;
    }

}