/*
 * 项目名称:platform-plus
 * 类名称:SysMenuController.java
 * 包名称:com.platform.modules.sys.controller
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2018/11/21 16:04    李鹏军      初版完成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.sys.controller;

import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.JedisUtil;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.AddGroup;
import com.platform.common.validator.group.UpdateGroup;
import com.platform.datascope.ContextHelper;
import com.platform.modules.qkjvip.entity.QkjvipMemberChannelEntity;
import com.platform.modules.qkjvip.entity.QkjvipOptionsEntity;
import com.platform.modules.qkjvip.entity.QkjvipTaglibsEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberChannelService;
import com.platform.modules.qkjvip.service.QkjvipMemberMessageService;
import com.platform.modules.qkjvip.service.QkjvipTaglibsService;
import com.platform.modules.sys.entity.*;
import com.platform.modules.sys.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 系统菜单
 *
 * @author 李鹏军
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private SysOrgService orgService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private QkjvipTaglibsService qkjvipTaglibsService;
    @Autowired
    private QkjvipMemberChannelService qkjvipMemberChannelService;
    @Autowired
    private QkjvipMemberMessageService qkjvipMemberMessageService;
    @Autowired
    private SysUserChannelService sysUserChannelService;

    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    SysCacheService sysCacheService;
    /**
     * 导航菜单
     *
     * @return RestResponse
     */
    @GetMapping("/nav")
    public RestResponse nav() {
        Map<String, Object> map = new HashMap<>(2);
        long start1=System.currentTimeMillis();
        List<String> listkey = Arrays.asList("MTM_CACHE:IMMELISTALL:DICTLIST","MTM_CACHE:ORGLISTALL:ORGLIST","MTM_CACHE:USERLISTALL:USERS","MTM_CACHE:IMMELISTALL:USERROLELIST");
        List list = jedisUtil.getpipeline(listkey);
        List<SysDictEntity> dictList = new ArrayList<>();//字典
        List<SysOrgEntity> orgList = new ArrayList<>();//部门列表
        List<SysUserEntity> userList = new ArrayList<>();//userlist
        List<SysMenuEntity> roleuserList = new ArrayList<>();// 权限列表
        if (list!=null&&list.size()>0) {
            for(int i=0;i<list.size();i++){
                redisEntity red = new redisEntity();
                red =  (redisEntity) list.get(i);
                if (red!=null) {
                    if (red.getKey()!=null&&red.getKey().equals("dictList")) dictList=red.getEntityList();
                    if (red.getKey()!=null&&red.getKey().equals("orgList")) orgList=red.getEntityList();
                    if (red.getKey()!=null&&red.getKey().equals("userList")) userList=red.getEntityList();
                    if (red.getKey()!=null&&red.getKey().equals("userRoleList")) roleuserList=red.getEntityList();
                }
            }
        }
        // 如果缓存中没有取到从数据库取
        if (dictList==null||dictList.size()<=0)dictList=getsqlList(1);
        if (orgList==null||orgList.size()<=0)orgList =getsqlList(2);
        if (userList==null||userList.size()<=0)userList =getsqlList(3);
        if (roleuserList==null||roleuserList.size()<=0)roleuserList =getsqlList(4);

        String username=getUser().getUserName();
        String userId=getUserId();
        //用户权限列表
        Set<String> permissions = new HashSet<>();
        List<String> permsList = new ArrayList<>();
        Map<String, Object> orgMaps = new HashMap<>();
        for (SysMenuEntity sme:roleuserList) {
            if (username!=null&&username.contains("admin")) { //管理员
                permsList.add(sme.getPerms());
            } else {
                if(sme.getUserId()!=null&&sme.getUserId().equals(userId)){
                    permsList.add(sme.getPerms());
                    orgMaps.put(sme.getPerms(),sme.getOrgnoselect()+";" + sme.getOrglist());
                }

            }
        }
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            String [] perstr=perms.trim().split(",");
            permissions.addAll(Arrays.asList(perstr));
        }
        long end2=System.currentTimeMillis();
        logger.info("the redis get time "+(end2-start1));

        long start2=System.currentTimeMillis();
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
        List<QkjvipTaglibsEntity> areaList = qkjvipTaglibsService.list(new QueryWrapper<QkjvipTaglibsEntity>().eq("TAG_GROUP_ID", "9af1533bea3d4c89b856ad80e9d0e457")); //liuqianru add
        List<QkjvipMemberChannelEntity> channelList = qkjvipMemberChannelService.queryAll(map);  // 所有得渠道
        List<QkjvipOptionsEntity> appChannels = qkjvipMemberMessageService.queryChannels();
        String channelIds = "";
        channelIds = sysUserChannelService.queryChannelIdByUserId(getUserId());
        map.clear();
