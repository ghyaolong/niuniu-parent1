/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/welfare/cashcoupon/config/list-debug", [ "$-debug", "pager-debug", "util-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), Util = require("util-debug");
    var util = new Util();
    util.ajaxGlobalSetting();
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    var theacertbody = $("#theacertbody");
    var pageSize = 0;
    var J = $;
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
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + data.id + "</td>" + '<td class=" ">' + data.name + "</td>" + '<td class="">' + util.fmoney(data.denomination / 100, 2) + "</td>" + '<td class=" ">' + data.expireDay + "</td>" + '<td class=" ">' + (data.type == 1 ? "普通" : "") + "</td>" + '<td class=" ">' + data.uniquenNum + "</td>" + '<td class=" ">' + data.descb + "</td>" + '<td class=" ">' + data.startTime + "</td>" + '<td class=" ">' + data.endTime + "</td>" + '<td class=" ">' + data.max + "</td>" + '<td class=" ">' + data.userMax + "</td>" + '<td class=" ">' + data.grantCount + "</td>" + '<td class=" ">' + (data.state == 0 ? "停止" : "正常") + "</td>" + '<td class=" ">' + data.createTime + "</td>" + '<td class=" ">' + data.modifyTime + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + '<a class="green" href="' + _contentPath + "/welfare/cashcoupon/config/edit/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + //'<a class="red" href="#">'+
        //	'<i class="icon-trash bigger-130"></i>'+
        //'</a>'+
        "</div>" + "</td>" + "</tr>";
        return html;
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
            url: _contentPath + "/welfare/cashcoupon/config/list/query.json?begin=" + begin,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.cashCouponConfigs);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    var func = {
        init: function() {
            fetchData(null, null, null, 0);
        }
    };
    func.init();
});
