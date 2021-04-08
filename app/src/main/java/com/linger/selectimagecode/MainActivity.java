package com.linger.selectimagecode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.linger.selectimagecode.code.Code;
import com.linger.selectimagecode.code.CodeDataUtils;
import com.linger.selectimagecode.code.CodeList;
import com.linger.selectimagecode.code.CodeType;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CodeList list=CodeDataUtils.getDataList(this);
        List<Code> codeList=list.getCodeList();
        for(Code code:codeList){
            Log.e("code","标记="+code.getTag()+"\t\tid="+code.getId());
        }
        List<CodeType>types=list.getTypeList();
        for (int i = 0; i < types.size(); i++) {
           CodeType codeType=types.get(i);
           Log.e("类别"+(i+1),codeType.getType());
            for (int j = 0; j <codeType.getCode().size() ; j++) {
                Log.e("code"+(j+1),codeType.getCode().get(j).getId()+"");
            }
        }
    }
}
