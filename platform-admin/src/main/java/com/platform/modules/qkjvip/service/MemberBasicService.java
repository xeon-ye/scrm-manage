/*
 * 项目名称:platform-plus
 * 类名称:MemberBasicService.java
 * 包名称:com.platform.modules.qkjvip.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/8/7 15:48            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkjvip.entity.MemberActiveEntity;
import com.platform.modules.qkjvip.entity.MemberBasicEntity;
import com.platform.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * MemberBasicService
 *
 * @author liuqianru
 * @date 2020/8/7 15:48
 */
public interface MemberBasicService extends IService<MemberBasicEntity> {
    /**
     * 查询所有
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberBasicEntity> queryAll(Map<String, Object> params);
    /**
     * saveBatch
     *
     * @param mbList mbList
     */
    void saveBatch(List<MemberBasicEntity> mbList);

    /**
     * 批量修改会员
     *
     * @param mbList mbList
     */
    void updateBatchByMobile(List<MemberBasicEntity> mbList);
}
