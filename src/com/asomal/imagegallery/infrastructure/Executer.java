package com.asomal.imagegallery.infrastructure;

import android.os.AsyncTask;

/**
 * {@link AsyncTask}のラッパークラス
 * 
 * @author chuross
 * 
 * @param <T> 戻り値の型
 */
public abstract class Executer<T> extends AsyncTask<Void, Void, T> {

	public Executer() {
		execute();
	}

}
