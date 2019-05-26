package com.fightyz.lifecycle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/2/19 8:41 PM
 * Description: ${TODO}
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface OnProductDisconnected {
}
