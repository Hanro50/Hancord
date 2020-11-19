package org.han.files;

import org.han.api.BaseData;
public abstract class DisgotJsonObj extends JsonObj{
	private static final long serialVersionUID = 1L;

	public DisgotJsonObj(String Path, String FileName, String FileExtention) {
		super(Path, FileName, FileExtention, BaseData.getFU());
		// TODO Auto-generated constructor stub
	}

}
