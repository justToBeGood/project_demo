package org.wengwx.com.entiry;

import java.io.Serializable;

/**
 * @ClassName User
 * @Author wengweixin
 * @Date 2020/11/27 9:53
 **/
public class User  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
