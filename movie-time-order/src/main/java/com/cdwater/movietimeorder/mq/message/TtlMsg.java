package com.cdwater.movietimeorder.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TtlMsg {

    private Long orderNo;
}
