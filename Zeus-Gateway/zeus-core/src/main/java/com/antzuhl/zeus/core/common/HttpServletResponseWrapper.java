package com.antzuhl.zeus.core.common;

import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {
	private int status = 0;

	public HttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void setStatus(int sc) {
		this.status = sc;
		super.setStatus(sc);
	}

	@Override
	public void setStatus(int sc, String sm) {
		this.status = sc;
		super.setStatus(sc, sm);
	}

	public int getStatus() {
		return status;
	}
}