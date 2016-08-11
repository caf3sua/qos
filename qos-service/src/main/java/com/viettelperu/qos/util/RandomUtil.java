package com.viettelperu.qos.util;

import org.apache.commons.lang.math.RandomUtils;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class RandomUtil {

	public static String randomFilename() {
		return "QOS" + System.currentTimeMillis() + RandomUtils.nextInt();
	}
}
