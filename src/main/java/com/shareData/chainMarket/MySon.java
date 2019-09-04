package com.shareData.chainMarket;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.shareData.chainMarket.constant.HttpCode;
import com.shareData.chainMarket.i.Central;
import com.shareData.chainMarket.i.RequestManager;
import io.netty.channel.ChannelHandlerContext;

public abstract class MySon {
    private Class<?> myboss;

    public void setMyboss(Class<?> mybos) {
        myboss = mybos;
    }

    public void body(String body, Map<Object, Object> map, RequestManager web, ChannelHandlerContext ch
            , String methodName) {
        try {
            //java反射机制获取所有方法名
            Method[] declaredMethods = myboss.getDeclaredMethods();
            //遍历循环方法并获取对应的注解名称
            boolean isRight = false;
            for (Method declaredMethod : declaredMethods) {
                String isNotNullStr;
                // 判断是否方法上存在注解  MethodInterface
                boolean annotationPresent = declaredMethod.isAnnotationPresent(Central.class);
                if (annotationPresent) {
                    // 获取自定义注解对象
                    Central methodAnno = declaredMethod.getAnnotation(Central.class);
                    // 根据对象获取注解值
                    isNotNullStr = methodAnno.url();
                    //System.out.println("内部=="+isNotNullStr+",待检测=="+methodName);
                    if (isNotNullStr.hashCode() == methodName.hashCode() && isNotNullStr.equals(methodName)) {
                        isRight = true;
                        Method me = myboss.getMethod(declaredMethod.getName(), String.class, Map.class);
                        Object obj = myboss.newInstance();
                        Object ob = me.invoke(obj, body, map);
                        if (ob != null) {
                            web.response(ch,ob,HttpCode.OK);
                        } else {
                            System.out.println("return null");
                        }
                        break;
                    }
                }

            }
            if (!isRight) {
                System.out.println("NOT FOUND URL");
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
