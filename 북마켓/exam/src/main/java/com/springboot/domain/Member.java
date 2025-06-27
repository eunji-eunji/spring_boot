package com.springboot.domain;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Getter
//@Setter
//@ToString
@Data // = getter, setter, tostring 합친 것
public class Member {
    @MemberId
    private String memberId;

    @Size(min=4, max=10, message="4~10자 이내")
    private String passwd;
}
