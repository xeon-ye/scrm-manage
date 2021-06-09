/*
 * 项目名称:platform-plus
 * 类名称:UploadData.java
 * 包名称:com.platform.modules.oss.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021/6/9 14:28            liuqianru    初版做成
 *
 */
package com.platform.modules.oss.entity;

import lombok.Data;

/**
 * UploadData
 * 上传文件参数
 * @author liuqianru
 * @date 2021/6/9 14:28
 */
@Data
public class UploadData {
    /**
     * 是否忽略非必填项check
     */
    private Boolean ischeckpass;
}
