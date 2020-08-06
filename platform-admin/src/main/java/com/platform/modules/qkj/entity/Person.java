package com.platform.modules.qkj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("s_sys_user")
public class Person {
    private String name;
    private String tel;
    private String email;
}
