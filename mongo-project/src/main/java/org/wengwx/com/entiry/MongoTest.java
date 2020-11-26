package org.wengwx.com.entiry;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName MongoTest
 * @Author wengweixin
 * @Date 2020/11/23 17:19
 **/
@Document(collection = "mongoTests")
public class MongoTest {
    private Integer id;
    private Integer age;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MongoTest{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
