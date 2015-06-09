package com.company.ilunch.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 删除收藏返回信息JavaBean
 */
public class DeleMyCollectBean {
	@JSONField(name = "Body")
	private Body body;
	@JSONField(name = "Head")
	private Head head;

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
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
		@JSONField(name = "bool")
		private String bool;

		public String getBool() {
			return bool;
		}

		public void setBool(String bool) {
			this.bool = bool;
		}
	}
}
