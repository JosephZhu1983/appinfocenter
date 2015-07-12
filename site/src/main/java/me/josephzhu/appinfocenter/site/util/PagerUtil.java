package me.josephzhu.appinfocenter.site.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PagerUtil
{
	
	public static int getPageCount(int dataCount, int pageSize) {
		int pageCount = 1;
		if(dataCount > 0) {
			if(dataCount % pageSize == 0) {
				pageCount = dataCount / pageSize;
			} else {
				pageCount = dataCount / pageSize + 1;
			}
		}
		return pageCount;
	}
	
	public static List<Integer> generatePageList(int p, int pageCount) {
		List<Integer> pageList = new ArrayList<>();
		if(p <= 5) {
			for(int i = 1; i < 10 && i <= pageCount; i++) {
				pageList.add(i);
			}
		} else if(p >= pageCount - 4) {
			for(int i = pageCount; i > 0 && i > pageCount - 9; i--) {
				pageList.add(i);
			}
			Collections.reverse(pageList);
		} else {
			for(int i = p - 4; i <= p + 4; i++) {
				pageList.add(i);
			}
		}
		return pageList;
	}
}