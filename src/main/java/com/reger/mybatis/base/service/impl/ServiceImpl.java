package com.reger.mybatis.base.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reger.mybatis.base.service.Service;

import tk.mybatis.mapper.common.Mapper;
/**
 * 业务基础实现
 * @author leige
 *
 * @param <OpsUser> 操作用户的类
 * @param <T> 数据库原型实体
 * @param <TI> 保存时输入的实体
 * @param <TU> 更新时输入的实体
 * @param <TO> 输出时的实体
 * @param <TS> 搜素时的输入实体
 * @param <ID> 查询主键编号
 */
public abstract class ServiceImpl<OpsUser, T, TI extends T,TU extends T, TO extends T, TS extends T, ID> implements Service<OpsUser, T, TI,TU, TO, TS, ID> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired(required = false)
	protected Mapper<T> mapper;

	/**
	 * 一个基础对象转化为一个输出对象输出
	 * 
	 * @param t
	 * @return 输出对象
	 */
	protected abstract TO out(T t, OpsUser opsUser);

	/**
	 * 转化输出列表对象
	 * 
	 * @param collection
	 * @param opsUser
	 * @return 输出对象
	 */
	protected List<TO> out(Collection<T> collection, OpsUser opsUser) {
		if(collection==null)
			return Collections.emptyList();
		return collection.stream().map(t->this.out(t, opsUser)).collect(Collectors.toList());
	}

	/**
	 * 转化输出分页对象
	 * @param page
	 * @return 输出对象
	 */
	@SuppressWarnings("unchecked")
	protected PageInfo<TO> outPage(Page<T> page, OpsUser opsUser ) {
		if (page == null)
			return new PageInfo<>();
		@SuppressWarnings("rawtypes")
		PageInfo  pageInfo= new PageInfo<>(page);
		pageInfo.setList(this.out(page, opsUser));
		return pageInfo;
	}

	protected void check(int num) {
		Assert.isTrue(num!=0, "保存数据到数据库失败");
	}

	/**
	 * 保存时编辑对象
	 * 
	 * @param ti
	 * @return 输出对象
	 */
	protected abstract T saveBuild(TI ti, OpsUser opsUser);

	/**
	 * 保存时校验对象
	 * 
	 * @param ti
	 */
	protected void saveCheck(TI ti, OpsUser opsUser){
		
	}

	@Override
	@Transactional
	public TO save(TI ti, OpsUser opsUser) {
		Assert.notNull(mapper, "没有正确注入mapper层");
		this.saveCheck(ti, opsUser);
		T t = saveBuild(ti, opsUser);
		int num = mapper.insertSelective(t);
		this.check(num);
		return this.out(t, opsUser);
	}

	@Override
	public TO get(ID id, OpsUser opsUser) {
		Assert.notNull(mapper, "没有正确注入mapper层");
		Assert.notNull(id,  "输入数据编号不可以为空");
		T t = mapper.selectByPrimaryKey(id);
		Assert.notNull(t,  "没有找到该编号的数据");
		return this.out(t, opsUser);
	}

	/**
	 * 更新时校验对象
	 * 
	 * @param id
	 * @param ti
	 */
	protected void updateCheck(ID id, TU ti,T old, OpsUser opsUser){
		
	}

	/**
	 * 更新时修改对象
	 * 
	 * @param id
	 * @param now
	 * @param old
	 * @return 输出对象
	 */
	protected abstract T updateBuild(ID id, TU now, T old, OpsUser opsUser);

	@Override
	@Transactional
	public TO update(ID id, TU ti, OpsUser opsUser) {
		Assert.notNull(mapper, "没有正确注入mapper层");
		Assert.notNull(id,  "输入数据编号不可以为空");
		T t = mapper.selectByPrimaryKey(id);
		Assert.notNull(t,  "没有找到该编号的数据");
		this.updateCheck(id, ti,t, opsUser);
		T now = this.updateBuild(id, ti, t, opsUser);
		int num = mapper.updateByPrimaryKeySelective(now);
		this.check(num);
		return this.get(id, opsUser);
	}

	@Override
	@Transactional
	public void del(ID id, OpsUser opsUser) {
		Assert.notNull(id,  "输入数据编号不可以为空");
		Assert.notNull(mapper, "没有正确注入mapper层");
		int num = mapper.deleteByPrimaryKey(id);
		this.check(num);
	}

	/**
	 * 列出时，确定查询条件
	 * 
	 * @return 输出对象
	 */
	protected T listBuild(OpsUser opsUser){
		return null;
	};

	@Override
	public PageInfo<TO> list(int pageNum, int pageSize, OpsUser opsUser) {
		Assert.notNull(mapper, "没有正确注入mapper层");
		PageHelper.startPage(pageNum, pageSize);
		T t = listBuild(opsUser);
		Page<T> page = (Page<T>) mapper.select(t);
		return this.outPage(page,opsUser);
	}

	/**
	 * 查询实现
	 * 
	 * @param pageno
	 * @param pagesize
	 * @param ti
	 * @return 输出对象
	 */
	protected Page<T> searchProcess(int pageno, int pagesize, String keywords, TS ti, OpsUser opsUser) {
		Assert.notNull(mapper, "没有正确注入mapper层");
		PageHelper.startPage( pageno  , pagesize);
		Page<T> page = (Page<T>) mapper.select(ti);
		return page;
	}

	@Override
	public PageInfo<TO> search(int pageno, int pagesize, String keywords, TS ti, OpsUser opsUser) {
		Page<T> page = this.searchProcess(pageno, pagesize, keywords, ti, opsUser);
		return this.outPage(page, opsUser);
	}

}
