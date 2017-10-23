package com.itmifen.utility.annotation.entityvalid;


import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {


    /**
     * @return 字段描述
     */
    public String description() default "";

    /**
     * 是否可以为空
     * @return true可以为空，false不能为空
     */
    public boolean isEmpty() default true;

    /**
     * 最大长度
     * @return
     */
    public int maxLength() default 1000;

    /**
     * 最小长度
     * @return
     */
    public int minLength() default 0;

    /**
     * 正则表达式
     * @return
     */
    public  String regex() default "";

}
