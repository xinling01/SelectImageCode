package com.linger.selectimagecode.code;

import java.util.List;

/**
 * 作者：linger
 * 功能：
 * 创建日期：2021/4/6
 */
public class CodeList {
    private List<Code> codeList;   //储存所有验证码的数据
    private List<CodeType> typeList;  //所有类型的集合

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }

    public List<CodeType> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<CodeType> typeList) {
        this.typeList = typeList;
    }

}
