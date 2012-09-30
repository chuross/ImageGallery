package com.asomal.imagegallery.domain.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;

import com.asomal.imagegallery.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

//TODO 認証処理は別クラスにもっていきたい
/**
 * Dropbox認証用Activity
 * 
 * @author chuross
 * 
 */
public class DropboxAuthActivity extends Activity {

	private static final String SP_KEY_ACCESS_TOKEN = "access_token";

	private static final String SP_KEY_ACCESS_TOKEN_SECRET = "access_token_secret";

	private static final String SP_IS_AUTHENTICATION = "";

	private DropboxAPI<AndroidAuthSession> dropboxApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources res = getResources();

		AppKeyPair appkeys = new AppKeyPair(res.getString(R.string.dropbox_app_key),
				res.getString(R.string.dropbox_app_secret));
		AndroidAuthSession session = new AndroidAuthSession(appkeys, AccessType.APP_FOLDER);

		dropboxApi = new DropboxAPI<AndroidAuthSession>(session);
		dropboxApi.getSession().startAuthentication(DropboxAuthActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (dropboxApi.getSession().authenticationSuccessful()) {
			dropboxApi.getSession().finishAuthentication();

			AccessTokenPair tokens = dropboxApi.getSession().getAccessTokenPair();

			Resources res = getResources();
			SharedPreferences sp = getSharedPreferences(res.getString(R.string.sp_dropbox_auth), MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putBoolean(res.getString(R.string.sp_key_is_autentication), true);
			edit.putString(SP_KEY_ACCESS_TOKEN, tokens.key);
			edit.putString(SP_KEY_ACCESS_TOKEN_SECRET, tokens.secret);
			edit.commit();

			finish();
			// TODO onActivityResultで処理させたい
			startActivity(new Intent(this, MainActivity.class));
		}
	}
}
