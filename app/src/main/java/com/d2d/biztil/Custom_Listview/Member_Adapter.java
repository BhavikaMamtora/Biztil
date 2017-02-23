package com.d2d.biztil.Custom_Listview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        rowView = inflater.inflate(R.layout.frg_member_list_item, null);

        // Initialization
        holder.frg_ml_company_tv = (TextView) rowView.findViewById(R.id.frg_ml_company_tv);
        holder.frg_ml_location_tv = (TextView) rowView.findViewById(R.id.frg_ml_location_tv);
        holder.frg_ml_category_tv = (TextView) rowView.findViewById(R.id.frg_ml_category_tv);
        holder.frg_ml_star_tv = (TextView) rowView.findViewById(R.id.frg_ml_star_tv);


        holder.frg_ml_pic_iv = (ImageView) rowView.findViewById(R.id.frg_ml_pic_iv);
        holder.frg_ml_star_1 = (ImageView) rowView.findViewById(R.id.frg_ml_star_1);

        holder.frg_ml_view_product_btn = (Button) rowView.findViewById(R.id.frg_ml_view_product_btn);
        holder.frg_ml_msg_btn = (Button) rowView.findViewById(R.id.frg_ml_msg_btn);
        holder.frg_ml_add_frnd_btn = (Button) rowView.findViewById(R.id.frg_ml_add_frnd_btn);


        // data set

        holder.frg_ml_company_tv.setText(tr_array.get(position).getCompany());
        holder.frg_ml_location_tv.setText(tr_array.get(position).getCity());
        holder.frg_ml_category_tv.setText(tr_array.get(position).getMembercategory());
        holder.frg_ml_star_tv.setText(tr_array.get(position).getRating());
        holder.frg_ml_add_frnd_btn.setText(tr_array.get(position).getFriendstatus());

        holder.frg_ml_pic_iv.setTag(tr_array
                                              .get(position).getProfilepic());
        ImageView image = holder.frg_ml_pic_iv;
        Picasso.with(context)
               .load(tr_array
                             .get(position).getProfilepic())
               .into(image);


        return rowView;
    }

    public
    class Holder {
        TextView frg_ml_company_tv,frg_ml_location_tv,frg_ml_category_tv,frg_ml_star_tv;
        ImageView frg_ml_pic_iv,frg_ml_star_1,frg_ml_star_2,frg_ml_star_3,frg_ml_star_4,frg_ml_star_5;
        Button frg_ml_view_product_btn,frg_ml_msg_btn,frg_ml_add_frnd_btn;

    }

}