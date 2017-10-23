package com.itmifen.utility.annotation.entityvalid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author itmifen
 */
public class EntityCheckUtil {

    /**
     * 注解验证电泳方法
     *
     * @param bean 验证的实体
     * @return
     */

    public static ValidResultEntity validate(Object bean) {
        ValidResultEntity result = new ValidResultEntity();
        result.setSucceed(true);
        result.setMessage("验证通过");

        Class<?> cls = bean.getClass();

        // 检测field是否存在
        try {
            // 获取实体字段集合
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                // 通过反射获取该属性对应的值
                f.setAccessible(true);
                // 获取字段值
                Object value = f.get(bean);
                // 获取字段上的注解集合
                Annotation[] arrayAno = f.getAnnotations();
                for (Annotation annotation : arrayAno) {
                    // 获取注解类型（注解类的Class）
                    Class<?> clazz = annotation.annotationType();
                    // 获取注解类中的方法集合
                    Method[] methodArray = clazz.getDeclaredMethods();
                    for (Method method : methodArray) {
                        // 获取方法名
                        String methodName = method.getName();

                        if("description".equals(methodName)) {
                            continue;
                        }


                        // 初始化注解验证的方法处理类 （我的处理方法写在本类中）
                        Object obj = EntityCheckUtil.class.newInstance();
                        // 获取方法
                        try {
                            // 根据方法名获取该方法
                            Method m = obj.getClass().getDeclaredMethod(methodName, Object.class, Field.class);
                            // 调用该方法
                            result = (ValidResultEntity) m.invoke(obj, value, f);
                            /* 验证结果 有一处失败则退出 */
                            if(result.isSucceed()==false) {
                                return result;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 验证是否空值
     *
     * @param value 参数值
     * @param field 字段
     * @return
     */
    private ValidResultEntity isEmpty(Object value, Field field) {
        ValidResultEntity validateResult = new ValidResultEntity();
        Valid annotation = field.getAnnotation(Valid.class);
        if(value == null || "".equals(value)) {
            validateResult.setMessage(annotation.description()+"为必填项");
            validateResult.setSucceed(false);

        } else {
            validateResult.setMessage("验证通过");
            validateResult.setSucceed(true);
        }
        return validateResult;
    }


    /**
     * 验证最大值
     *
     * @param value 参数值
     * @param field 字段
     * @return
     */
    private ValidResultEntity maxLength(Object value, Field field) {
        ValidResultEntity validateResult=new ValidResultEntity();
        Valid annotation = field.getAnnotation(Valid.class);
        if(value != null && value.toString().length() < annotation.maxLength()) {
            validateResult.setSucceed(true);
            validateResult.setMessage("验证成功");

        } else {
            validateResult.setSucceed(false);
            validateResult.setMessage(annotation.description()+"最大长度不能超过"+annotation.maxLength());
        }
        return validateResult;
    }


    /**
     * 验证最大值
     *n
     * @param value 参数值
     * @param field 字段
     * @return
     */
    private ValidResultEntity minLength(Object value, Field field) {
        ValidResultEntity validateResult=new ValidResultEntity();
        Valid annotation = field.getAnnotation(Valid.class);
        if(value != null && value.toString().length() > annotation.minLength()) {
            validateResult.setSucceed(true);
            validateResult.setMessage("验证成功");

        } else {
            validateResult.setSucceed(false);
            validateResult.setMessage(annotation.description()+"最小长度不能小于"+annotation.minLength());
        }
        return validateResult;
    }

    /**
     * 正则表达式验证
     * @param value
     * @param field
     * @return
     */
    private ValidResultEntity regex(Object value,Field field){
        ValidResultEntity validateResult=new ValidResultEntity();
        Valid annotation = field.getAnnotation(Valid.class);

        String pattern=annotation.regex();
        if((pattern==null)||("".equals(pattern))) {
            validateResult.setSucceed(true);
            validateResult.setMessage("验证成功");
        }
        else {
          boolean isMatch= Pattern.matches(pattern, value.toString());
          if(isMatch) {
              validateResult.setSucceed(true);
              validateResult.setMessage("验证成功");
          }
          else {
              validateResult.setSucceed(false);
              validateResult.setMessage(annotation.description()+"格式错误");
          }
        }

        return  validateResult;
    }


}
