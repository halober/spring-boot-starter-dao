//package com.reger.mybatis.base.modelvo;
//
//import java.io.Serializable;
//
//import org.springframework.web.bind.annotation.RequestBody;
//
//import io.swagger.annotations.ApiModelProperty;
//
//
//public class SearchInVO<SearchIn> implements Serializable {
//	public static final long serialVersionUID = 1L;
//
//	private Integer pageNo = 1;
//	
//	private Integer pageSize = 10;
//	
//	@ApiModelProperty("搜索关键词")
//	private String keywords;
//
//	@ApiModelProperty("搜索的参数")
//	private SearchIn search;
//
//	public Integer getPageNo() {
//		return pageNo;
//	}
//
//	public void setPageNo(Integer pageNo) {
//		if (pageNo != null)
//			this.pageNo = pageNo;
//	}
//
//	public Integer getPageSize() {
//		return pageSize;
//	}
//
//	public void setPageSize(Integer pageSize) {
//		if (pageSize != null)
//			this.pageSize = pageSize;
//	}
//
//	public String getKeywords() {
//		return keywords;
//	}
//
//	public void setKeywords(String keywords) {
//		this.keywords = keywords;
//	}
//
//	public SearchIn getSearch() {
//		return search;
//	}
//
//	public void setSearch(SearchIn search) {
//		this.search = search;
//	}
//}
