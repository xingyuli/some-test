package org.swordess.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestCaseAnalysis {
	
	public MethodAnalysis[] value();
	
	public static @interface MethodAnalysis {
		public String signature();
		public EquivalentCondition[] equivalentConditions();
		public Boundary[] boundaries() default {};
	}
	
}
