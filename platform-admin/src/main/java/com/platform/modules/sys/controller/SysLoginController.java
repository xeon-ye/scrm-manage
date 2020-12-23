/*
 * 项目名称:platform-plus
 * 类名称:SysLoginController.java
 * 包名称:com.platform.modules.sys.controller
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2018/11/21 16:04    李鹏军      初版完成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.sys.controller;

import com.platform.common.annotation.SysLog;
import com.platform.common.utils.Constant;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.form.SysLoginBindForm;
import com.platform.modules.sys.form.SysLoginForm;
import com.platform.modules.sys.service.SysCaptchaService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.modules.sys.service.SysUserTokenService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 *
 * @author 李鹏军
 */
@RestController
public class SysLoginController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysCaptchaService sysCaptchaService;

    /**
     * 验证码
     *
     * @param response response
     * @param uuid     uuid
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     *
     * @param form 登录表单
     * @return RestResponse
     */
    @SysLog("登录")
    @PostMapping("/sys/login")
    public RestResponse login(@RequestBody SysLoginForm form) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return RestResponse.error("验证码不正确");
        }

        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUserName());

        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return RestResponse.error("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return RestResponse.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        String token = sysUserTokenService.createToken(user.getUserId());

        return RestResponse.success().put("token", token).put("expire", Constant.EXPIRE);
    }

    /**
     * 登录
     *
     * @param form 登录表单
     * @return RestResponse
     */
    @SysLog("公众号登录")
    @PostMapping("/sys/checkuser")
    public RestResponse checkuser(@RequestBody SysLoginBindForm form) {
        //用户信息
        SysUserEntity user = sysUserService.queryByOpenid(form.getOpenid());

        //账号不存在
        if (user == null) {
            return RestResponse.error("账号不存在");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return RestResponse.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        String token = sysUserTokenService.createToken(user.getUserId());

        return RestResponse.success().put("token", token).put("expire", Constant.EXPIRE);
    }

    /**
     * 登录
     *
     * @param form 登录表单
     * @return RestResponse
     */
    @SysLog("绑定openid")
    @PostMapping("/sys/loginbind")
    public RestResponse loginbind(@RequestBody SysLoginBindForm form) {
        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUserName());

        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return RestResponse.error("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return RestResponse.error("账号已被锁定,请联系管理员");
        }

        //保存业务员的unionid和openid
        user.setUnionid(form.getUnionid());
        user.setOpenid(form.getOpenid());
        sysUserService.update(user);

        //生成token，并保存到数据库
//        String token = sysUserTokenService.createToken(user.getUserId());

        return RestResponse.success();
    }


    @SysLog("登录2")
    @PostMapping("/sys/login2")
    public RestResponse login2(@RequestBody SysLoginForm form) {
        //用户信息
        String loginsingid=form.getOaId();
        List<SysUserEntity> list=new ArrayList<>();
        if(loginsingid!=null){
            Map<String, Object> params=new HashMap<String, Object>();
            params.put("oaId",loginsingid);
            list = sysUserService.queryAll(params);
        }else{
            return RestResponse.error("您无权限登录谢谢");
        }
        SysUserEntity user=new SysUserEntity();
        if(list.size()>0){
            user=list.get(0);
        }else{
            return RestResponse.error("您无权限登录谢谢");
        }
        //账号锁定
        if (user.getStatus() == 0) {
            return RestResponse.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        String token = sysUserTokenService.createToken(user.getUserId());

        return RestResponse.success().put("token", token).put("expire", Constant.EXPIRE);
    }


    /**
     * 退出系统
     *
     * @return RestResponse
     */
    @PostMapping("/sys/logout")
    public RestResponse logout() {
        sysUserTokenService.logout(getUserId());
        return RestResponse.success();
    }

}
