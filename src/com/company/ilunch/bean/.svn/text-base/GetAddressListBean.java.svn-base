package com.company.ilunch.bean;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 获取地址列表返回信息JavaBean
 */
public class GetAddressListBean {
	@JSONField(name = "Body")
	private ArrayList<Body> body;
	@JSONField(name = "Head")
	private Head head;

	public ArrayList<Body> getBody() {
		return body;
	}

	public void setBody(ArrayList<Body> body) {
		this.body = body;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public static class Head {
		@JSONField(name = "ResultCode")
		private String resultCode;
		@JSONField(name = "ResultInfo")
		private String resultInfo;

		public String getResultCode() {
			return resultCode;
		}

		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}

		public String getResultInfo() {
			return resultInfo;
		}

		public void setResultInfo(String resultInfo) {
			this.resultInfo = resultInfo;
		}
	}

	public static class Body {
		@JSONField(name = "DataID")
		private String dataID;
		@JSONField(name = "Address")
		private String address;
		@JSONField(name = "BuildingName")
		private String buildingName;
		@JSONField(name = "Receiver")
		private String receiver;

		public String getDataID() {
			return dataID;
		}

		public void setDataID(String dataID) {
			this.dataID = dataID;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getBuildingName() {
			return buildingName;
		}

		public void setBuildingName(String buildingName) {
			this.buildingName = buildingName;
		}

		public String getReceiver() {
			return receiver;
		}

		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
	}
}
