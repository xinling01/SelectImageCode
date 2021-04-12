package com.linger.selectimagecode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linger.selectimagecode.code.Code;
import com.linger.selectimagecode.code.CodeDataUtils;
import com.linger.selectimagecode.code.CodeList;
import com.linger.selectimagecode.code.CodeType;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;          //提示输入内容
    @BindView(R.id.reset)
    ImageButton reset;       //重置验证码

    @BindViews(value = {R.id.image_1,R.id.image_2,R.id.image_3,R.id.image_4,R.id.image_5,R.id.image_6,R.id.image_7,R.id.image_8})
    List<ImageView> imageList;  //验证码选择
    @BindView(R.id.login)
    Button login;     //登录验证
	private boolean hasData;  //是否有数据 
    private List<Code> allCode;  //验证码集合
    private List<CodeType> typeCode;  //所有验证码类型的数据集合
	private String codeName;//验证码条件
    private ArrayList<String> selectCode;  //存储验证信息的集合
    private CompositeSubscription subscription=new CompositeSubscription();
    private int bgColor[]={0xffff0000,0xff000000};     //红色，黑色
    private Random mRandom=new Random(); //随机数生成器
    private ArrayList<Integer> showImageList = new ArrayList<>();//筛选数据集合
    private ArrayList<String> codeString = new ArrayList<>();//验证字符串
    private String[] codeNumberInAll = new String[2];//用于保存验证值
    private ArrayList<Code> showCodeList = new ArrayList<>();//用于显示的验证图片
    private ArrayList<Integer> indexRecordList = new ArrayList<>();//用于记录标记位
    private boolean isSelected[]=new boolean[8]; //保存选择状态

    /**
     * 初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*CodeList list=CodeDataUtils.getDataList(this);
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
		  }}*/
		  getImageCodeData();    //获取数据源
		  }
		  
		  private void addCodeImageToWindow() {
        //获取随机类别
        int typeNum=mRandom.nextInt(typeCode.size());
        CodeType type=typeCode.get(typeNum);  //获取类别
        codeName = type.getType();//获取验证码条件
        text.setText(Html.fromHtml("请点击下面所有的："+
                "<font color = '#ff0000'>"
				
				+codeName+"</font>"));
        saveCodeMessage(type.getCode());
        getCodeImageFromAll();
        setCodeShowIndex();  //设置显示的图片
        showImageInWindow();  //显示图片在界面中
    }
/**
     * 设置验证码选择器
     */
    private void showImageInWindow() {
        for (int i = 0; i <8 ; i++) {
            imageList.get(i).setImageBitmap(showCodeList.get(i).getBitmap());
        }
    }
