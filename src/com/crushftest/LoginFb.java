package com.crushftest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class LoginFb extends ActionBarActivity {

	public static final int CONTACT_PICKER_RESULT = 1;
	public static final String DEBUG_TAG = "---->";
	String f_email;
	Button btnFrds;
	String RequestId = "";

	
	private UiLifecycleHelper uiHelper;
	private PendingAction pendingAction = PendingAction.NONE;
	private GraphUser user;
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
			Log.e("HelloFacebook", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
			Log.e("HelloFacebook", "Success!");
		}
	};
	private LoginButton btn_fb_login;
	private String AccessToken = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.invite_friend_screen);


		
		btnFrds = (Button)findViewById(R.id.btn_friends);
		btn_fb_login = (LoginButton) findViewById(R.id.btn_fb_login);
		btn_fb_login.setReadPermissions(Arrays.asList("basic_info", "email",
				"read_friendlists"));


		btnFrds.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new GetLists().execute();
			}
		});



		btn_fb_login
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						LoginFb.this.user = user;
						updateUI("onCreate");
						// It's possible that we were waiting for this.user to
						// be populated in order to post a
						// status update.
						handlePendingAction();
					}
				});

	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			
			}

		} else {
			Log.w(DEBUG_TAG, "Warning: activity result not ok");
		}

		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}
	
	
	public JSONObject getListofFacebookFriend() {
	JSONObject resa = null;
	if (!AccessToken.equalsIgnoreCase("")) {
		resa = GeneralClass.makeHttpRequest(
				"https://graph.facebook.com/" + user.getId()
						+ "/friends?fields=id,first_name,last_name,location,picture,birthday&access_token="
						+ AccessToken, "GET", GeneralClass.perameters);

		
	}
	 return resa;
}
	
	private class GetLists extends AsyncTask<Void, Void, JSONObject> {
	   	 
		JSONObject jre;
		private MyProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new MyProgressDialog(LoginFb.this);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}
		
        @Override
        protected JSONObject doInBackground(Void... params) {
            
        	try {
		
        		jre = getListofFacebookFriend();
        		
		} catch (Exception e) {
			e.printStackTrace();
		}
        	return jre;			
        }
 
        @Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pDialog != null)
				pDialog.dismiss();
			
			Log.e("fb res--->", "--->" + result);
			
			Intent i = new Intent(LoginFb.this, FriendListActivity.class);
			i.putExtra("json", result.toString());
			startActivity(i);
    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		AppEventsLogger.activateApp(this);

		updateUI("onResume");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(LoginFb.this)
					.setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI("onSessionStateChange");
	}

	private void updateUI(String fromWhere) {

		Log.e("fromWhere", "" + fromWhere);

		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());

		if (enableButtons && user != null) {

			AccessToken = session.getAccessToken();
			Log.e("-->", "" + session.getPermissions());
			Log.e("fb user.getId", "" + user.getId());
			Log.e("fb user.getFirstName", "" + user.getFirstName());
			Log.e("fb user.getMiddleName", "" + user.getMiddleName());
			Log.e("fb user.getLastName", "" + user.getLastName());
			Log.e("fb user.getUsername", "" + user.getUsername());

			
			btnFrds.setVisibility(View.VISIBLE);

		} else {

		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			// postPhoto();
			break;
		case POST_STATUS_UPDATE:
			// postStatusUpdate();
			break;
		}
	}




}
