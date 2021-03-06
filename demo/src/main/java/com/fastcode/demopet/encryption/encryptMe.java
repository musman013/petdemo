package com.fastcode.demopet.encryption;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface encryptMe {
		public String value() default "";
	}