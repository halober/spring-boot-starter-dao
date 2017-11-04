package com.reger.mybatis.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.reger.core.model.ResponseEntity;
import com.reger.mybatis.base.service.Service;

import io.swagger.annotations.ApiOperation;

public abstract class Controller<OpsUser, T, TI extends T, TU extends T, TO extends T, TS extends T, ID> {

	@Autowired(required = false)
	protected Service<OpsUser, T, TI, TU, TO, TS, ID> service;

	protected abstract OpsUser getOpsUser();

	/**
	 * 初始化一些通用数据
	 * 
	 * @param t
	 */
	protected void init(T t) {
	};

	@PostMapping
	@ApiOperation("保存")
	public ResponseEntity<TO> save(@RequestBody TI ti) {
		Assert.notNull(service, "没有正确注入service层");
		OpsUser opsUser = getOpsUser();
		this.init(ti);
		TO entity = service.save(ti, opsUser);
		return ResponseEntity.success(entity);
	}

	@GetMapping("/{id}")
	@ApiOperation("按主键获取一个")
	public ResponseEntity<TO> get(@PathVariable ID id) {
		Assert.notNull(service, "没有正确注入service层");
		OpsUser opsUser = getOpsUser();
		TO entity = service.get(id, opsUser);
		return ResponseEntity.success(entity);
	}

	@PutMapping("/{id}")
	@ApiOperation("按主键更新一个")
	public ResponseEntity<TO> update(@PathVariable ID id, @RequestBody TU ti) {
		Assert.notNull(service, "没有正确注入service层");
		OpsUser opsUser = getOpsUser();
		this.init(ti);
		TO entity = service.update(id, ti, opsUser);
		return ResponseEntity.success(entity);
	}

	@DeleteMapping("/{id}")
	@ApiOperation("按主键删除一个")
	public ResponseEntity<?> del(@PathVariable ID id) {
		Assert.notNull(service, "没有正确注入service层");
		OpsUser opsUser = getOpsUser();
		service.del(id, opsUser);
		return ResponseEntity.success();
	}

	@GetMapping("/list")
	@ApiOperation("分页列出")
	public ResponseEntity<PageInfo<TO>> list(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Assert.notNull(service, "没有正确注入service层");
		OpsUser opsUser = getOpsUser();
		PageInfo<TO> entity = service.list(pageNo, pageSize, opsUser);
		return ResponseEntity.success(entity);
	}

	@PostMapping("/search")
	@ApiOperation("带条件查询,分页列出")
	public ResponseEntity<PageInfo<TO>> search(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize, @RequestBody TS search) {
		Assert.notNull(service, "没有正确注入service层");
		OpsUser opsUser = getOpsUser();
		this.init(search);
		PageInfo<TO> entity = service.search(pageNo, pageSize, search, opsUser);
		return ResponseEntity.success(entity);
	}
}
