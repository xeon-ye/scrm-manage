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
    /**
     * 拼接sql in后的逗号隔开的字符串用
     * @param list
     * @return
     */
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

    /**
     * list类型转分隔符隔开的字符串
     * @param list
     * @param separator 分隔符
     * @return
     */
    public static String listToString(List<String> list, String separator) {
        StringBuffer sb = new StringBuffer();
        String resultStr = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i) + separator);
            }
            resultStr = sb.toString();
            resultStr = resultStr.substring(0, resultStr.length() - separator.length());
        }
        return resultStr;
    }

    /**
     * 数组类型转分隔符隔开的字符串
     * @param attrStr
     * @return
     */
    public static String attrToString(String[] attrStr, String separator) {
        StringBuffer sb = new StringBuffer();
        String resultStr = "";
        if (attrStr.length > 0) {
            for (int i = 0; i < attrStr.length; i++) {
                sb.append(attrStr[i] + separator);
            }
            resultStr = sb.toString();
            resultStr = resultStr.substring(0, resultStr.length() - separator.length());
        }
        return resultStr;
    }
}
