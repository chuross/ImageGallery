package com.asomal.imagegallery.domain.dropbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.asomal.imagegallery.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

/**
 * DropboxAPIの処理を扱うクラス
 * 
 * @author chuross
 * 
 */
public class DropboxApiManager {

	Context context;
	Resources res;

	public DropboxApiManager(Context context) {
		this.context = context;
		res = context.getResources();
	}

	/**
	 * 認証する
	 * 
	 * @return session APIセッション
	 */
	public DropboxAPI<AndroidAuthSession> Authentication() {
		AppKeyPair appkeys = new AppKeyPair(res.getString(R.string.dropbox_app_key),
				res.getString(R.string.dropbox_app_secret));
		AndroidAuthSession session = new AndroidAuthSession(appkeys, AccessType.APP_FOLDER);

		DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxAPI<AndroidAuthSession>(session);
		dropboxApi.getSession().startAuthentication(context);

		return dropboxApi;
	}

	/**
	 * 認証済みAPIを取得する
	 * 
	 * @return 認証済みAPI
	 * @throws DropboxException Tokenがnullの時
	 */
	public DropboxAPI<AndroidAuthSession> getApi() throws DropboxException {
		SharedPreferences sp = context.getSharedPreferences(res.getString(R.string.sp_dropbox_auth),
				Context.MODE_PRIVATE);

		String userToken = sp.getString(res.getString(R.string.sp_key_access_token), null);
		String userSecret = sp.getString(res.getString(R.string.sp_key_access_token_secret), null);

		if (userToken == null || userSecret == null) {
			throw new DropboxException("Token is null.");
		}

		AppKeyPair access = new AppKeyPair(res.getString(R.string.dropbox_app_key),
				res.getString(R.string.dropbox_app_secret));
		AndroidAuthSession session = new AndroidAuthSession(access, AccessType.APP_FOLDER);

		DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxAPI<AndroidAuthSession>(session);
		AccessTokenPair tokenPair = new AccessTokenPair(userToken, userSecret);
		dropboxApi.getSession().setAccessTokenPair(tokenPair);

		return dropboxApi;
	}
}
