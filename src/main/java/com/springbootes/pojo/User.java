package com.springbootes.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * @author clearlove
 * @ClassName User.java
 * @Description
 * @createTime 2021年06月03日 22:56:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;

    private int age;

    public static void main(String[] args) {

    }
}
