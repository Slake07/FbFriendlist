package com.crushftest;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Config;
import android.util.Log;

public class crushfb extends Application {

	private static crushfb mInstance;
	private static SharedPreferences Pref;
	
	@SuppressWarnings("unused")
	public void onCreate() {

		super.onCreate();

		mInstance = this;

		Pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
//		if (Config.DEVELOPER_MODE
//				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//					.detectAll().penaltyDialog().build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//					.detectAll().penaltyDeath().build());
//		}



		checkFBKey();
	}

	public static synchronized crushfb getInstance() {
		return mInstance;
	}
	
	public static boolean checkUserFBLogin() {
		// USERID = Pref.getString(GeneralClass.isUserID, "");
		// return Pref.getBoolean(GeneralClass.isFbLogin, false);

		// boolean isloginShare = Pref.getBoolean(
		// GeneralClass.temp_isFbLoginShare, false);
		boolean isFbLogin = Pref.getBoolean(
				GeneralClass.temp_iUserFaceBookBLOGIN, false);
		// Log.e("is facebook login", "---->" + isFbLogin);
		// if (isloginShare && isFbLogin) {
		// return false;
		// } else {
		if (isFbLogin) {
			return true;
		} else {
			return false;
		}
		// }
	}
	
	public void checkFBKey() {

		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				// String something = new
				// String(Base64.encodeBytes(md.digest()));
				// Log.e("hash key", something);
			}
		} catch (NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}

	}
}
