> 开发工作中，都会需要针对传入的参数进行验证，特别是针对实体进行验证，确保传入的参数格式正确。这里做了一个非常简单的组件进行验证。抛砖引玉，让大家深入思考下反射的应用。    

## 需求
日常开发，都是通过API进行前后端的系统对接，对API参数的验证是一个使用率非常高的功能，如果能非常简便的的进行参数验证，能降低代码量，提升工作效率。 

## 使用  
项目地址：https://github.com/itmifen/mfutility  

以前使用最原始的验证方式：

``` java 
 if(testEntity.getImages().length()>2){
            //这里是业务逻辑
        }
        if(testEntity.getTitle().length()>2){
            //这里是业务逻辑
        }
```  

这样导致实现起来重复的代码太多，而且开发起来太耗时。这里使用注解的方式进行优化，只需要在实体定义的时候，定义验证的额内容，使用的时候用只需要调用验证的方法就可以了。  

``` java
/**
     * 定义测试的实体
     */

    public class TestEntity{
        /**
         * 图片
         */
        @Valid(description = "图片",minLength = 1,maxLength = 200,regex=".*runoob.*")
        private String images;

        /**
         * 标题
         */
        @Valid(description = "标题",isEmpty = false,maxLength = 20)
        private String title;


        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

```


字段 | 说明
---|---
description  | 字段中文名
isEmpty  | 是否可为空
maxLength | 最大长度
minLength | 最小长度
regex | 正则表达式  

验证的时候只需要调用实体就可以进行验证  
``` java
ValidResultEntity validResultEntity = EntityCheckUtil.validate(testEntity);
System.out.println(validResultEntity.getMessage());
```  

返回的ValidResultEntity会告诉你是否成功，如果错误，会告诉你错误的原因。  

## 源码说明  
其实，整体的实现思路非常简单，主要是使用java的自定义注解来进行验证。  
新定义一个注解(Valid.java)：  
``` java

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

```  

建一个通用的方法来进行验证：  
``` java
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

```  

validate 主要是通过反射获取类的值、注解，根据获取的数据进行调用：

``` java 
// 根据方法名获取该方法
Method m = obj.getClass().getDeclaredMethod(methodName, Object.class, Field.class);
// 调用该方法
result = (ValidResultEntity) m.invoke(obj, value, f);
/* 验证结果 有一处失败则退出 */
if(result.isSucceed()==false) {
   return result;
}
```

invoke 中对获取的方法进行具体调用实现，这里我定义了最简单的几个方法，包括：
- isEmpty
- maxLength
- minLength
- regex  

其实，自己也可以扩展更多的方法，只要能了解这个思路，完全可以自己定制更多的规则。  

## 思路扩展  
不管是java 还是 .net，都是支持反射的，反射的应用其实很广的，可以很容易的针对代码进行抽象处理，在具体的开发过成功，其实是可以很好的进行扩展的。  其实，关于实体验证的框架也是有很多成熟的产品(如：http://hibernate.org/validator/)，但是大多数都是考虑很广，实现比较复杂点，如果自己只想做一个很轻量级的，完全可以自己来实现。  
以上的代码非常简单，但是却能节省很大工作量的，再次抛砖引玉，大家也可以思考下很多类似的实现，如： 

- 基于缓存注解

``` java
@redis(key='test',expire=1800)
    public void  testOldRedis(){
        if(testEntity.getImages().length()>2){
            //这里是业务逻辑
        }
        if(testEntity.getTitle().length()>2){
            //这里是业务逻辑
        }
    }
```  
- 基于MQ注解

当然，这些都是需要自己开发的，其实开发的负责难度不高，但是却能让代码的结构更加清晰简洁，反射绝对不是黑科技，而是提高效率的核武器。

（完）


---

欢迎大家关注我的公众号交流、学习、第一时间获取最新的文章。  
微信号：itmifen  

![](https://ws2.sinaimg.cn/large/006tNc79gy1fjqwqm6v25j30760760t7.jpg)



