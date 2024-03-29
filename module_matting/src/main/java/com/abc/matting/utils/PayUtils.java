package com.abc.matting.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import com.abc.matting.Constants;
import com.abc.matting.bean.PayBean;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.UserBean;
import com.feisukj.base.util.GsonUtils;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.PackageUtils;
import com.feisukj.base.util.SPUtil;
import com.just.agentweb.AgentWeb;

public class PayUtils {

    /**
     * 生成订单号
     * */
    public static String getTrade(PayBean bean, String payType){
        //VIP1_101029_HUAWEI_ALI_27759
        String channel = PackageUtils.getAppMetaData(BaseApplication.application,"CHANNEL");
        String str = channel.substring(channel.indexOf("_")+1);
        if(str.equals("360")) {
            str = "SLL";
        }
        str = str.toUpperCase();
        LogUtils.INSTANCE.e("渠道号--------->"+str);
        int id = GsonUtils.parseObject(SPUtil.getInstance().getString(Constants.USER_BEAN), UserBean.class).getData().getId();
        return bean.getVipType()+"_"+id+"_"+str+"_"+payType+"_"+getRadom(100000);
    }

    /**
     * 生成随机数
     * */
    public static int getRadom(int i){
        return (int)(Math.random()*i);
    }

    /**
     * 支付
     * */
    public static void payByWXOrALI(Context context, LinearLayout view, PayBean bean, String payType){
        String url = "";
        String trade = "";
        switch (payType){
            case Constants.PAY_TYPE_WX:     //微信支付
                trade = getTrade(bean, Constants.PAY_TYPE_WX);
                url = Constants.PAY_WX_URL;
                break;
            case Constants.PAY_TYPE_ALI:    //支付宝支付
                trade = getTrade(bean, Constants.PAY_TYPE_ALI);
                url = Constants.PAY_ALI_URL;
                break;
        }
        url += "trade="+trade+"&subject="+bean.getSubject()+"&price="+bean.getMoney()+"&body="+bean.getBody();

        LogUtils.INSTANCE.e("支付网址："+url);
        AgentWeb mAgentWeb = AgentWeb.with((Activity) context)//传入Activity
                .setAgentWebParent(view, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
//                .defaultProgressBarColor() // 使用默认进度条颜色
//                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(url);

//        Intent intent = new Intent(context, WebViewActivity.class);
//        intent.putExtra(WebViewActivity.URL_KEY,url);
//        intent.putExtra("type","pay");
//        intent.putExtra("payType",payType);
//        context.startActivity(intent);
    }
}

//http://www.aisou.club/api.php?service=passport.deleteUser&id=
