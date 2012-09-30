package com.asomal.imagegallery.infrastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * リモートにある画像パスを取得する
 * 
 * @author chuross
 * 
 */
public class GetRemoteImageFilePathCommand implements Command<List<String>> {

	@Override
	public List<String> execute() {
		List<String> pathList = new ArrayList<String>();

		return pathList;
	}
}
