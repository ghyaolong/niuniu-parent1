/**
 * common util  -douzy 
 */
define("banjiajia/web/1.0.0/scripts/common/util-debug", [ "$-debug" ], function(require, exports, module) {
    var $ = require("$-debug");
    function Util() {}
    Util.prototype = {
        JsonEval: function(jsonObj) {
            var str = "";
            try {
                str = eval(jsonObj);
            } catch (err) {
                str = eval("(" + jsonObj + ")");
            }
            return str;
        },
        Request: function() {
            var option = {
                url: "",
                dataType: "json",
                async: true,
                type: "GET",
                beforeSend: function() {},
                success: function() {},
                complete: function() {},
                error: function() {}
            };
            function send(parmes) {
                option.url = parmes.url || option.url;
                option.dataType = parmes.dataType || option.dataType;
                option.async = parmes.async || true;
                //默认异步请求
                option.type = parmes.type || option.type;
                //默认GET
                option.beforeSend = parmes.beforeSend || option.beforeSend;
                option.success = parmes.success || option.success;
                option.complete = parmes.complete || option.complete;
                option.error = parmes.error || option.error;
                option.data = parmes.data || option.data;
                $.ajax(option);
            }
            return {
                send: send
            };
        }(),
        CreateLoading: function(obj) {
            var loadingImg = '<img id="loadingImg" style="padding-top:5px" src="/resources/img/loader.gif">';
            obj.append(loadingImg);
        },
        RemoveLoading: function() {
            $("#loadingImg").remove();
        },
        getLocalTime: function(NS) {
            return NS;
        },
        prompt: function(type, title, str) {
            var _html = null;
            if (!!type) {
                var _commonHtml = '<div role="alert" class="alert alert-{0} alert-dismissible"><button data-dismiss="alert" class="close" type="button"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button><strong>{1}</strong>{2}</div>';
                var _typeStr = type.toLowerCase();
                //success/info/warning/danger
                _html = String.format(_commonHtml, _typeStr, title, str);
            }
            return _html;
        },
        getLocalTime: function(nS) {
            if (!nS || nS == "null") return "";
            return new Date(parseInt(nS) * 1e3).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
        },
        include: function(file) {
            var includePath = "";
            var files = typeof file == "string" ? [ file ] : file;
            for (var i = 0; i < files.length; i++) {
                var name = files[i].replace(/^\s|\s$/g, "");
                var att = name.split(".");
                var ext = att[att.length - 1].toLowerCase();
                var isCSS = ext == "css";
                var tag = isCSS ? "link" : "script";
                var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
                var link = (isCSS ? "href" : "src") + "='" + includePath + name + "'";
                if ($(tag + "[" + link + "]").length == 0) $("head").prepend("<" + tag + attr + link + "></" + tag + ">");
            }
        },
        fmoney: function(s, n) {
            n = n > 0 && n <= 20 ? n : 2;
            s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
            var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
            t = "";
            for (i = 0; i < l.length; i++) {
                t += l[i] + ((i + 1) % 3 == 0 && i + 1 != l.length ? "," : "");
            }
            return t.split("").reverse().join("") + "." + r;
        },
        getAjaxLoadingInitOption: function() {
            var self = this;
            self.include(_contentPath + "/resources/js/msgBox.js");
            self.include(_contentPath + "/resources/css/msgBox.css");
            return {
                t: 0,
                i: 6
            };
        },
        ajaxGlobalSetting: function() {
            var self = this;
            var loadingOption = self.getAjaxLoadingInitOption();
            $.ajaxSetup({
                beforeSend: function() {
                    loadingOption.t = 0;
                    self.ajaxLoading(loadingOption);
                },
                complete: function() {
                    loadingOption.t = 1;
                    self.ajaxLoading(loadingOption);
                }
            });
        },
        ajaxLoading: function(option) {
            var tip = "";
            switch (option.i) {
              case 1:
                tip = "服务器繁忙，请稍后再试。";
                break;

              case 4:
                tip = "设置成功！";
                break;

              case 5:
                tip = "数据拉取失败";
                break;

              case 6:
                tip = "服务器处理中，请稍后...";
                break;
            }
            if (parseInt(option.t) === 0) ZENG.msgbox.show(tip, option.i);
            if (parseInt(option.t) === 1) ZENG.msgbox.show(tip, option.i, 1e3);
        },
        //元转分
        priceY: function(p) {
            return Math.round(p * 100);
        }
    };
    module.exports = Util;
    String.format = function() {
        if (arguments.length == 0) return null;
        var str = arguments[0];
        for (var i = 1; i < arguments.length; i++) {
            var re = new RegExp("\\{" + (i - 1) + "\\}", "gm");
            str = str.replace(re, arguments[i]);
        }
        return str;
    };
});
