package com.asomal.imagegallery.infrastructure;

/**
 * {@link CommandExecuter}に送るコマンド
 * 
 * @author chuross
 * 
 */
public interface Command<T> {

	public interface OnFinishListener<T> {

		/**
		 * 終了時の処理
		 * 
		 * @param result
		 */
		public void onFinished(T result);

	}

	public T execute();

	public void onCanceled();

}
