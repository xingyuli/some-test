package org.swordess.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cover {
	
	public String methodSignature();
	
	public int[] validECs() default {};
	public int[] invalidECs() default {};
	
	public int[] boundaries() default {};
	
	public String[] conditions() default {};
	
}
