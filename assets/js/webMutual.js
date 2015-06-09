// JavaScript Document

var requestHead = "weetmall-request-";
var callbackHead = "weetmall-callback-";

// 点击是否结束
var isClickEnd = true;
// 点击保护的延时
var TheClickDelay = 500;

var webMutual = {
	callbackCount : 100,
	callbackArray : [],
	requestArray : [],
	leftRequestArray : [],
	scheme : "weetmall",
	isPlatformActive:false,
	platformType : "PC",
    isAndroid : false,
    isiOS : false,
	regionCode : "",
	imei : '',
	OSName : "",
	OSVersion : "",
	locInfo : {},
    userId : 0
}

/** 
 * 公用方法 start {
 */

// 显示平台层Alert
webMutual.showAlert = function(message) {
	if (this.isPlatformActive) {
		webMutual.execute(3, '', message);
	} else {
		alert(message);
	}
}

// 显示平台层Toast
webMutual.showToast = function(message) {
	if (this.isPlatformActive) {
		webMutual.execute(4, '', message);
	} else {
		alert(message);
	}
}

// 显示平台层loading界面
webMutual.startLoading = function(message) {
	webMutual.execute(5, '', message);
}

// 关闭平台层loading界面
webMutual.stopLoading = function() {
	webMutual.execute(6, '', '');
}

// 拷贝文本
webMutual.copeText = function(text) {
	webMutual.execute(7, '', text);
}

// 判断是否登录
webMutual.isLogin = function (callback) {
    webMutual.getData('IsUserLogin', '', function (result) {
        webMutual.isLogin = result.isLogin;
		webMutual.userInfo = result.userInfo;
        callback(result);
    });
}

// 获取用户Id
webMutual.getUserId = function (callback) {
    if (webMutual.userId > 0) {
        callback(webMutual.userId);
        return;
    }
    webMutual.getData('GetUserId', '', function (result) {
        webMutual.userId = result.userId;
        callback(result.userId);
    });
}

// 获取用户信息, 暂时没用
webMutual.getUserInfo = function (callback) {
    webMutual.getData('GetUserInfo', '', function (result) {
        webMutual.userInfo = result;
    });
}


// 获取位置信息
webMutual.needOSInfo = function () {
	webMutual.getData('GetOSInfo', '', function (result) {
		webMutual.OSName = result.OSName;
		webMutual.OSVersion = result.OSVersion;
		webMutual.imei = result.imei
	});
}

// 获取位置信息
webMutual.needLocationInfo = function () {
	webMutual.getData('GetLocationInfo', '', function (result) {
		webMutual.locInfo = result;
	});
}

// 获取位置信息
webMutual.needRegionCode = function () {
	webMutual.getData('GetRegionCode', '', function (result) {
		webMutual.regionCode = result;
	});
}



/**
 *	@brief	调用打开平台层界面接口
 *
 *	@param 	handler 	需要打开什么界面,数据类型是字符串
 *	@param 	data 		需要给平台层传什么数据，数据类型是Object
 *	@param 	success 	登录成功时的回调,可以为空或不写
 *	@param 	failed 		登录失败时的回调,可以为空或不写
 *
 *	@return	no return
 */

webMutual.openView = function(handler, data, success, failed)
{
    webMutual.execute(0, handler, data, success, failed);
}


/**
 *	@brief	向平台成发数据
 *
 *	@param 	handler 	需要打开什么界面,数据类型是字符串
 *	@param 	data 		向平台层发送的数据，数据类型是Object，
 *	@param 	success 	登录成功时的回调,返回需要的数据,数据类型为Object,可以为空或不写
 *	@param 	failed 		登录失败时的回调,返回失败原因可以为空或不写, 数据类型为String
 *
 *	@return	no return
 */
webMutual.getData = function(handler, data, success, failed)
{
    webMutual.execute(1, handler, data, success, failed);
}

/**
 *	@brief	向平台成发数据
 *
 *	@param 	handler 	需要打开什么界面,数据类型是字符串
 *	@param 	data 		向平台层发送的数据，数据类型是Object，
 *	@param 	success 	登录成功时的回调,这里只有传输数据成功失败的状态,没有任何数据,需要数据请用getData,可以为空或不写
 *	@param 	failed 		登录失败时的回调,这里只有传输数据成功失败的状态,可以为空或不写
 *
 *	@return	no return
 */
