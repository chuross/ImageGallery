package com.asomal.imagegallery.infrastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link Executer}の管理クラス
 * 
 * @author chuross
 * 
 */
public class ExecuteManager<T> {

	private Integer count = 0;

	protected Map<Integer, Executer<T>> map;

	public ExecuteManager() {
		map = new HashMap<Integer, Executer<T>>();
	}

	/**
	 * 指定位置にセットする
	 * 
	 * @param position 追加位置
	 * @param task
	 */
	public void set(int position, Executer<T> task) {
		if (map.containsKey(position)) {
			task.cancel();
			return;
		}
		map.put(position, task);
		count++;
	}

	/**
	 * 指定位置を削除する
	 * 
	 * @param position
	 */
	public void remove(int position) {
		map.remove(position);
	}

	/**
	 * 全タスクをキャンセルする
	 */
	public void cancelAll() {
		for (Map.Entry<Integer, Executer<T>> task : map.entrySet()) {
			task.getValue().cancel();
		}
		map.clear();
	}

	/**
	 * 指定したタスクをキャンセルする
	 * 
	 * @param position キャンセルしたい位置
	 */
	public void cancel(int position) {
		Executer<T> task = map.get(position);
		task.cancel();
		map.remove(position);
	}

	/**
	 * 登録済みかどうか判定する
	 * 
	 * @param position キー
	 * @return true:追加済 false:まだ
	 */
	public boolean containsKey(int position) {
		return map.containsKey(position);
	}

}
