package com.reger.mybatis.base.service;

import com.github.pagehelper.PageInfo;

/**
 * 业务基础接口
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
public interface Service<OpsUser, T, TI extends T,TU extends T, TO extends T,TS extends T, ID> {

	/**
	 * 保存一个对象
	 * 
	 * @param ti
	 * @return
	 */
	public TO save(TI ti, OpsUser opsUser) ;

	/**
	 * 获取一个对象
	 * 
	 * @param id
	 * @return
	 */
	public TO get(ID id, OpsUser opsUser) ;

	/**
	 * 更新一个对象
	 * 
	 * @param id
	 * @param ti
	 * @return
	 */
	public TO update(ID id, TU ti, OpsUser opsUser) ;

	/**
	 * 删除一个对象
	 * 
	 * @param id
	 */
	public void del(ID id, OpsUser opsUser) ;

	/**
	 * 分页列出对象
	 * @param pageNo
	 * @param pageSize
	 * @param opsUser
	 * @return
	 */
	public PageInfo<TO> list(int pageNo, int pageSize, OpsUser opsUser) ;

	/**
	 * 搜索对象
	 * @param pageno
	 * @param pagesize
	 * @param search
	 * @param opsUser
	 * @return
	 */
	public PageInfo<TO> search(int pageNo, int pageSize, TS search, OpsUser opsUser) ;

}
