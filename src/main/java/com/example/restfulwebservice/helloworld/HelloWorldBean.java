package com.example.restfulwebservice.helloworld;
// lombok

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // setter, getter, 생성자 그리고 필요에 따라서 toString 만들어진다.
@AllArgsConstructor
@NoArgsConstructor // 디폴트 생성자 (매개변수 X)
public class HelloWorldBean {
    private String message; //message라는 property에 Hello World가 들어감. (json)
}
