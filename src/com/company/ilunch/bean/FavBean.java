package com.company.ilunch.bean;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 描述：收藏列表JavaBean
 */

public class FavBean {

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
		@JSONField(name = "dataid")
		private String dataid;
		@JSONField(name = "togoid")
		private String togoid;
		@JSONField(name = "userid")
		private String userid;
		@JSONField(name = "ctime")
		private String ctime;
		@JSONField(name = "Togoname")
		private String togoname;
		@JSONField(name = "FoodName")
		private String foodName;
		@JSONField(name = "foodId")
		private String foodId;
		@JSONField(name = "Picture")
		private String picture;

		public String getFoodId() {
			return foodId;
		}

		public void setFoodId(String foodId) {
			this.foodId = foodId;
		}

		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		public String getDataid() {
			return dataid;
		}

		public void setDataid(String dataid) {
			this.dataid = dataid;
		}

		public String getTogoid() {
			return togoid;
		}

		public void setTogoid(String togoid) {
			this.togoid = togoid;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getCtime() {
			return ctime;
		}

		public void setCtime(String ctime) {
			this.ctime = ctime;
		}

		public String getTogoname() {
			return togoname;
		}

		public void setTogoname(String togoname) {
			this.togoname = togoname;
		}

		public String getFoodName() {
			return foodName;
		}

		public void setFoodName(String foodName) {
			this.foodName = foodName;
		}
	}
}