webMutual.sendData = function(handler, data, success, failed)
{
    webMutual.execute(2, handler, data, success, failed);
}

/* 公用方法 } end */

// 获取url参数
webMutual.getQueryString = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

webMutual.activePlatform = function (platform) {
	webMutual.isPlatformActive = true;
	webMutual.platformType = 'Android';
	this.isAndroid = true;
	if (platform && platform == 'iOS') {
		webMutual.isPlatformActive = true;
		webMutual.platformType = 'iOS';
		this.isAndroid = false;
		this.isiOS = true;
	}
	
}

// 检查设备平台
webMutual.checkPlatform = function () {
    var curPlatform = this.getQueryString("platform")
    if (curPlatform) {
		webMutual.platformType = curPlatform;
        if (curPlatform == "Android") {
            this.isAndroid = true;
            return;
        } else if (curPlatform == "iOS") {
            this.isiOS = true;
            return;
        }
    }
    var platform = navigator.platform;
    var androidPlatforms = ['android', 'Linux arm*', 'Linux,ARM*'];
    var iOSPlatforms = ['iPhone', 'iPod', 'iPad'];
	
    for (var i = 0, len = androidPlatforms.length; i < len; i++) {
        if (platform.match(androidPlatforms[i])) {
			webMutual.platformType = "Android";
            this.isAndroid = true;
        }
    }
    if (this.isAndroid == false) {
        for (var i = 0, len = iOSPlatforms.length; i < len; i++) {
            if (platform.match(iOSPlatforms[i])) {
				webMutual.platformType = "iOS";
                this.isiOS = true;
            }
        }
        if (this.isiOS == false) {
            // 请在手机上打开该网页
        }
    }
}

function platformCallback(success, failed) {
	this.success = success;
	this.failed = failed;
	this.handlerCallback = function(result) {
		if (result.isSuccess) {
			if (this.success) {
				if (typeof(result.data) == 'string') {
					var returnData = result.data;
					try {
						returnData = JSON.parse(result.data)
					} catch(e) {
						returnData = result.data;
					} finally {
						this.success(returnData);
					}
					
				} else {
					this.success(result.data);
				}
			}
		} else {
			if (this.failed) {
				this.failed(result.message);
			}
		}
	};
}

webMutual.checkPlatform();

webMutual.execute = function(model, handler, data, success, failed) {
	var aRequest = {};
	aRequest.model = model;
	aRequest.handler = handler;
	aRequest.data = data;
	aRequest.success = success;
	aRequest.failed = failed;
	if (webMutual.isiOS) {
		webMutual.leftRequestArray.unshift(aRequest);
		if (this.leftRequestArray.length > 1) {
			// 存在请求还没获取到数据
			return;
		}
	}
	webMutual.startExecute(aRequest);
	
}

webMutual.startExecute = function(aRequest) {
	var callbackHandler = new platformCallback(aRequest.success, aRequest.failed);
	var callbackId = callbackHead + webMutual.callbackCount;
	var requestId = requestHead + webMutual.callbackCount;
	webMutual.callbackCount ++;
	webMutual.callbackArray[callbackId] = callbackHandler;
	
	// 配置请求数据
	var requestData = {};
	requestData.model = aRequest.model;
	requestData.handler = aRequest.handler;
	requestData.callbackId = callbackId;
	var jsonString = '';
	if (aRequest.data != 'undefined') {
		if (typeof(aRequest.data) == 'string') {
			jsonString = aRequest.data;
		} else {
			jsonString = JSON.stringify(aRequest.data);
		}
	}
	requestData.jsonData = jsonString;
	var requestString = JSON.stringify(requestData);
	this.requestArray[requestId] = requestString;
	
	// 开始发请求
    if (this.isAndroid) {
        window.androidWeb.execute(requestString);
    } else if (this.isiOS) {
        this.execIframe = this.execIframe || createExecIframe();
        this.execIframe.src = this.scheme + ":" + requestId;
    }
}

