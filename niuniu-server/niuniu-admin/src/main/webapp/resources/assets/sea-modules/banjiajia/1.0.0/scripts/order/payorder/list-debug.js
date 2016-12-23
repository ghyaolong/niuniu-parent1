/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/order/payorder/list-debug", [ "$-debug", "pager-debug", "util-debug", "state-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug", "plugin-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), Util = require("util-debug"), State = require("state-debug");
    var util = new Util();
    util.ajaxGlobalSetting();
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    var plugin = require("plugin-debug");
    var Util = require("util-debug");
    var util = new Util();
    var payOrderState = new State();
    payOrderState.init(_contentPath + "/system/config/get/getPayOrderState.json");
    var state = new State();
    state.init(_contentPath + "/system/config/get/pay_channel_state.json");
    var theacertbody = $("#theacertbody");
    var pageSize = 0;
    var parmasArr = new Array();
    function relaodDate(result) {
        var theacertbodyHtml = "";
        for (var teacher in result.data) {
            theacertbodyHtml += teacherTr(result.data[teacher]);
        }
        theacertbody.html(theacertbodyHtml);
        //		teacherPage.html(pagehtml(result.page));
        pageSize = result.page.length;
        var option = {
            totalPages: result.page.total,
            currentPage: result.page.current,
            callback: fetchData
        };
        Pager.init($(".pagination"), option);
    }
    function teacherTr(data) {
        var html = "";
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + data.payNo + "</td>" + '<td class=""><a href="/order/detail/' + data.orderNo + '.html" target="_blank">' + data.orderNo + "</a></td>" + '<td class=" ">' + util.fmoney(data.payPrice / 100, 2) + "</td>" + '<td class=" ">' + data.userId + "</td>" + '<td class=" ">' + payOrderState.get(payOrderState.OrderProcessState(), data.payState) + "</td>" + '<td class=" ">' + (data.payChannel == 1 ? "android" : "IOS") + "</td>" + '<td class=" ">' + state.get(state.OrderProcessState(), data.payWay) + "</td>" + '<td class=" ">' + (!!data.createTime ? data.createTime : "") + "</td>" + '<td class=" ">' + (!!data.modifyTime ? data.modifyTime : "") + "</td>" + '<td class=" ">' + (!!data.finshiTime ? data.finshiTime : "") + "</td>" + //'<td class=" ">'+
        //	'<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
        //		'<a class="blue" href="'+_contentPath+'/order/pay/show/'+data.id+'.html">'+
        //			'<i class="icon-zoom-in bigger-130"></i>'+
        //		'</a>'+
        //		'<a class="green" href="'+_contentPath+'/order/pay/edit/'+data.id+'.html">'+
        //			'<i class="icon-pencil bigger-130"></i>'+
        //		'</a>'+
        //		'<a class="red" href="#">'+
        //			'<i class="icon-trash bigger-130"></i>'+
        //		'</a>'+
        //	'</div>'+
        //'</td>'+
        "</tr>";
        return html;
    }
    function searchBtn() {
        $("#serchBtn").click(function() {
            var filed = $(this).attr("data-fild");
            parmasArr = new Array();
            parmasArr.push(filed + "=" + encodeURI(encodeURI($("#searchValue").val())));
            fetchData(null, null, null, 1);
        });
    }
    function enableTooltips(table) {
        $(".navtable .ui-pg-button").tooltip({
            container: "body"
        });
        $(table).find(".ui-pg-div").tooltip({
            container: "body"
        });
    }
    function fetchData(event, originalEvent, type, page) {
        var begin = (parseInt(page) - 1) * pageSize;
        $.ajax({
            url: _contentPath + "/order/pay/list/query.json?begin=" + begin + "&" + parmasArr.join("&"),
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.payOrders);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    var func = {
        init: function() {
            fetchData(null, null, null, 0);
            searchBtn();
            var plugin_bootstrap = new plugin().bootstrap();
            plugin_bootstrap.dropdownInIt();
            plugin_bootstrap.dropDownAlone();
        }
    };
    func.init();
});
