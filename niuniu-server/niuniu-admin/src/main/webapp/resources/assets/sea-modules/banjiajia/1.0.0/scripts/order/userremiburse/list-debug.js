/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/order/userremiburse/list-debug", [ "$-debug", "pager-debug", "util-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug", "plugin-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), Util = require("util-debug");
    var util = new Util();
    util.ajaxGlobalSetting();
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    var Util = require("util-debug");
    var plugin = require("plugin-debug");
    var util = new Util();
    var theacertbody = $("#theacertbody");
    var pageSize = 0;
    var parmasArr = new Array();
    function relaodDate(result) {
        var theacertbodyHtml = "";
        if (!!result) {
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
    }
    function teacherTr(data) {
        var userReimburse = data.userReimburse;
        var user = data.user;
        var html = "";
        html += '<tr class="odd">' + '<td class=" ">' + userReimburse.reimburseNo + "</td>" + '<td class=" "><a href="' + _contentPath + "/order/detail/" + userReimburse.orderNo + '.html" target="_blank">' + userReimburse.orderNo + "</a></td>" + '<td class="">' + user.name + "</td>" + '<td class="">' + user.phone + "</td>" + '<td class=" ">' + util.fmoney(userReimburse.price / 100, 2) + "</td>" + '<td class=" ">' + (userReimburse.state == 1 ? "退款申请" : "已退款") + "</td>" + '<td class=" ">' + (!!userReimburse.fininshTime ? userReimburse.fininshTime : "") + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + (//'<a class="blue" href="'+_contentPath+'/order/userremiburse/show/'+userReimburse.id+'.html">'+
        //	'<i class="icon-zoom-in bigger-130"></i>'+
        //'</a>'+
        userReimburse.state == 1 ? '<a class="green" href="' + _contentPath + "/order/userremiburse/edit/" + userReimburse.reimburseNo + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" : "") + //'<a class="red" href="#">'+
        //	'<i class="icon-trash bigger-130"></i>'+
        //'</a>'+
        "</div>" + "</td>" + "</tr>";
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
            url: _contentPath + "/order/userremiburse/list/query.json?begin=" + begin + "&" + parmasArr.join("&"),
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.userReimburses);
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
