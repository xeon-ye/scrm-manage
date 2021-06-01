package com.platform.datascope;

import com.platform.common.utils.Constant;
import com.platform.common.utils.ShiroUtils;
import com.platform.modules.cache.CacheFactory;
import com.platform.modules.cache.SysDBCacheLogic;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysRoleOrgEntity;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysRoleOrgService;
import com.platform.modules.util.JSONUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * HttpServlet相关的工具类
 * 
 * @author Kreo
 * 
 */
@Component
public class ContextHelper extends AbstractController {

	private static Log log = LogFactory.getLog(ContextHelper.class);
	@Autowired
	private SysRoleOrgService sysRoleOrgService;

	private static SysRoleOrgService sysRoleOrgServicestatic;

	@PostConstruct
	public void init() {
		sysRoleOrgServicestatic = this.sysRoleOrgService;
	}

	/**
	 * 专门为限制部门人员查询而做的工具函数,可以把部门和人员查询的限制条件加入map中
	 * 
	 * @param m
	 *            待添加map
	 * @param dept_column
	 *            添加限制的dept元素名称
	 * @param user_column
	 *            添加闲置的user元素名称
	 * @date 2014-2-1 下午7:54:01
	 */
	public static void setSearchDeptPermit4Search(Map<String, Object> m, String dept_column, String user_column,String menu_column) {
		SysUserEntity user = ShiroUtils.getUserEntity();
		//如果不是超级管理员，则只能查询本机构及子机构数据
		if (user!=null&&!user.getUserName().contains("admin")) {
			if(dept_column!=null)m.put(dept_column,user.getOrgNo());
			if(user_column!=null)m.put(user_column, user.getUserId());
			if(menu_column!=null)m.put("select_menu",menu_column);
			if(user.getCreateState()!=null&&user.getCreateState()==1) { // 供应商
				m.put("createstate",1);
			}
		}
	}

	/**
	 * 为DataScope方法提供返回部门list
	 * @param sros
	 */
	public static  String setSearchDeptPermit4Search(List<SysRoleOrgEntity> sros, String dept) {
		StringBuffer deps=new StringBuffer();
		if(sros!=null&&sros.size()>0){
			String str = (String) CacheFactory.getCacheInstance().get(SysDBCacheLogic.CACHE_DEPT_PREFIX_SUB + dept);
			String[] s = (String[]) JSONUtil.toObject(str, String[].class);// 转换成数组
			for(SysRoleOrgEntity d:sros){
				if(d!=null&&d.getOrgNo()!=null&&!d.getOrgNo().equals("")){
					if(d.getOrgNo().equals("1")){//只有本部门权
						deps.append("'");
						deps.append(dept+"',");
					}else if(d.getOrgNo().equals("2")){ //本部门权及子部门
						if(s!=null&&s.length>0){
							for(int i=0;i<s.length;i++){
								deps.append("'"+s[i]+"',");
							}
						}
						deps.append("'");
						deps.append(dept+"',");
					}else if(d.getOrgNo().equals("3")){ //仅子部门
						if(s!=null&&s.length>0){
							for(int i=0;i<s.length;i++){
								deps.append("'"+s[i]+"',");
							}
						}
					}else{
						deps.append("'");
						deps.append(d.getOrgNo()+"',");
					}
				}
			}
		}
		if(deps==null||deps.toString().equals("")||deps.length()<=0){
			return "";
		}else{
			return deps.substring(0,deps.length()-1)+"";
		}
	}


	public static Set<String> setOrdertypes(List<SysRoleOrgEntity> sros) {
		Set<String> dset = new HashSet<>();
		if(sros!=null&&sros.size()>0){
			for(SysRoleOrgEntity d:sros){
				if(d!=null&&d.getOrdertype()!=null){
					String[] str=d.getOrdertype().split(",");
					for (int i=0;i<str.length;i++) {
						dset.add(str[i]);
					}
				}
			}
		}
		return dset;
	}

	public static  String setSearchDepts(String userPerm,String userid,String dept) {
		List<SysRoleOrgEntity> sros = new ArrayList<>();
		Map<String, Object> m = new HashMap<>();
		m.put("userId", userid);
		m.put("userPerm", userPerm);
		sros = sysRoleOrgServicestatic.queryOrgNoIsselect(m);
		String orgs = ContextHelper.setSearchDeptPermit4Search(sros, dept);
		return orgs;
	}

	public static  Set<String> setOrdertypesm(String userPerm,String userid) {
		List<SysRoleOrgEntity> sros = new ArrayList<>();
		Map<String, Object> m = new HashMap<>();
		m.put("userId", userid);
		m.put("userPerm", userPerm);
		sros = sysRoleOrgServicestatic.queryOrgNoIsselect(m);

		Set<String> list = new HashSet<>();
		list = ContextHelper.setOrdertypes(sros);
		return list;
	}
}
