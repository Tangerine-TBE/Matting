package com.abc.matting.http;

import com.abc.matting.Constants;
import com.feisukj.base.util.MD5Utlis;
import com.feisukj.base.util.maputils.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class ApiMapUtil {

    /**
     * @param service 服务
     * @param userInfo 必填参数
     * */
    public static Map<String,Object> setMapValues(String service, Map<String,String> userInfo) {
        //获取时间戳
        long currentTimeMillis = System.currentTimeMillis()/1000;
//        LogUtils.INSTANCE.i("接收    拼接的字符串------------>"+Constants.TOKEN_VALUE+currentTimeMillis+Constants.NONCE_VALUE+service+SortMapUtil.sortMapByValue(userInfo));
        String md5 = MD5Utlis.md5(Constants.TOKEN_VALUE+currentTimeMillis+Constants.NONCE_VALUE+service+ MapUtils.sortMapByValue2(userInfo));
//        LogUtils.INSTANCE.i("接收"+service+"   MD5-------->"+md5);
//        LogUtils.INSTANCE.i("接收"+service+"   时间戳-------->"+currentTimeMillis);
        Map<String,Object> map=new HashMap<>();
        map.put(Constants.TIMESTAMP,currentTimeMillis);
        map.put(Constants.SIGNATURE,md5);
        map.putAll(userInfo);
        return  map;
    }
    /**
     * @param service 服务
     * @param userInfo 必填参数
     * @param map1 非必填参数
     * */
    public static Map<String,Object> setMapValues(String service, Map<String,String> userInfo, Map<String,String> map1) {
        //获取时间戳
        long currentTimeMillis = System.currentTimeMillis()/1000;
//        LogUtils.INSTANCE.i("接收    拼接的字符串------------>"+Constants.TOKEN_VALUE+currentTimeMillis+Constants.NONCE_VALUE+service+SortMapUtil.sortMapByValue(userInfo));
        String md5 = MD5Utlis.md5(Constants.TOKEN_VALUE+currentTimeMillis+Constants.NONCE_VALUE+service+MapUtils.sortMapByValue2(userInfo));
        Map<String,Object> map=new HashMap<>();
        map.put(Constants.TIMESTAMP,currentTimeMillis);
        map.put(Constants.SIGNATURE,md5);
        map.putAll(userInfo);
        map.putAll(map1);
        return  map;
    }

    /**
     * @param service 服务
     * */
    public static Map<String,Object> setMapValues(String service) {
        //获取时间戳
        long currentTimeMillis = System.currentTimeMillis()/1000;
        String md5 = MD5Utlis.md5(Constants.TOKEN_VALUE+currentTimeMillis+Constants.NONCE_VALUE+service);
        Map<String,Object> map=new HashMap<>();
        map.put(Constants.TIMESTAMP,currentTimeMillis);
        map.put(Constants.SIGNATURE,md5);
        return  map;
    }
}
