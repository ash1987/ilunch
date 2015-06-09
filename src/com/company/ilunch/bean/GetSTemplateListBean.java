package com.company.ilunch.bean;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 获取备注返回信息JavaBean
 */
public class GetSTemplateListBean {
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
		@JSONField(name = "ID")
		private String id;
		@JSONField(name = "Remark")
		private String remark;
		@JSONField(name = "ischeck")
		private boolean ischeck;

		public boolean isIscheck() {
			return ischeck;
		}

		public void setIscheck(boolean ischeck) {
			this.ischeck = ischeck;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}
}
