package com.viettelperu.qos.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.viettelperu.qos.framework.data.JPAEntity;

/**
 * The FileUpload Entity
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Entity
@Table(indexes = {  @Index(name="filename_idx", columnList = "filename", unique = true)})
public class FileUpload extends JPAEntity<Long> {

	public FileUpload() {
    }
	
	public FileUpload(String filename, byte[] file, String mimeType) {

        this.file = file;
        this.filename = filename;
        this.mimeType = mimeType;
    }
	
	private String filename;

    @Lob
    private byte[] file;

    private String mimeType;
    
    private String username;

    @NotNull @NotBlank
    @Column
    public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}

    @Column
	public byte[] getFile() {
		return file;
	}


	public void setFile(byte[] file) {
		this.file = file;
	}

	@Column
	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Column
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	@Override
    public String toString() {
        return "FileUpload{" +
                "filename='" + filename + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
