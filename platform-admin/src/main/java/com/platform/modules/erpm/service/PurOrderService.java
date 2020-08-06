/*
 * 项目名称:platform-plus
 * 类名称:PurOrderService.java
 * 包名称:com.platform.modules.erpm.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-24 17:54:49        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.erpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.erpm.entity.PurOrderEntity;

import java.util.List;
import java.util.Map;

/**
 * 记录Service接口
 *
 * @author 孙珊珊
 * @date 2019-10-24 17:54:49
 */
public interface PurOrderService extends IService<PurOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PurOrderEntity> queryAll(Map<String, Object> params);


}
