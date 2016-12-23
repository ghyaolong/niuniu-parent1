/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/welfare/cashcoupon/list-debug", [ "$-debug", "pager-debug", "util-debug", "state-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug", "plugin-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), Util = require("util-debug"), State = require("state-debug");
    var util = new Util();
    util.ajaxGlobalSetting();
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    var Util = require("util-debug");
    var util = new Util();
    var state = new State();
    state.init(_contentPath + "/system/config/get/cash_coupon_state.json");
    var theacertbody = $("#theacertbody");
    var plugin = require("plugin-debug");
    var pageSize = 0;
    var parmasArr = "";
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
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + '<a href="' + _contentPath + "/welfare/cashcoupon/config/show/" + data.id + '.html">' + data.id + "</a>" + "</td>" + '<td class=" ">' + data.name + "</td>" + '<td class="">' + util.fmoney(data.denomination / 100, 2) + "</td>" + '<td class=" ">' + (data.useType == 1 ? "全部用户" : "绑定用户") + "</td>" + '<td class=" ">' + state.get(state.OrderProcessState(), data.state) + "</td>" + '<td class=" ">' + data.userId + "</td>" + '<td class=" ">' + data.createTime + "</td>" + '<td class=" ">' + data.expireTime + "</td>" + '<td class=" ">' + (!!data.modifyTime ? data.modifyTime : "") + "</td>" + '<td class=" ">' + data.descb + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + //		//'<a class="blue" href="'+_contentPath+'/welfare/cashcoupon/config/show/'+data.id+'.html">'+
        //		//	'<i class="icon-zoom-in bigger-130"></i>'+
        //		//'</a>'+
        '<a class="green" target="_blank" href="' + _contentPath + "/welfare/cashcoupon/upd/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + //		//'<a class="red" href="#">'+
        //		//	'<i class="icon-trash bigger-130"></i>'+
        //		//'</a>'+
        "</div>" + "</td>" + "</tr>";
        return html;
    }
    function searchBtn() {
        $("#serchBtn").click(function() {
            var filed = $(this).attr("data-fild");
            parmasArr = "";
            parmasArr = "&" + filed + "=" + encodeURI(encodeURI($("#searchValue").val()));
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
            url: _contentPath + "/welfare/cashcoupon/list/query.json?begin=" + begin + parmasArr,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.cashCoupons);
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
            new plugin().bootstrap().dropdownInIt();
        }
    };
    func.init();
});
