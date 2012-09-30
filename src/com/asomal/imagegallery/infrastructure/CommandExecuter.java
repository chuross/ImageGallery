package com.asomal.imagegallery.infrastructure;

/**
 * 非同期処理を実行するクラス
 * 
 * @author chuross
 * 
 */
public class CommandExecuter {

	/**
	 * 非同期処理を実行する
	 * 
	 * @param <T>
	 * 
	 * @param command 処理したいコマンド
	 * @param listener 終了時の処理
	 */
	public static <T> Executer<T> post(final Command<T> command, final Command.OnFinishListener<T> listener) {

		return new Executer<T>() {

			@Override
			protected T doInBackground(Void... params) {
				return command.execute();
			}

			@Override
			protected void onPostExecute(T result) {
				listener.onFinished(result);
			}
		};

	}
}
