package com.itmifen.utility;

import com.itmifen.utility.annotation.entityvalid.EntityCheckUtil;
import com.itmifen.utility.annotation.entityvalid.Valid;
import com.itmifen.utility.annotation.entityvalid.ValidResultEntity;
import org.junit.jupiter.api.Test;


public class AnnotationTest {

    @Test
    public void testValidate() throws Exception{
        TestEntity testEntity=new TestEntity();
        testEntity.setImages("from runoob.com");
        testEntity.setTitle("aa");
       ValidResultEntity validResultEntity = EntityCheckUtil.validate(testEntity);
       System.out.println(validResultEntity.getMessage());
       assert  validResultEntity.isSucceed();
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
