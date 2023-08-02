package com.customer.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class myErrorDetails {
	
	private String name;
	private LocalDate localDate;
	private String desc;
	
	public myErrorDetails() {
		super();
	}

	public myErrorDetails(String name, LocalDate localDate, String desc) {
		super();
		this.name = name;
		this.localDate = localDate;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


}
