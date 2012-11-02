package com.asomal.imagegallery.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asomal.imagegallery.util.Logger;

/**
 * ListView系で使用できる{@link ExecuteManager}
 * 
 * @author chuross
 * 
 * @param <T> {@link ExecuteManager}の戻りの型
 */
public class ListViewExecuteManager<T> extends ExecuteManager<T> {

	// 読み込む範囲
	private static final int RANGE = 20;
	private static final String TAG = ListViewExecuteManager.class.getSimpleName();

	/**
	 * 表示範囲以外の非同期処理をキャンセルする
	 * 
	 * @param firstPosition 現在位置
	 */
	public void clean(int firstPosition, int lastPosition) {

		if (map.size() <= 0) {
			return;
		}

		int min = firstPosition;
		int max = lastPosition;

		Logger.d(TAG, "task count: " + map.size());

		List<Integer> cleanList = new ArrayList<Integer>();

		for (Map.Entry<Integer, Executer<T>> executer : map.entrySet()) {
			if (executer.getKey() > max || executer.getKey() < min) {
				Logger.d(TAG, "taskCancel: " + min + "以上 " + executer.getKey() + " " + max + "以下");
				executer.getValue().cancel();
				cleanList.add(executer.getKey());
			}
		}

		for (int targetPos : cleanList) {
			map.remove(targetPos);
		}
	}
}
