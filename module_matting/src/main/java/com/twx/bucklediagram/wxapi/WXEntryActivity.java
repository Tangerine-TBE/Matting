package com.twx.bucklediagram.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.abc.matting.Constants;
import com.abc.matting.bean.DataBean;
import com.abc.matting.utils.HttpUtils;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.UserBean;
import com.feisukj.base.util.GsonUtils;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.PackageUtils;
import com.feisukj.base.util.SPUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    IWXAPI iwxapi;
    final Map<String,String> map = new TreeMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                LogUtils.INSTANCE.e("code------->"+code);
                //获取accesstoken
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void getAccessToken(String code) {
        RequestBody body = new FormBody.Builder()
                .add("appid",Constants.WX_APP_ID)
                .add("secret",Constants.WX_SECRET)
                .add("code",code)
                .add("grant_type","authorization_code")
                .build();
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/oauth2/access_token")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseInfo= response.body().string();
                LogUtils.INSTANCE.e("获取accesstoken成功--------------->"+responseInfo);
                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo);
                    access = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                checkThird(openId);
            }
        });
    }

    private void checkThird(String openId) {
        SPUtil.getInstance().putString(Constants.OPENID,openId);
        SPUtil.getInstance().putString(Constants.LOGIN_TYPE,"0");
        map.put("openId",openId);
        map.put("type","0");
        //判断是否注册
        HttpUtils.getData(map, Constants.CHECK_THIRD, new HttpUtils.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.INSTANCE.e("验证微信是否注册"+response);
                //如果没有注册
                try{
                    if(GsonUtils.parseObject(response, DataBean.class).getData().equals("0")){
                        map.put("platform", PackageUtils.getAppMetaData(BaseApplication.application,"CHANNEL"));
                        map.put("package",getApplicationInfo().processName);
                        //注册
                        HttpUtils.getData(map, Constants.REGISTER_BY_THIRD, new HttpUtils.RequestCallback() {
                            @Override
                            public void onSuccess(String response) {
                                LogUtils.INSTANCE.e("微信注册----->"+response);
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
     * 微信登录
     * */
    private void wxLogin(){
        //注册成功后登录
        HttpUtils.getData(map, Constants.LOGIN_THIRD, new HttpUtils.RequestCallback() {
            @Override
            public void onSuccess(String response) {
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
                        finish();
                    }
                }catch (Exception e){
                    finish();
                }
            }

            @Override
            public void onFailure(String msg, Exception e) {
                finish();
            }
        });
    }
}
