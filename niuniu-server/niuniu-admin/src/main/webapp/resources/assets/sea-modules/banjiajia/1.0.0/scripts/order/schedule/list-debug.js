/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/order/schedule/list-debug", [ "$-debug", "pager-debug", "util-debug", "state-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "baiduMap-debug", "validation-debug", "plugin-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), Util = require("util-debug"), State = require("state-debug");
    var util = new Util();
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    var BDMap = require("baiduMap-debug");
    require("validation-debug")($);
    var plugin = require("plugin-debug");
    var theacertbody = $("#theacertbody");
    var pageSize = 0;
    var state = new State();
    state.init(_contentPath + "/system/config/get/order_detail_state.json");
    var parmasArr = new Array();
    function relaodDate(result) {
        var theacertbodyHtml = "";
        if (!!result) {
            for (var teacher in result.data) {
                theacertbodyHtml += teacherTr(result.data[teacher]);
            }
            theacertbody.html(theacertbodyHtml);
            $(".locationMap").click(function() {
                var x = $(this).attr("data-x");
                var y = $(this).attr("data-y");
                var addStr = $(this).attr("data-baby-add");
                var txt = $(this).attr("data-name") + "于" + $(this).attr("data-t") + "在此" + $(this).attr("data-type");
                var babyAdd = "宝宝:" + $(this).attr("data-baby-name") + "在此处";
                var option = {
                    point: {
                        lng: x,
                        lat: y,
                        txt: txt
                    },
                    adds: {
                        add: addStr,
                        txt: babyAdd
                    }
                };
                new BDMap().divtoshow(option, "", true);
            });
            $("#dataTotal").text("共检索出" + result.page.count + "条数据");
            pageSize = result.page.length;
            var option = {
                totalPages: result.page.total,
                currentPage: result.page.current,
                callback: fetchData
            };
            Pager.init($(".pagination"), option);
        }
    }
    function teacherTr(data) {
        var html = "";
        var orderServiceSchedule = data.orderServiceSchedule;
        var orderDetailSign = data.orderDetailSign;
        var babyAddress = data.orderAddress;
        html += '<tr class="odd">' + //'<td class="center  sorting_1">'+
        //	'<label>'+
        //		'<input type="checkbox" class="ace">'
        //		+'<span class="lbl"></span>'
        //	+'</label>'+
        //'</td>'+
        //'<td class=" ">'+orderServiceSchedule.teacherId+'</td>'+
        '<td class="">' + orderServiceSchedule.teacherName + "</td>" + //'<td class=" ">'+orderServiceSchedule.userId+'</td>'+
        '<td class=" ">' + orderServiceSchedule.userName + "</td>" + '<td class=" "><a href="' + _contentPath + "/order/detail/" + orderServiceSchedule.orderNo + '.html" target="_blank">' + orderServiceSchedule.orderNo + "</a></td>" + '<td class=" ">' + (!!orderDetailSign ? util.getLocalTime(orderDetailSign.createTime / 1e3) : "") + "</td>" + '<td class=" ">' + (!!orderDetailSign ? '<a href="javascript:void(0)" class="locationMap" data-type="签到" data-name="' + orderServiceSchedule.teacherName + '" data-t="' + (!!orderDetailSign ? util.getLocalTime(orderDetailSign.createTime / 1e3) : "") + '"  data-x="' + orderDetailSign.locationX + '" data-y="' + orderDetailSign.locationY + '" data-baby-add="' + babyAddress.address + '" data-baby-name="' + babyAddress.babysNames + '">查看</a>' : "") + "</td>" + '<td class=" ">' + (!!orderDetailSign ? util.getLocalTime(orderDetailSign.signEndTime / 1e3) : "") + "</td>" + '<td class=" ">' + (!!orderDetailSign ? '<a href="javascript:void(0)" class="locationMap" data-type="签退" data-name="' + orderServiceSchedule.teacherName + '" data-t="' + (!!orderDetailSign ? util.getLocalTime(orderDetailSign.signEndTime / 1e3) : "") + '"  data-x="' + orderDetailSign.locationXOut + '" data-y="' + orderDetailSign.locationYOut + '" data-baby-add="' + babyAddress.address + '" data-baby-name="' + babyAddress.babysNames + '">查看</a>' : "") + "</td>" + '<td class=" ">' + state.get(state.OrderProcessState(), orderServiceSchedule.state) + "</td>" + '<td class=" ">' + orderServiceSchedule.serviceStartTime + "</td>" + '<td class=" ">' + orderServiceSchedule.serviceEndTime + "</td>" + '<td class=" ">' + orderServiceSchedule.createTime + "</td>" + '<td class=" ">' + orderServiceSchedule.bak + "</td>" + //'<td class=" ">'+
        //	'<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
        //		'<a class="blue" href="'+_contentPath+'/order/schedule/show/'+orderServiceSchedule.id+'.html">'+
        //			'<i class="icon-zoom-in bigger-130"></i>'+
        //		'</a>'+
        //		'<a class="green" href="'+_contentPath+'/order/schedule/edit/'+orderServiceSchedule.id+'.html">'+
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
            parmasArr.push("orderState=" + $("#stateDropDown").attr("data-chk"));
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
            url: _contentPath + "/order/schedule/list/query.json?begin=" + begin + "&" + parmasArr.join("&"),
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.orderServiceSchedules);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    var func = {
        init: function() {
            util.ajaxGlobalSetting();
            fetchData(null, null, null, 0);
            searchBtn();
            var plugin_bootstrap = new plugin().bootstrap();
            plugin_bootstrap.dropdownInIt();
            plugin_bootstrap.dropDownAlone();
        }
    };
    func.init();
});
