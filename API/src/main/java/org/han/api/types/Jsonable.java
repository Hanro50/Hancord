package org.han.api.types;

import org.han.files.FIleUtil;

public interface Jsonable {
	public default String encode() {
		return FIleUtil.GsonInstanceAll.toJson(this);
	}
	
	public static <T extends Jsonable> T decode(String Encodedval, Class<T> clazztodecode) {
		return FIleUtil.GsonInstanceAll.fromJson(Encodedval, clazztodecode);
	}
}
