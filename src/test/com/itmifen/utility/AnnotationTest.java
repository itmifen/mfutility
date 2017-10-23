package com.itmifen.utility;

import com.itmifen.utility.annotation.entityvalid.EntityCheckUtil;
import com.itmifen.utility.annotation.entityvalid.Valid;
import com.itmifen.utility.annotation.entityvalid.ValidResultEntity;
import org.junit.Before;
import org.junit.Test;


public class AnnotationTest {

    private  TestEntity testEntity=new TestEntity();

    @Before
    public  void createEntity() {
        this.testEntity.setImages("from runoob.com");
        this.testEntity.setTitle("aa");
    }

    @Test
    public void testValidate() throws Exception{
       ValidResultEntity validResultEntity = EntityCheckUtil.validate(testEntity);
       System.out.println(validResultEntity.getMessage());
       assert  validResultEntity.isSucceed();
    }

    /**
     * 原始的验证思路
     */
    @Test
        public void  testOldMethod(){
            if(testEntity.getImages().length()>2){
                //这里是业务逻辑
            }
            if(testEntity.getTitle().length()>2){
                //这里是业务逻辑
            }
        }

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
}
