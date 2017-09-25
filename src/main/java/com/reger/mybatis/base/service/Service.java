//package com.reger.mybatis.base.service;
//
//import com.github.pagehelper.PageInfo;
//
///**
// * 业务基础接口
// * @author leige
// *
// * @param <OpsUser> 操作用户的类
// * @param <T> 数据库原型实体
// * @param <TI> 保存时输入的实体
// * @param <TU> 更新时输入的实体
// * @param <TO> 输出时的实体
// * @param <TS> 搜素时的输入实体
// * @param <ID> 查询主键编号
// */
//public interface Service<OpsUser, T, TI extends T,TU extends T, TO extends T,TS extends T, ID> {
//
//	/**
//	 * 保存一个对象
//	 * 
//	 * @param ti
//	 * @return
//	 */
//	public TO save(TI ti, OpsUser opsUser) ;
//
//	/**
//	 * 获取一个对象
//	 * 
//	 * @param id
//	 * @return
//	 */
//	public TO get(ID id, OpsUser opsUser) ;
//
//	/**
//	 * 更新一个对象
//	 * 
//	 * @param id
//	 * @param ti
//	 * @return
//	 */
//	public TO update(ID id, TU ti, OpsUser opsUser) ;
//
//	/**
//	 * 删除一个对象
//	 * 
//	 * @param id
//	 */
//	public void del(ID id, OpsUser opsUser) ;
//
//	/**
//	 * 列出一个对象
//	 * 
//	 * @param pageno
//	 * @param pagesize
//	 * @return
//	 */
//	public PageInfo<TO> list(int pageno, int pagesize, OpsUser opsUser) ;
//
//	/**
//	 * 搜索对象
//	 * 
//	 * @param pageno
//	 * @param pagesize
//	 * @param keywords
//	 * @param ti
//	 * @return
//	 */
//	public PageInfo<TO> search(int pageno, int pagesize, String keywords, TS ti, OpsUser opsUser) ;
//
//}
