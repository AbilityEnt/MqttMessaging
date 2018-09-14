package com.mqttmessaging.List;

public class ConnectionList {

	private String cname, cid;

	public ConnectionList(String cname, String cid) {
		super();
		this.cname = cname;
		this.cid = cid;
	}
	public ConnectionList(String cname) {
		super();
		this.cname = cname;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
}