//        map.put("channelType", 1);
        map.put("userId", getUserId());
        if ("0".equals(channelIds) || getUser().getUserName().contains("admin")) {
            map.put("queryPermission", "all");
        }
        List<SysUserChannelEntity> permissionChannels = sysUserChannelService.queryPermissionChannels(map);  // 有权限的渠道
        long end22=System.currentTimeMillis();
        logger.info("the selectsql get time "+(end22-start2));

        return RestResponse.success()
                .put("menuList", menuList)
                .put("permissions", permissions)
                .put("dictList", dictList)
                .put("orgList", orgList)
                .put("userList", userList)
                .put("areaList", areaList)
                .put("channelList", channelList)
                .put("appChannels", appChannels)
                .put("permissionChannels", permissionChannels);
    }


    /**
     * 更新redis
     */
    public List getsqlList (Integer type){
        Map<String, Object> map = new HashMap<>();
        List list = new ArrayList();
        if (type == 1){
            map.put("status", 1);
            list = sysDictService.queryAll(map);
            sysCacheService.saveDictRedis(list,"dictList","MTM_CACHE:IMMELISTALL:DICTLIST");
        };
        if (type == 2){
            list = orgService.list(new QueryWrapper<SysOrgEntity>().eq("STATUS", 1));
            sysCacheService.saveDictRedis(list,"orgList","MTM_CACHE:ORGLISTALL:ORGLIST");
        };
        if (type == 3){
            list = userService.list(new QueryWrapper<SysUserEntity>().select("USER_ID,REAL_NAME,ORG_NO,status"));
            sysCacheService.saveDictRedis(list,"userList","MTM_CACHE:USERLISTALL:USERS");
        };
        if (type == 4){
            list = sysMenuService.queryListRedis();
            sysCacheService.saveDictRedis(list,"userRoleList","MTM_CACHE:IMMELISTALL:USERROLELIST");
        };
        logger.info("存在序列化失败问题：类型：" + type);
        return list;
    }
    /**
     * 所有菜单列表
     *
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public RestResponse list() {
        List<SysMenuEntity> menuList = sysMenuService.queryList();
        return RestResponse.success().put("menuList", menuList);
    }

    /**
     * 选择菜单(添加、修改菜单)
     *
     * @return RestResponse
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public RestResponse select() {
        //查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

        //添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setMenuId("0");
        root.setName("一级菜单");
        root.setParentId("-1");
        root.setOpen(true);
        menuList.add(root);

        return RestResponse.success().put("menuList", menuList);
    }

    /**
     * 根据主键查询详情
     *
     * @param menuId 主键
     * @return RestResponse
     */
    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public RestResponse info(@PathVariable("menuId") String menuId) {
        SysMenuEntity menu = sysMenuService.getById(menuId);
        return RestResponse.success().put("menu", menu);
    }

    /**
     * 保存
     *
     * @param menu menu
     * @return RestResponse
     */
    @SysLog("保存菜单")
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public RestResponse save(@RequestBody SysMenuEntity menu) {
        ValidatorUtils.validateEntity(menu, AddGroup.class);
        //数据校验
        verifyForm(menu);

        sysMenuService.add(menu);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param menu menu
     * @return RestResponse
     */
    @SysLog("修改菜单")
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public RestResponse update(@RequestBody SysMenuEntity menu) {
        ValidatorUtils.validateEntity(menu, UpdateGroup.class);
        //数据校验
        verifyForm(menu);

        sysMenuService.updateById(menu);

        return RestResponse.success();
    }

    /**
     * 删除
     *
     * @param menuId 主键
     * @return RestResponse
     */
    @SysLog("删除菜单")
    @PostMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public RestResponse delete(@PathVariable("menuId") String menuId) {

        //判断是否有子菜单或按钮
        List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
        if (menuList.size() > 0) {
            return RestResponse.error("请先删除子菜单或按钮");
        }

        sysMenuService.delete(menuId);

        return RestResponse.success();
    }

    /**
     * 验证参数是否正确
     *
     * @param menu menu
     */
    private void verifyForm(SysMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new BusinessException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new BusinessException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new BusinessException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (!Constant.STR_ZERO.equals(menu.getParentId())) {
            SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new BusinessException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new BusinessException("上级菜单只能为菜单类型");
            }
        }
    }



//            if (username!=null&&!username.contains("admin")&&!perms.contains(":info")&&!perms.contains(":list")) { //非管理员 查询 权限对应部门
//        String pstr = (String) orgMaps.get(perms);
//        Set<String> permsSet = new HashSet<>();
//        permsSet.add(perms+userId);
//        String[] pstrmap = pstr.split(";");
//        //菜单部门
//        if (pstrmap.length>0) {
//            Set<String> orgNos = new HashSet<>();
//            Integer noselectdept= Integer.parseInt(pstrmap[0]);
//            orgNos = ContextHelper.setSearchDepts(noselectdept,pstrmap[1],getOrgNo());
//            String orgs = "";
//            if (!orgNos.isEmpty()) {
//                orgs = StringUtils.join(orgNos.toArray(), ",");
//            }
//            permsSet.add(perms+orgs+";");
//        }
//        permissions.addAll(permsSet);
//    }
}
