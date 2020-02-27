$(function () {

    var incomer = getQueryString("incomer");
    var orderNo = getQueryString("orderNo");
    localStorage.setItem("orderNo", orderNo);


    //调用注册方法
    registerHandlerAPP();

});

//判断是否在APP内打开页面
function isApp() {
    if(/DahuoApp/i.test(navigator.userAgent)){
        return true;
    }else{
        return false;
    }
}

//截取URL的参数
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}



/** * [isMobile 判断平台] * @param test: 0:iPhone    1:Android */
function ismobile() {
    var u = navigator.userAgent,
        app = navigator.appVersion;
    if (/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))) {
        if (window.location.href.indexOf("?mobile") < 0) {
            try {
                if (/iPhone|mac|iPod|iPad/i.test(navigator.userAgent)) {
                    return '0';
                } else {
                    return '1';
                }
            } catch (e) {}
        }
    } else if (u.indexOf('iPad') > -1) {
        return '0';
    } else {
        return '1';
    }
}

//App交互开始
function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        return callback(WebViewJavascriptBridge);
    }
    if (window.WVJBCallbacks) {
        return window.WVJBCallbacks.push(callback);
    }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() {
        document.documentElement.removeChild(WVJBIframe)
    }, 0)
};

// 给APP端注册的方法
function registerHandlerAPP() {
    // 注册渲染的方法
    if (ismobile() == 0){
        setupWebViewJavascriptBridge(function (bridge) {
            bridge.registerHandler('getSignResponseInfo', function (data, responseCallback) {
                getSignResponseInfo(data);
                responseCallback(data);
            });
        });
    }
}

/** js调APP端人脸识别 */
function agentAppFaceOcr() {
    if (ismobile() == 0) {
        // 调用ios的方法
        setupWebViewJavascriptBridge(function (bridge) {
            bridge.callHandler('agentAppFaceOcr', {
                'keyword': localStorage.getItem("keyword"),
                'url': localStorage.getItem("pdfPath")
            },function(data) {
                log('JS got data', data);
            })
        })
    } else if (ismobile() == 1) {
        // 调用安卓的方法
        window.app.agentAppFaceOcr(localStorage.getItem("keyword"), localStorage.getItem("pdfPath"));
    }
}

/** js调APP端电子签名 */
function agentAppSign() {
    if (ismobile() == 0) {
        // 调用ios的方法
        setupWebViewJavascriptBridge(function (bridge) {
            bridge.callHandler('agentAppSign', {
                'keyword': localStorage.getItem("keyword"),
                'url': localStorage.getItem("pdfPath")
            }, function(response) {
                log('JS got response', response)
            })
        })
    } else if (ismobile() == 1) {
        // 调用安卓的方法
        window.app.agentAppSign(localStorage.getItem("keyword"), localStorage.getItem("pdfPath"));
    }
}

//ajax示例1
$('#btn-signature-next').on("click", function() {
    $.ajax({
		url: path + "/dealSignature",
		type: "post",
		async: true,
		data: {orderNo: orderNo},
		dataType:"json",
		beforeSend: function () { //发送前执行
			$(".loading").show();
		},
		success: function (data) {
			var obj = data;
			if (obj.code == "1") {
				var orderNo = obj.data.orderNo;
				var returnPage = obj.data.returnPage;
				var payPage = obj.data.payPage;
				if (payPage == 1) {
					window.location.href = plamentPath + "?orderNo=" + orderNo + "&inTranType=order";
				} else {
					window.location.href = path + returnPage + "?orderNo=" + orderNo;
				}

			} else {
				$(".pop").show();
				$(".mask").show();
				$(".pop span").text(obj.msg);
			}
		},
		error: function (data) {
			console.log(data);
		},
		complete: function () { //发送完成执行
			$(".loading").hide();
		}
    });
});

//ajax示例2
$(".pop-sure").click(function(event) {
	var orderNo =  $(this).siblings(".btnOrderNo").val();
	cancenOrder(memberId,orderNo,code);
});
function cancenOrder(memberId,orderNo,code){
		sendRequest(path +"/orderInfo/cancleOrder",{
		memberId:memberId,
		orderNo:orderNo,
		code:code
	},function(){
		$(".pop-sure").html('<img src="../images/loading.gif" alt="" />');
		$(".pop-sure").addClass("disabled");
	},function (data) {
		var dataObj = JSON.parse(data);
		if(dataObj.code == 1){
			window.location.href =path+"/orderInfo/myOrderList?userId="+memberId+"&type=5&code="+code+"&roleType="+roleType;
		}else{
			$(".mask").hide();
			//alert(dataObj.msg)
			$(".prommask").show();
			$(".prommask span").html(dataObj.msg);
		}
	},function(){

	},function(){
		$(".pop-sure").html('确定');
		$(".pop-sure").removeClass("disabled");
	});
}
function sendRequest(url, data, beforeSend, success, error, complete) {
    $.ajax({
        url: url,
        type: "post",
        async: true,
        data: data ? data : {},
        beforeSend:function(){
            if (beforeSend) {
                beforeSend();
            }
        },
        success: function (data) {
            if (success) {
                success(data);
            }
        },
        error: function (data) {
            if (error) {
                error(data);
            }
        },
        complete:function(){
            $(".loading").hide();
           if (complete) {
                complete();
            }
        }
    });
}



