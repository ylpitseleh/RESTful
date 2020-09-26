package com.example.restfulwebservice.helloworld;
// lombok

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // setter, getter, 생성자 그리고 필요에 따라서 toString 만들어진다.
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldBean {
    private String message;
}
