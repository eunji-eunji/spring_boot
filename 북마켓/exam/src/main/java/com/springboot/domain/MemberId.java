package com.springboot.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;


@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemberIdValidator.class)
public @interface MemberId{
   String message() default "중복된 아이디입니다";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
