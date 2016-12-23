var Global = Global || {};
Global.Error = Global.Error || {};
Global.Config = Global.Config || {};
Global.rootPath = getRootPath();

function ajax(url, requestData, success, error, isTraditional) {
    if (error === undefined) {
        error = function (message) {
            console.log(message);
        };
    }
    if (isTraditional == undefined) {
        isTraditional = false;
    }
    $.ajax({
        url: url,
        traditional: isTraditional,
        data: requestData,
        dataType: 'json',
        type: 'POST',
        
        success: function (data) {
            var variable = data ? data : {};
            success.call(this, variable);
        },
        error: function (message) {
            error.call(this, message.responseText);
        },
        timeout: 30000
    });
}

function ajaxE(url, reqData, success) {
    ajax(url, reqData, success, undefined, true);
}


function ajaxC(config) {
    var tempconfig = {
        url: '',
        type: 'POST',
        reqData: {},
        success: function (data) {
            alert(data);
        },
        error: function (message) {
            console.log(message);
        }
    };
    tempconfig = $.extend(tempconfig, config);

    $.ajax({
        url: tempconfig.url,
        traditional: true,
        data: tempconfig.reqData,
        dataType: 'json',
        type: tempconfig.type,
        success: tempconfig.success,
        error: function (message) {
            tempconfig.error.call(this, message.responseText);
        },
        timeout: 30000
    });
}

function getRootPath() {
    var strFullPath = window.document.location.href;
    var strPath = window.document.location.pathname;
    var pos = strFullPath.indexOf(strPath);
    var prePath = strFullPath.substring(0, pos);
    var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);

    var result = prePath + postPath;
    //result=result.replace('PushList', '');

    return result;
}

$(function () {
    $('body').append('<div id="messageDiv"></div>');
});



/*ajax请求时session超时处理*/
$.ajaxSetup({
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
    complete:function(XMLHttpRequest,textStatus){
        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，
        if(sessionstatus=="timeout"){
            alert("登录超时,请重新登录！");
            //如果超时就处理 ，指定要跳转的页面
            window.location.replace(getRootPath() + "/login.html");
        }
    }
});