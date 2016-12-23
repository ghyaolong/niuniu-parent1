/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/welfare/discount/list-debug", [ "$-debug", "pager-debug", "util-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug" ], function(require, exports, module) {
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
        var html = "";
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + '<a href="' + _contentPath + "/welfare/discount/show/" + data.id + '.html">' + data.id + "</a>" + "</td>" + '<td class=" ">' + data.name + "</td>" + '<td class="">' + data.city + "</td>" + '<td class=" ">' + (data.priceLimitMin == 0 ? "不限" : data.priceLimitMin) + "</td>" + '<td class=" ">' + (data.countLimitMin == 0 ? "不限" : data.countLimitMin) + "</td>" + '<td class=" ">' + (data.discountType == 1 ? "抵扣" : "打折") + "</td>" + '<td class=" ">' + data.aggressive + "</td>" + '<td class=" ">' + (data.state == 1 ? "正常" : "暂停") + "</td>" + '<td class=" ">' + (data.courseId == 0 ? "不限" : data.courseId) + "</td>" + '<td class=" ">' + data.startTime + "</td>" + '<td class=" ">' + data.endTime + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + //'<a class="blue" href="'+_contentPath+'/welfare/discount/show/'+data.id+'.html">'+
        //	'<i class="icon-zoom-in bigger-130"></i>'+
        //'</a>'+
        '<a class="green" href="' + _contentPath + "/welfare/discount/edit/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + //		'<a class="red" href="#">'+
        //			'<i class="icon-trash bigger-130"></i>'+
        //		'</a>'+
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
            url: _contentPath + "/welfare/discount/list/query.json?begin=" + begin,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.discounts);
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
