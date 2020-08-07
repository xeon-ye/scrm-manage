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
import com.platform.modules.qkjvip.entity.MemberBasicEntity;

import java.util.List;

/**
 * MemberBasicService
 *
 * @author liuqianru
 * @date 2020/8/7 15:48
 */
public interface MemberBasicService extends IService<MemberBasicEntity> {
    /**
     * saveOrUpdate
     *
     * @param mbList mbList
     */
    void saveOrUpdate(List<MemberBasicEntity> mbList);
}
