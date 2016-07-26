package com.viettelperu.qos.model.dto;

import javax.persistence.Lob;

/**
 *
 * {"name":"cat1", "priority":2, "parent":"pCat"}
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class FileUploadDTO {
	String filename;

    String mimeType;

    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
    public String toString() {
        return "FileUploadDTO{" +
                "filename='" + filename + '\'' +
                ", mimeType=" + mimeType +
                '}';
    }
}