webMutual.getRequestData = function(requestId) {
	var aRequestData = webMutual.requestArray[requestId];
	if (aRequestData != 'undefined') {
		delete webMutual.requestArray[requestId];
		if (webMutual.isiOS) {
			webMutual.leftRequestArray.pop();
			if (webMutual.leftRequestArray.length > 0) {
				var aRequest = webMutual.leftRequestArray[webMutual.leftRequestArray.length - 1];
				setTimeout(function(){
					webMutual.startExecute(aRequest);
				}, 100);
			}
			
		}
		return aRequestData;
	} else {
		return null;
	}
}

webMutual.platformCallback = function(result) {
	var resultObj = JSON.parse(result);
	var aPlatformCallback = webMutual.callbackArray[resultObj.callbackId];
	aPlatformCallback.handlerCallback(resultObj);
	delete webMutual.callbackArray[resultObj.callbackId];
}

function createExecIframe() {
    var iframe = document.createElement("iframe");
    iframe.style.display = 'none';
    document.body.appendChild(iframe);
    return iframe;
}

// 平台层调用网页，需要被网页重写
webMutual.platformCall = function(method, data) {
    
}

// 平台层实际调用网页的方法
webMutual.platformExecute = function(result) {
    if (typeof(result) == 'object') {
        webMutual.platformCall(result.method, result.data);
    } else {
        var resultObj = JSON.parse(result);
        if (typeof(resultObj.data) == 'string') {
            var returnData = resultObj.data;
            try {
                resultObj.data = JSON.parse(resultObj.data)
            } catch(e) {
                
            } finally {
                webMutual.platformCall(resultObj.method, resultObj.data);
            }
            
        } else {
            webMutual.platformCall(resultObj.method, resultObj.data);
        }
    }
    
}



/**
 * 统一点击事件
 */
$.fn.tap = function (callFunction, clickDelay) {
	$(this).on('click', function () {
		if (isClickEnd) {
			isClickEnd = false;
			if(typeof(callFunction) == 'function') {
				callFunction($(this));
			}
			var aClickDelay = TheClickDelay;
			if (clickDelay) {
				aClickDelay = clickDelay;
			}
			setTimeout(function(){isClickEnd = true}, aClickDelay);
		}
	});
}

$(function () {
	$('a').tap(function(thisItem) {
		if (thisItem.attr('href').indexOf("javascript") == 0) {
			if (webMutual && webMutual.isPlatformActive) {		// 判断是否可以与平台层交互
				// 判断是否是返回
				if (thisItem.attr('url').indexOf('isGoBack') >= 0) {
					webMutual.backToUrl($(this).attr('url'));
				} else {
					var data = {};
					data.url = thisItem.attr('url');
					data.title = thisItem.attr('title');
					webMutual.openView("OpenWebView", data);
				}
				return;
			} else {
				if (thisItem.attr('url') && thisItem.attr('url').length > 0) {
					thisItem.attr('href', thisItem.attr('url'));
					isClickEnd = true;
					thisItem.click();
				}
			}
		}
	});
 })


function openShareView() {
	webMutual.execute(0, 'OpenShareView', '');
}

function getUserInfo() {
	webMutual.execute(1, 'GetUserInfo', '', function(reuslt) {
		document.getElementById('display').innerHTML = JSON.stringify(reuslt);
	},
	function(message) {
		document.getElementById('display').innerHTML = message;
	});
}

function getRegionCode() {
	webMutual.execute(1, 'GetRegionCode', '', function(reuslt) {
		document.getElementById('display').innerHTML = reuslt;
	},
	function(message) {
		document.getElementById('display').innerHTML = message;
	});
}


function showAlert() {
	var sendText = document.getElementById('sendText').value;
	if (sendText.length > 0) {
		webMutual.showAlert(sendText);
	} else {
		document.getElementById('display').innerHTML = "请输入显示类容";
	}
}

function showToast() {
	var sendText = document.getElementById('sendText').value;
	if (sendText.length > 0) {
		webMutual.showToast(sendText);
	} else {
		document.getElementById('display').innerHTML = "请输入显示类容";
	}
}

function startLoading() {
	webMutual.startLoading('正在加载......');
	setTimeout(function() {
		webMutual.stopLoading();
	},3000);
}
