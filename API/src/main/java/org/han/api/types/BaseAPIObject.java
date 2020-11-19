package org.han.api.types;

import org.han.files.FIleUtil;
public interface BaseAPIObject {
	@Deprecated
	public default String encode() {
		return FIleUtil.GsonInstanceAll.toJson(this);
	}

	@Deprecated
	public static <T extends BaseAPIObject> T decode(String Encodedval, Class<T> clazztodecode) {
		return FIleUtil.GsonInstanceAll.fromJson(Encodedval, clazztodecode);
	}
	public long getCreationdate();
}
