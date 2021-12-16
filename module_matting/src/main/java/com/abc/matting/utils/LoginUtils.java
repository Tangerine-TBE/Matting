package com.abc.matting.utils;

import com.abc.matting.Constants;
import com.feisukj.base.bean.UserBean;
import com.feisukj.base.util.GsonUtils;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.MD5Utlis;
import com.feisukj.base.util.RegularUtils;
import com.feisukj.base.util.SPUtil;

import java.util.Map;
import java.util.TreeMap;


public class LoginUtils {

    /**
     * 手机登录
     */
    public static void loginByMobile() {
        String userName = SPUtil.getInstance().getString(Constants.USER_NAME,"");
        String userPwd = SPUtil.getInstance().getString(Constants.USER_PWD,"");
        if (!(RegularUtils.validatePhoneNumber(userName) && RegularUtils.isPassword(userPwd))) {
            LogUtils.INSTANCE.e("传入的手机号或密码错误");
        } else {
            Map<String, String> map = new TreeMap<>();
            map.put("mobile", userName);
            map.put("password", MD5Utlis.md5(userPwd));
            HttpUtils.getData(map, Constants.LOGIN, new HttpUtils.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        if(GsonUtils.parseObject(response, UserBean.class).getMsg().equals("OK")){
                            LogUtils.INSTANCE.e("进入软件获取用户信息并写入缓存---------------->"+response);
                            SPUtil.getInstance().putString(Constants.USER_BEAN, response);
                        }
                    }catch (Exception e){
                        reSetUserDate();
                    }
                }

                @Override
                public void onFailure(String msg, Exception e) {
                    reSetUserDate();
                }
            });
        }
    }

    /**
     * 微信或QQ登录
     * */
    public static void otherLogin(){
        Map<String,String> map = new TreeMap<>();
        String openId = SPUtil.getInstance().getString(Constants.OPENID);
        String type = SPUtil.getInstance().getString(Constants.LOGIN_TYPE);
        if (openId.isEmpty() || type.isEmpty())
            return;
        map.put("openId",openId );
        map.put("type", type);
        //注册成功后登录
        HttpUtils.getData(map, Constants.LOGIN_THIRD, new HttpUtils.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    if(GsonUtils.parseObject(response, UserBean.class).getMsg().equals("OK")){
                        LogUtils.INSTANCE.e("进入软件获取用户信息并写入缓存---------------->"+response);
                        SPUtil.getInstance().putString(Constants.USER_BEAN, response);
                    }
                }catch (Exception e){
                    reSetUserDate();
                }
            }

            @Override
            public void onFailure(String msg, Exception e) {
                reSetUserDate();
            }
        });
    }

    private static void reSetUserDate(){
        LogUtils.INSTANCE.e("请求失败清除用户数据");
        SPUtil.getInstance().putBoolean(Constants.IS_LOGIN, false)
                .putBoolean(Constants.IS_CODE, false)
                .putBoolean(Constants.IS_REMEMBERPWD, false)
                .putBoolean(Constants.IS_REMEMBERUSER, false)
                .putBoolean(Constants.IS_LOGIN_THIRD, false)
                .putBoolean(Constants.IS_VIP, false)
                .putString(Constants.OTHER_NAME,"")
                .putString(Constants.OPENID, "")
                .putString(Constants.LOGIN_TYPE, "")
                .putString(Constants.USER_BEAN, "")
                .putString(Constants.USER_NAME, "")
                .putString(Constants.USER_PWD, "");
    }
}
