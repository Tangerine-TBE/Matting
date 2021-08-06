package com.abc.matting.utils;

import android.content.Context;
import android.widget.Toast;

import com.abc.matting.Constants;
import com.abc.matting.bean.DataBean;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.UserBean;
import com.feisukj.base.util.GsonUtils;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.PackageUtils;
import com.feisukj.base.util.SPUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * 自定义监听器实现IUiListener接口后，需要实现的3个方法
 * onComplete完成 onError错误 onCancel取消
 */
public class BaseUiListener implements IUiListener {

    private Tencent mTencent;
    private UserInfo mUserInfo;
    private Context mContext;
    final Map<String,String> map = new TreeMap<>();

    public BaseUiListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onComplete(Object response) {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext);
        Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
        LogUtils.INSTANCE.e("response:" + response);
        JSONObject obj = (JSONObject) response;
        try {
            final String openID = obj.getString("openid");
            String accessToken = obj.getString("access_token");
            String expires = obj.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(accessToken, expires);
            QQToken qqToken = mTencent.getQQToken();
            mUserInfo = new UserInfo(mContext, qqToken);
            mUserInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    //是一个json串response.tostring，直接使用gson解析就好
                    LogUtils.INSTANCE.e( "登录成功" + response.toString());
                    //登录成功后进行Gson解析即可获得你需要的QQ头像和昵称
                    // Nickname  昵称
                    //Figureurl_qq_1 //头像
                    JSONObject object = (JSONObject) response;
                    String name = "";
                    try {
                        name = object.getString("nickname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SPUtil.getInstance().putString(Constants.OTHER_NAME,name);
                    checkThird(openID);
                }

                @Override
                public void onError(UiError uiError) {
                    LogUtils.INSTANCE.e("登录失败" + uiError.toString());
                }

                @Override
                public void onCancel() {
                    LogUtils.INSTANCE.e( "登录取消");

                }

                @Override
                public void onWarning(int i) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {
        Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onWarning(int i) {

    }

    private void checkThird(String openId) {
        SPUtil.getInstance().putString(Constants.OPENID,openId);
        SPUtil.getInstance().putString(Constants.LOGIN_TYPE,"1");
        map.put("openId",openId);
        map.put("type","1");
        //判断是否注册
        HttpUtils.getData(map, Constants.CHECK_THIRD, new HttpUtils.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.INSTANCE.e("验证QQ是否注册"+response);
                //如果没有注册
                try{
                    if(GsonUtils.parseObject(response, DataBean.class).getData().equals("0")){
                        map.put("platform", PackageUtils.getAppMetaData(BaseApplication.application,"CHANNEL"));
                        map.put("package",mContext.getApplicationInfo().processName);
                        //注册
                        HttpUtils.getData(map, Constants.REGISTER_BY_THIRD, new HttpUtils.RequestCallback() {
                            @Override
                            public void onSuccess(String response) {
                                LogUtils.INSTANCE.e("QQ注册----->"+response);
                                map.remove("platform");
                                map.remove("package");
                                String str ;
                                try{
                                    str = GsonUtils.parseObject(response, UserBean.class).getMsg();
                                }catch (Exception e){
                                    str = GsonUtils.parseObject(response, DataBean.class).getMsg();
                                }
                                //注册成功
                                if(str.equals("OK")){
                                    //登录
                                    wxLogin();
                                }
                            }

                            @Override
                            public void onFailure(String msg, Exception e) {

                            }
                        });
                    }else if(GsonUtils.parseObject(response, DataBean.class).getData().equals("1")){
                        //已注册，登录
                        wxLogin();
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(String msg, Exception e) {

            }
        });
    }

    /**
     * QQ登录
     * */
    private void wxLogin(){
        //注册成功后登录
        HttpUtils.getData(map, Constants.LOGIN_THIRD, new HttpUtils.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.INSTANCE.e("第三方登录：------------->"+response);
                try{
                    String str ;
                    try{
                        str = GsonUtils.parseObject(response, UserBean.class).getMsg();
                    }catch (Exception e){
                        str = GsonUtils.parseObject(response, DataBean.class).getMsg();
                    }
                    if(str.equals("OK")){
                        SPUtil.getInstance().putString(Constants.USER_BEAN,response);
                        SPUtil.getInstance().putBoolean(Constants.IS_LOGIN_THIRD,true);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(String msg, Exception e) {

            }
        });
    }

}
