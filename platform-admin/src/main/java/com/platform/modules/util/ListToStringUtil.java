/*
 * 项目名称:platform-plus
 * 类名称:ListToStringUtil.java
 * 包名称:com.platform.modules.util
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/12/22 16:46            liuqianru    初版做成
 *
 */
package com.platform.modules.util;

import com.platform.common.utils.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * ListToStringUtil
 *
 * @author liuqianru
 * @date 2020/12/22 16:46
 */
public class ListToStringUtil {
    public static String listToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        String resultStr = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                sb.append("'" + list.get(i) + "',");
            }
            resultStr = sb.toString();
            resultStr = "(" + resultStr.substring(0, resultStr.length() - 1) + ")";
        }
        return resultStr;
    }

    public static String attrToString(String[] attrStr) {
        StringBuffer sb = new StringBuffer();
        String resultStr = "";
        if (attrStr.length > 0) {
            for (int i = 0; i < attrStr.length; i++) {
                sb.append(attrStr[i] + ",");
            }
            resultStr = sb.toString();
            resultStr = resultStr.substring(0, resultStr.length() - 1);
        }
        return resultStr;
    }
}
