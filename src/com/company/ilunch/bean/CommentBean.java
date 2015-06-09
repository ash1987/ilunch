package com.company.ilunch.bean;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 描述：点评列表JavaBean
 */

public class CommentBean {

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
		@JSONField(name = "picture")
		private String picture;
		@JSONField(name = "Rcontent")
		private String rcontent;
		@JSONField(name = "Rtype")
		private String rtype;
		@JSONField(name = "Rtime")
		private String rtime;
		@JSONField(name = "DataID")
		private String dataID;
		@JSONField(name = "UserID")
		private String userID;
		@JSONField(name = "TogoID")
		private String togoID;
		@JSONField(name = "Point")
		private String point;
		@JSONField(name = "Comment")
		private String comment;
		@JSONField(name = "PostTime")
		private String postTime;
		@JSONField(name = "ServiceGrade")
		private String serviceGrade;
		@JSONField(name = "FlavorGrade")
		private String flavorGrade;
		@JSONField(name = "SpeedGrade")
		private String speedGrade;
		@JSONField(name = "UserName")
		private String userName;
		@JSONField(name = "TogoName")
		private String togoName;
		@JSONField(name = "FoodName")
		private String foodName;

		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		public String getRcontent() {
			return rcontent;
		}

		public void setRcontent(String rcontent) {
			this.rcontent = rcontent;
		}

		public String getRtype() {
			return rtype;
		}

		public void setRtype(String rtype) {
			this.rtype = rtype;
		}

		public String getRtime() {
			return rtime;
		}

		public void setRtime(String rtime) {
			this.rtime = rtime;
		}

		public String getDataID() {
			return dataID;
		}

		public void setDataID(String dataID) {
			this.dataID = dataID;
		}

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}

		public String getTogoID() {
			return togoID;
		}

		public void setTogoID(String togoID) {
			this.togoID = togoID;
		}

		public String getPoint() {
			return point;
		}

		public void setPoint(String point) {
			this.point = point;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getPostTime() {
			return postTime;
		}

		public void setPostTime(String postTime) {
			this.postTime = postTime;
		}

		public String getServiceGrade() {
			return serviceGrade;
		}

		public void setServiceGrade(String serviceGrade) {
			this.serviceGrade = serviceGrade;
		}

		public String getFlavorGrade() {
			return flavorGrade;
		}

		public void setFlavorGrade(String flavorGrade) {
			this.flavorGrade = flavorGrade;
		}

		public String getSpeedGrade() {
			return speedGrade;
		}

		public void setSpeedGrade(String speedGrade) {
			this.speedGrade = speedGrade;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getTogoName() {
			return togoName;
		}

		public void setTogoName(String togoName) {
			this.togoName = togoName;
		}

		public String getFoodName() {
			return foodName;
		}

		public void setFoodName(String foodName) {
			this.foodName = foodName;
		}
	}
}
