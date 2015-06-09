package com.company.ilunch.bean;

import java.util.ArrayList;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 获取订单列表
 */
public class GetOrderListBean {
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
		@JSONField(name = "Unid")
		private String unid;
		@JSONField(name = "Orderid")
		private String orderid;
		@JSONField(name = "OrderRcver")
		private String orderRcver;
		@JSONField(name = "togoname")
		private String togoname;
		@JSONField(name = "OrderDateTime")
		private String orderDateTime;

		public String getUnid() {
			return unid;
		}

		public void setUnid(String unid) {
			this.unid = unid;
		}

		public String getOrderid() {
			return orderid;
		}

		public void setOrderid(String orderid) {
			this.orderid = orderid;
		}

		public String getOrderRcver() {
			return orderRcver;
		}

		public void setOrderRcver(String orderRcver) {
			this.orderRcver = orderRcver;
		}

		public String getTogoname() {
			return togoname;
		}

		public void setTogoname(String togoname) {
			this.togoname = togoname;
		}

		public String getOrderDateTime() {
			return orderDateTime;
		}

		public void setOrderDateTime(String orderDateTime) {
			this.orderDateTime = orderDateTime;
		}

	}
}
