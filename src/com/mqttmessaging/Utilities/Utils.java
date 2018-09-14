package com.mqttmessaging.Utilities;

import android.content.ContentResolver;

public class Utils {

	static String TAG = "Utils";

	public static String getUniqueId(ContentResolver resolver) {

		String androidId = android.provider.Settings.Secure.getString(resolver,
				android.provider.Settings.Secure.ANDROID_ID);

		return androidId;
	}

}
