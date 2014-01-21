package org.swordess.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Boundary {
	public int nbr();
	public String desc();
}
