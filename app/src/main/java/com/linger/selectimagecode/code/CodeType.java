package com.linger.selectimagecode.code;

import java.util.List;

/**
 * 作者：linger
 * 功能：验证码类别
 * 创建日期：2021/4/6
 */
public class CodeType {
    private List<Code> code;//一个类别中的数据
    private String type;  //类别的名称

    public List<Code> getCode() {
        return code;
    }

    public void setCode(List<Code> code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
