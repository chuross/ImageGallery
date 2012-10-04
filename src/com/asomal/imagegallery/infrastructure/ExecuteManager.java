package com.asomal.imagegallery.infrastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link Executer}の管理クラス
 * 
 * @author chuross
 * 
 */
public class ExecuteManager<T> extends HashMap<Integer, Executer<T>> {

	private static final long serialVersionUID = 3269199384142687461L;
	private Integer count = 0;

	/**
	 * タスクを追加する
	 * 
	 * @param task 非同期タスク
	 */
	public void add(Executer<T> task) {
		put(count++, task);
	}

	/**
	 * 指定位置にセットする
	 * 
	 * @param position 追加位置
	 * @param task
	 */
	public void set(int position, Executer<T> task) {
		put(position, task);
		count++;
	}

	/**
	 * 全タスクをキャンセルする
	 */
	public void cancelAll() {
		for (Map.Entry<Integer, Executer<T>> task : entrySet()) {
			task.getValue().cancel();
		}
	}

	/**
	 * 指定したタスクをキャンセルする
	 * 
	 * @param position キャンセルしたい位置
	 */
	public void cancel(int position) {
		Executer<T> task = get(position);
		task.cancel();
		remove(position);
	}

}
