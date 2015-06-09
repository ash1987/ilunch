package com.company.ilunch.bean;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 区域列表返回信息JavaBean
 */
public class GetSectionBean {
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
		@JSONField(name = "Num")
		private String num;
		@JSONField(name = "pri")
		private String pri;
		@JSONField(name = "SectionID")
		private String sectionId;
		@JSONField(name = "Cid")
		private String cid;
		@JSONField(name = "SectionName")
		private String sectionName;

		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getPri() {
			return pri;
		}
		public void setPri(String pri) {
			this.pri = pri;
		}
		public String getSectionId() {
			return sectionId;
		}
		public void setSectionId(String sectionId) {
			this.sectionId = sectionId;
		}
		public String getCid() {
			return cid;
		}
		public void setCid(String cid) {
			this.cid = cid;
		}
		public String getSectionName() {
			return sectionName;
		}
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
	}
}
