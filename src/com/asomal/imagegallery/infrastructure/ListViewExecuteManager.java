package com.asomal.imagegallery.infrastructure;

import java.util.Map;

/**
 * ListView系で使用できる{@link ExecuteManager}
 * 
 * @author chuross
 * 
 * @param <T> {@link ExecuteManager}の戻りの型
 */
public class ListViewExecuteManager<T> extends ExecuteManager<T> {

	private static final long serialVersionUID = 8102141123624000378L;

	// 読み込む範囲
	private static final int RANGE = 20;

	/**
	 * 表示範囲以外の非同期処理をキャンセルする
	 * 
	 * @param start 開始位置
	 */
	public void clean(int start) {

		if (size() <= 0) {
			return;
		}

		int min = (start - RANGE > 0) ? 0 : start - RANGE;
		int max = start + RANGE;

		for (Map.Entry<Integer, Executer<T>> executer : entrySet()) {
			if (executer.getKey() > max || executer.getKey() < min) {
				Executer<T> task = executer.getValue();
				task.cancel();
			}
		}
	}
}
