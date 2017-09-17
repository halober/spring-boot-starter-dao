//package com.reger.mybatis.base.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.Assert;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.github.pagehelper.PageInfo;
//import com.reger.mybatis.base.modelvo.SearchInVO;
//import com.reger.mybatis.base.service.Service;
//
//import io.swagger.annotations.ApiOperation;
//
//public abstract class Controller<OpsUser,T,TI extends T,TU extends T,TO extends T,TS extends T,ID> {
//	
//	@Autowired(required=false)
//	protected Service<OpsUser,T,TI,TU,TO,TS,ID> service;
//
//	protected abstract OpsUser getOpsUser();
//	
//	/**
//	 * 初始化一些通用数据
//	 * @param t
//	 */
//	protected void init(T t){
//	};
//	
//	/**
//	 * @deprecated 请判断是否需要使用
//	 * @param ti
//	 * @return
//	 */
//	@PostMapping
//	@ApiOperation("保存")
//	public ResponseEntity<TO> save(@RequestBody TI ti) {
//		Assert.notNull(service, "没有正确注入service层");
//		OpsUser opsUser= getOpsUser();
//		this.init(ti);
//		TO to = service.save(ti,opsUser);
//		return ResponseEntity.now(to);
//	}
//
//	/**
//	 * @deprecated 请判断是否需要使用
//	 * @param id
//	 * @return
//	 */
//	@GetMapping
//	@ApiOperation("按主键获取一个")
//	public ResponseEntity<TO> get(@RequestParam ID id) {
//		Assert.notNull(service, "没有正确注入service层");
//		OpsUser opsUser= getOpsUser();
//		TO to = service.get(id,opsUser);
//		return ResponseEntity.now(to);
//	}
//
//	/**
//	 * @deprecated 请判断是否需要使用
//	 * @param id
//	 * @param ti
//	 * @return
//	 */
//	@PutMapping
//	@ApiOperation("按主键更新一个")
//	public ResponseEntity<TO> update(@RequestParam ID id,@RequestBody TU ti) {
//		Assert.notNull(service, "没有正确注入service层");
//		OpsUser opsUser= getOpsUser();
//		this.init(ti);
//		TO to = service.update(id,ti,opsUser);
//		return ResponseEntity.now(to);
//	}
//
//	/**
//	 * @deprecated 请判断是否需要使用
//	 * @param id
//	 * @return
//	 */
//	@DeleteMapping
//	@ApiOperation("按主键删除一个")
//	public ResponseEntity<?> del(@RequestParam ID id) {
//		Assert.notNull(service, "没有正确注入service层");
//		OpsUser opsUser= getOpsUser();
//		service.del(id,opsUser);
//		return ResponseEntity.now();
//	}
//
//	/**
//	 * @deprecated 请判断是否需要使用
//	 * @param pageNo
//	 * @param pageSize
//	 * @return
//	 */
//	@GetMapping("/list")
//	@ApiOperation("分页列出")
//	public ResponseEntity<PageInfo<TO>> list(
//			@RequestParam(defaultValue="1") int pageNo,
//			@RequestParam(defaultValue="10") int pageSize ) {
//		Assert.notNull(service, "没有正确注入service层");
//		OpsUser opsUser= getOpsUser();
//		PageInfo<TO> tos=service.list(pageNo,pageSize,opsUser);
//		return ResponseEntity.now(tos);
//	}
//
//	/**
//	 * @deprecated 请判断是否需要使用 
//	 * @param pageNo
//	 * @param pageSize
//	 * @param keywords
//	 * @param search
//	 * @return
//	 */
//	@PostMapping("/search")
//	@ApiOperation("带条件查询,分页列出")
//	public ResponseEntity<PageInfo<TO>> search(
//			@RequestBody(required=true) SearchInVO<TS> search) {
//		Assert.notNull(service, "没有正确注入service层");
//		search=search!=null?search:new SearchInVO<>();
//		OpsUser opsUser= getOpsUser();
//		this.init(search.getSearch());
//		PageInfo<TO> tos=service.search(search.getPageNo(),search.getPageSize(),search.getKeywords(),search.getSearch(),opsUser);
//		return ResponseEntity.now(tos);
//	}
//}
