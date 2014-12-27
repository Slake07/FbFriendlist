package com.crushftest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

public class FriendListActivity extends ActionBarActivity{

	FriendsAdapter adapter;
	String res;
	ListView lvFriends;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		
		initImageLoader();
		
		res = getIntent().getStringExtra("json");
		lvFriends = (ListView)findViewById(R.id.lvFriends);
		
		
		data = new ArrayList<HashMap<String,String>>();
		
		setData();
	}
	
	public void setData(){
		try {
			JSONObject obj = new JSONObject(res);
			
			JSONArray jary = obj.getJSONArray("data");
			
			for(int i=0;i<jary.length();i++){
				JSONObject job = jary.getJSONObject(i);
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("name", job.getString("first_name"));
				
				JSONObject jpic = job.getJSONObject("picture");
				
				JSONObject jurl = jpic.getJSONObject("data");
				map.put("pic", jurl.getString("url"));
				
				data.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adapter = new FriendsAdapter(getApplicationContext(), data, imageLoader);
		
		lvFriends.setAdapter(adapter);
		
	}
	
	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}
}
