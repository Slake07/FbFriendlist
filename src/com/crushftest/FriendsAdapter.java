package com.crushftest;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> data;
	private LayoutInflater mInflater;
	ImageLoader imageLoader;

	public FriendsAdapter(Context con, ArrayList<HashMap<String, String>> data, ImageLoader imageLoader) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(con);
		this.data = data;
		this.imageLoader = imageLoader;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// return product_id1.size();
		return data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// return product_id1.get(position).hashCode();
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ListContent holder;
		View v = convertView;
		if (v == null) {
			v = mInflater.inflate(R.layout.friend_row, null);
			holder = new ListContent();

			holder.ivImg = (ImageView)v.findViewById(R.id.ivFrdImg);
			holder.FrdNm = (TextView) v.findViewById(R.id.tvFrdNm);


			v.setTag(holder);
		} else {

			holder = (ListContent) v.getTag();

		}
		
		holder.FrdNm.setText(data.get(position).get("name"));
		
		imageLoader
		.displayImage(
				data.get(position).get("pic"),
				holder.ivImg,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(
							String imageUri, View view) {
						holder.ivImg
								.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
						super.onLoadingStarted(imageUri,
								view);
					}
				});

		return v;
	}

	class ListContent {

		ImageView ivImg;
		TextView FrdNm;

	}

}
