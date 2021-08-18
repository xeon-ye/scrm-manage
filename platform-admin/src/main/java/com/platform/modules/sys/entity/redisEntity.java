package com.platform.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class redisEntity implements Serializable {
    private String key;
    private List entityList;
}
