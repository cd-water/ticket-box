package com.cdwater.movietimeuser.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeMsg {

    private String phone;

    private String code;
}
