package org.swordess.test.util;

import java.util.Collection;

public class CollectionUtils {

	public static boolean isEmpty(Collection<?> c) {
		return null == c || c.isEmpty();
	}
	
	public static boolean isNotEmpty(Collection<?> c) {
		return null != c && !c.isEmpty();
	}
	
	private CollectionUtils() {
	}
	
}