/**
     * 设置图片的显示位置（打乱验证码选图片的排列顺序 )
     */
    private void setCodeShowIndex() {
        showCodeList.clear();
        indexRecordList.clear();
        int count=0;
        while (count<8){
            int index=mRandom.nextInt(8);
            if(detection(showImageList.get(index),indexRecordList)){
                indexRecordList.add(showImageList.get(index));
                count++;
            }
        }
        /**
         * 生成最后的验证数据
         */
        for (int i = 0; i <8 ; i++) {
            showCodeList.add(allCode.get(indexRecordList.get(i)));
        }
    }
 /**
     * 获取数据添加到显示验证码效果中
     */
    private void getCodeImageFromAll() {
        int count=0;
        while (count<6){
            int num=mRandom.nextInt(24);
            if(detection(num,showImageList)){  //没有这个数据
                showImageList.add(num);
                count++;
            }
        }
    }

    /**
     * 验证数据是否有效
     * @param num
     * @param list
     * @return
     */
    private boolean detection(int num, ArrayList<Integer> list) {
        for (int i = 0; i <list.size() ; i++) {
                if(num==list.get(i))return false;
        }
        return true;
    }
	/**
     * 保存验证数据
     */
    private void saveCodeMessage(List<Code> typeCodeList) {
        codeString.clear();
        showImageList.clear();
        for (int i = 0; i <typeCodeList.size() ; i++) {
            Code code=typeCodeList.get(i);
            showImageList.add(code.getId());   //总长度最后就是8
            codeNumberInAll[i]=String.valueOf(code.getId());  //存入值
        }
        //拼接验证码
        codeString.add(codeNumberInAll[0]+codeNumberInAll[1]);  //01
        codeString.add(codeNumberInAll[1]+codeNumberInAll[0]);  //10
    }

    /**
     *  获取数据源
     */
    private void getImageCodeData() {
        Observable<CodeList> codeListObservable =
                Observable.create(subscriber -> {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(CodeDataUtils.getDataList(this));
                    }
                });

        Subscription codeListSubscription = codeListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(codeList -> {
                    if (codeList != null) {
                        allCode = codeList.getCodeList();//获取数据
                        typeCode = codeList.getTypeList();//获取类别
                        Logger.t("图片总数据数量").i(allCode.size() + "");
                        Logger.t("图片类别数量").i(typeCode.size() + "");
                        hasData = true;//标识数据以获取
                    } else {
                        hasData = false;//数据获取失败
                        //showToast("初始化数据失败");
                    }
                    selectCode = new ArrayList<>();//保存验证数据
                    imageViewBackground();//初始化图片的背景
                    addCodeImageToWindow();//添加验证图片
                });
        subscription.add(codeListSubscription);
    }

    

    

    

    

    /**
     * 设置图片选择器的背景颜色
     */
    private void imageViewBackground() {
        for (int i = 0; i < imageList.size(); i++) {
            imageList.get(i).setBackgroundColor(bgColor[1]);
        }
    }

    /**
     *登录验证
     */
    @OnClick(R.id.login)
    public void onClick(){
        if(selectCode.size()>0){
            StringBuilder sb=new StringBuilder();
            for (int i = 0; i <selectCode.size() ; i++) {
                sb.append(showCodeList.get(Integer.parseInt(selectCode.get(i))).getId());
            }
            for (int i = 0; i <codeString.size() ; i++) {
                if(TextUtils.equals(sb.toString(),codeString.get(i))){
                    showToast("登录成功");
                    return;
                }
            }
            showToast("登录失败。验证码有误！");
        }else {
            showToast("请输入验证码！");
        }
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

    }

    /**
     * 重置验证码
     */
    @OnClick(R.id.reset)
    public void setReset(){
        addCodeImageToWindow();
        resetImageState();
        selectCode.clear();
    }

    private void resetImageState() {
        for (int i = 0; i <isSelected.length ; i++) {
            isSelected[i]=false;
        }
        imageViewBackground();//设置图片的背景色
    }

    /**
     * 验证码选择
      * @param view
     */
    @OnClick(value = {R.id.image_1,R.id.image_2,R.id.image_3,R.id.image_4,R.id.image_5,R.id.image_6,R.id.image_7,R.id.image_8})
    public void selectCode(View view){
        int index=0;
        switch (view.getId()){
            case R.id.image_1:
                index=0;
                break;
            case R.id.image_2:
                index=1;
                break;
            case R.id.image_3:
                index=2;
                break;
            case R.id.image_4:
                index=3;
                break;
            case R.id.image_5:
                index=4;
                break;
            case R.id.image_6:
                index=5;
                break;
            case R.id.image_7:
                index=6;
                break;
            case R.id.image_8:
                index=7;
                break;
        }
        //保存选中的状态
        setImageViewState(index);
    }

    /**
     * 设置选择状态
     * @param index
     */
    private void setImageViewState(int index) {
        boolean imageState=isSelected[index]?false:true;
        isSelected[index]=imageState;
        int backgroundColor;
        if(isSelected[index]){
            backgroundColor=bgColor[0]; //红色背景
            selectCode.add(String.valueOf(index));
        }else {
            backgroundColor=bgColor[1];  //黑色背景
            selectCode.remove(String.valueOf(index));
        }
        //向对应的图片设置背景
        imageList.get(index).setBackgroundColor(backgroundColor);
    }

    @Override
    protected void onDestroy() {
        subscription.unsubscribe();
        super.onDestroy();
    }

   

}
