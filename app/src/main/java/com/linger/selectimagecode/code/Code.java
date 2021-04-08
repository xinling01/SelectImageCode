package com.linger.selectimagecode.code;

import android.graphics.Bitmap;

/**
 * 作者：linger
 * 功能：储存验证码详细的类
 * 创建日期：2021/4/6
 */
public class Code {
    private Bitmap bitmap;  //图片
    private String tag;    //标记
    private int id;

    public Code(Bitmap bitmap, String tag, int id) {
        this.bitmap = bitmap;
        this.tag = tag;
		this.id=id;
}
 public int getId() {
        return id;
    }

    public void setId(int id) {
        
    
      this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

   
}
