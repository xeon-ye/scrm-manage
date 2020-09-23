/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberFansServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-09 14:02:22        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.modules.quartz.dao.QrtzMemberFansUpdateTimeDao;
import com.platform.modules.quartz.entity.QrtzMemberFansUpdateTimeEntity;
import com.platform.modules.quartz.service.QrtzMemberFansUpdateTimeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author liuqianru
 * @date 2020-09-09 14:02:22
 */
@Service("qrtzMemberFansUpdateTimeService")
public class QrtzMemberFansUpdateTimeServiceImpl extends ServiceImpl<QrtzMemberFansUpdateTimeDao, QrtzMemberFansUpdateTimeEntity> implements QrtzMemberFansUpdateTimeService {

    @Override
    public List<QrtzMemberFansUpdateTimeEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public void update(QrtzMemberFansUpdateTimeEntity fansUpdateTimeEntity) {
        this.updateById(fansUpdateTimeEntity);
    }
}
