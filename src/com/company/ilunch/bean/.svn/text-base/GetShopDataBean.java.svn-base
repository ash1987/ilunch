package com.company.ilunch.bean;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 菜品分类接口返回信息JavaBean
 */
public class GetShopDataBean {
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
		private int id;
		@JSONField(name = "classname")
		private String classname;
		@JSONField(name = "Depth")
		private int depth;
		@JSONField(name = "Status")
		private int status;
		@JSONField(name = "Priority")
		private int priority;
		@JSONField(name = "Parentid")
		private int parentid;
		@JSONField(name = "isDel")
		private int isDel;
		@JSONField(name = "Picture")
		private String picture;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getClassname() {
			return classname;
		}

		public void setClassname(String classname) {
			this.classname = classname;
		}

		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public int getParentid() {
			return parentid;
		}

		public void setParentid(int parentid) {
			this.parentid = parentid;
		}

		public int getIsDel() {
			return isDel;
		}

		public void setIsDel(int isDel) {
			this.isDel = isDel;
		}

		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}
	}
}
