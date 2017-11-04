package com.reger.mybatis.utils;

import java.util.List;

import com.github.pagehelper.Page;
import com.reger.core.model.PageInfo;

public final class PageUtis {

	public static <T, TO> PageInfo<TO> outPage(Page<T> page, List<TO> tos) {
		if (page == null)
			return new PageInfo<>();
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNum();
		long total = page.getTotal();
		return new PageInfo<>(pageNo, pageSize, total, tos);
	}

	public static <T> PageInfo<T> outPage(Page<T> page) {
		return outPage(page, page);
	}
}
