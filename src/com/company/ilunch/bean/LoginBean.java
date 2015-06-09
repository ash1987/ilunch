package com.company.ilunch.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 登陆接口返回信息JavaBean
 */
public class LoginBean {
	@JSONField(name = "Head")
	private Head head;
	@JSONField(name = "Body")
	private Body body;

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
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
		@JSONField(name = "PhoneActivate")
		private String phoneActivate;
		@JSONField(name = "Usermoney")
		private String usermoney;
		@JSONField(name = "DataID")
		private String dataID;
		@JSONField(name = "Name")
		private String name;
		@JSONField(name = "TrueName")
		private String trueName;
		@JSONField(name = "Sex")
		private String sex;
		@JSONField(name = "Tell")
		private String tell;
		@JSONField(name = "Phone")
		private String phone;
		@JSONField(name = "QQ")
		private String qq;
		@JSONField(name = "MSN")
		private String msn;
		@JSONField(name = "RegTime")
		private String regTime;
		@JSONField(name = "Point")
		private String point;
		@JSONField(name = "Picture")
		private String picture;
		@JSONField(name = "State")
		private String state;
		@JSONField(name = "EMAIL")
		private String email;
		@JSONField(name = "Password")
		private String password;
		@JSONField(name = "IsActivate")
		private String isActivate;
		@JSONField(name = "ActivateCode")
		private String activateCode;
		@JSONField(name = "GroupID")
		private String groupID;
		@JSONField(name = "WebSite")
		private String webSite;
		@JSONField(name = "RID")
		private String rid;
		@JSONField(name = "openid")
		private String openid;
		@JSONField(name = "wtype")
		private String wtype;
		@JSONField(name = "PayPassword")
		private String payPassword;

		public String getPhoneActivate() {
			return phoneActivate;
		}

		public void setPhoneActivate(String phoneActivate) {
			this.phoneActivate = phoneActivate;
		}

		public String getUsermoney() {
			return usermoney;
		}

		public void setUsermoney(String usermoney) {
			this.usermoney = usermoney;
		}

		public String getDataID() {
			return dataID;
		}

		public void setDataID(String dataID) {
			this.dataID = dataID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTrueName() {
			return trueName;
		}

		public void setTrueName(String trueName) {
			this.trueName = trueName;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getTell() {
			return tell;
		}

		public void setTell(String tell) {
			this.tell = tell;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getQq() {
			return qq;
		}

		public void setQq(String qq) {
			this.qq = qq;
		}

		public String getMsn() {
			return msn;
		}

		public void setMsn(String msn) {
			this.msn = msn;
		}

		public String getRegTime() {
			return regTime;
		}

		public void setRegTime(String regTime) {
			this.regTime = regTime;
		}

		public String getPoint() {
			return point;
		}

		public void setPoint(String point) {
			this.point = point;
		}

		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getIsActivate() {
			return isActivate;
		}

		public void setIsActivate(String isActivate) {
			this.isActivate = isActivate;
		}

		public String getActivateCode() {
			return activateCode;
		}

		public void setActivateCode(String activateCode) {
			this.activateCode = activateCode;
		}

		public String getGroupID() {
			return groupID;
		}

		public void setGroupID(String groupID) {
			this.groupID = groupID;
		}

		public String getWebSite() {
			return webSite;
		}

		public void setWebSite(String webSite) {
			this.webSite = webSite;
		}

		public String getRid() {
			return rid;
		}

		public void setRid(String rid) {
			this.rid = rid;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

		public String getWtype() {
			return wtype;
		}

		public void setWtype(String wtype) {
			this.wtype = wtype;
		}

		public String getPayPassword() {
			return payPassword;
		}

		public void setPayPassword(String payPassword) {
			this.payPassword = payPassword;
		}
	}
}
