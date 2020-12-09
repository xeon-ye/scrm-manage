/*
 * 项目名称:platform-plus
 * 类名称:SysUserController.java
 * 包名称:com.platform.modules.sys.controller
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2018/11/21 16:04    李鹏军      初版完成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.AbstractAssert;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.AddGroup;
import com.platform.common.validator.group.UpdateGroup;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.form.PasswordForm;
import com.platform.modules.sys.service.SysUserChannelService;
import com.platform.modules.sys.service.SysUserRoleService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.modules.webservices.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 系统用户
 *
 * @author 李鹏军
 */
@RestController
@RequestMapping("/sys/user")
@Component("quartzuser")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserChannelService sysUserChannelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("sys:dict:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<SysUserEntity> list = sysUserService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 所有用户列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {

        //如需数据权限，在参数中添加DataScope
//        params.put("dataScope", getDataScope());  //liuqianru del 人员只有管理员有权限操作，所以无需数据权限

        Page page = sysUserService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 获取登录的用户信息
     *
     * @return RestResponse
     */
    @GetMapping("/info")
    public RestResponse info() {
        return RestResponse.success().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     *
     * @param form form
     * @return RestResponse
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    public RestResponse password(@RequestBody PasswordForm form) {
        AbstractAssert.isBlank(form.getNewPassword(), "新密码不为能空");

        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return RestResponse.error("原密码不正确");
        }

        return RestResponse.success();
    }

    /**
     * 根据主键查询详情
     *
     * @param userId 主键
     * @return RestResponse
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public RestResponse info(@PathVariable("userId") String userId) {
        SysUserEntity user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<String> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        //获取用户所属的渠道列表
        List<String> channelIdlList = sysUserChannelService.queryChannelIdList(userId);
        user.setChannelIdList(channelIdlList);

        return RestResponse.success().put("user", user);
    }

    /**
     * 保存用户
     *
     * @param user user
     * @return RestResponse
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public RestResponse save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        user.setCreateUserId(getUserId());
        user.setCreateUserOrgNo(getOrgNo());
        sysUserService.add(user, params);

        return RestResponse.success();
    }

    /**
     * 修改用户
     *
     * @param user user
     * @return RestResponse
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public RestResponse update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        user.setCreateUserId(getUserId());
        user.setCreateUserOrgNo(getOrgNo());
        sysUserService.update(user, params);

        return RestResponse.success();
    }

    /**
     * 删除用户
     *
     * @param userIds userIds
     * @return RestResponse
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public RestResponse delete(@RequestBody String[] userIds) {
        if (ArrayUtils.contains(userIds, Constant.SUPER_ADMIN) || ArrayUtils.contains(userIds, Constant.SUPER_ADMIN2) || ArrayUtils.contains(userIds, Constant.SUPER_ADMIN3)) {
            return RestResponse.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return RestResponse.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return RestResponse.success();
    }

    /**
     * 重置密码
     *
     * @param userIds userIds
     * @return RestResponse
     */
    @SysLog("重置密码")
    @PostMapping("/resetPw")
    @RequiresPermissions("sys:user:resetPw")
    public RestResponse resetPw(@RequestBody String[] userIds) {
        if (ArrayUtils.contains(userIds, Constant.SUPER_ADMIN) || ArrayUtils.contains(userIds, Constant.SUPER_ADMIN2) || ArrayUtils.contains(userIds, Constant.SUPER_ADMIN3)) {
            return RestResponse.error("系统管理员不能重置");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return RestResponse.error("当前用户不能重置");
        }

        sysUserService.resetPw(userIds);

        return RestResponse.success();
    }

    /**
     * 人员定时任务
     *
     */
    @SysLog("人员定时任务")
    @RequestMapping("/quartzUser")
    public void quartzUser() {
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("status", 1);
        List<SysUserEntity> users = sysUserService.queryAll(params);  //表中寄存的用户
        HrmService service = new HrmService();
        try {
            HrmServicePortType clien = service.getHrmServiceHttpPort();
            ArrayOfUserBean arrayUser = clien.getHrmUserInfo("221.207.54.67", "", "", "","", "");
            List<UserBean> oaUsers = arrayUser.getUserBean();  //OA的人员
            List<SysUserEntity> sysUsers = new ArrayList<>();
            // 将crm系统存在但oa不存在的人员冻结
            this.freezeUser(users, oaUsers);
            for (UserBean oauser:oaUsers) {
                Boolean flag = true; //用户是否在crm存在标识
                for (SysUserEntity user:users) {
                    if(oauser.getAccounttype() == 0) {
                        if (user.getOaId() != null && user.getOaId().equals(oauser.getUserid().toString()) && (oauser.getLoginid().getValue()!=null && !"".equals(oauser.getLoginid().getValue()))) {
                            if (Integer.parseInt(oauser.getStatus().getValue()) < 4) {  //正常在职员工
                                //修改人员信息
                                user.setSex((oauser.getSex().getValue()==null || "".equals(oauser.getSex().getValue())) ? 3 : ("男".equals(oauser.getSex().getValue()) ? 1 : 2));
                                user.setRealName(oauser.getLastname().getValue());
                                user.setUserName(oauser.getLoginid().getValue());
                                user.setOrgNo(oauser.getDepartmentid().getValue());
                                user.setEmail(oauser.getEmail().getValue());
                                user.setMobile(oauser.getMobile().getValue());
                                user.setStatus(1);
//                                user.setCreateUserId(getUserId());
//                                user.setCreateUserOrgNo(getOrgNo());
                                user.setCreateTime(new Date());
                                user.setDingId(oauser.getHomeaddress().getValue());
                                sysUsers.add(user);
                                System.out.println("人员：" + oauser.getLastname().getValue() + "已修改");
                            } else {  //离职、退休、无效的员工
                                //修改状态为禁用
                                user.setStatus(0);
                                sysUserService.quartzUpdate(user);
                                System.out.println("人员：" + oauser.getLastname().getValue() + "已修改为禁用");
                            }
                            flag = false;
                            break;
                        }
                    }
                }

                if(flag == true && oauser.getAccounttype() == 0 && Integer.parseInt(oauser.getStatus().getValue()) < 4 && (oauser.getLoginid().getValue()!=null && !"".equals(oauser.getLoginid().getValue()))) {//添加
                    SysUserEntity sysUser = new SysUserEntity();
                    sysUser.setSex((oauser.getSex().getValue()==null || "".equals(oauser.getSex().getValue())) ? 3 : ("男".equals(oauser.getSex().getValue()) ? 1 : 2));
                    sysUser.setRealName(oauser.getLastname().getValue());
                    sysUser.setUserName(oauser.getLoginid().getValue());
                    sysUser.setOrgNo(oauser.getDepartmentid().getValue());
                    sysUser.setEmail(oauser.getEmail().getValue());
                    sysUser.setMobile(oauser.getMobile().getValue());
                    sysUser.setStatus(1);
//                    sysUser.setCreateUserId(getUserId());
//                    sysUser.setCreateUserOrgNo(getOrgNo());
                    sysUser.setDingId(oauser.getHomeaddress().getValue());
                    sysUser.setOaId(oauser.getUserid().toString());
                    sysUserService.add(sysUser);
                    System.out.println("人员：" + oauser.getLastname().getValue() + "已添加");
                }
            }
            sysUserService.quartzBatchUpdate(sysUsers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void freezeUser (List<SysUserEntity> users, List<UserBean> oaUsers) {
        for(SysUserEntity user:users){
            Boolean flag = true;  //用户是否在oa存在标识true不存在
            for (UserBean oauser:oaUsers) {
                if((user.getOaId() != null && user.getOaId().equals(oauser.getUserid().toString())) || user.getUserName().contains("admin")) {//存在
                    flag=false;
                    break;
                }
            }
            if(flag == true){
                user.setStatus(0);
                sysUserService.quartzUpdate(user);
                System.out.println("人员：" + user.getRealName() + "已修改为禁用");
            }
        }
    }
}
