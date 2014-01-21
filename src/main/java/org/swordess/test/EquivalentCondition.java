package org.swordess.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EquivalentCondition {
	
	public String name();
	public Condition[] valid();
	public Condition[] invalid() default {};
	
	public static @interface Condition {
		public int nbr();
		public String desc();
	}
	
}
