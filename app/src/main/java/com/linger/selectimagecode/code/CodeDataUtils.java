package com.linger.selectimagecode.code;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：linger
 * 功能：验证码数据源生成工具
 * 创建日期：2021/4/6
 */
public class CodeDataUtils {
    private static String[] tagName={
            "鼠","牛","虎","兔",
            "蛇","龙","马","羊",
            "猴","鸡","狗","猪"
    };
    private static String[] resName={
            "shu","niu","hu","tu",
            "she","long","ma","yang",
            "hou","ji","gou","zhu"
    };

    /**
     * 生成数据源
     * @param context
     * @return
     */
    public static CodeList getDataList(Context context){
        CodeList codeList=new CodeList();
        List<Code> all=new ArrayList<>();  //总数
        List<CodeType> type=new ArrayList<>();  //类型总数
        Resources resources=context.getApplicationContext().getResources();
        String packageName=context.getPackageName();//包名
        int count=0;   //计数
        for (int i = 0; i < tagName.length; i++) {
            List<Code> codes=new ArrayList<>();
            for (int j = 0; j <2 ; j++) {
                //获取指定资源ID
                int resID=resources.getIdentifier(resName[i]+(j+1),"drawable",packageName);
                //获取图片
                Bitmap bitmap=BitmapFactory.decodeResource(resources,resID);
                Code code=new Code(bitmap,tagName[i],count);
                codes.add(code);
                all.add(code);
                count++;
            }
            CodeType codeType=new CodeType();  //类别
            codeType.setType(tagName[i]);
            codeType.setCode(codes);
            type.add(codeType);  // 存入类别
        }
        codeList.setCodeList(all);
        codeList.setTypeList(type);//类型集合
        return codeList;
    }
}
