package de.mgruc.service.core;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel
@XmlRootElement
public class ValueEntry {
	@JsonProperty
	private int id;

	@JsonProperty
	private String content;

	public ValueEntry() {
		// Jackson deserialization
	}

	public ValueEntry(int id, String content) {
		this.id = id;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
