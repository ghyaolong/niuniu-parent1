/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/system/config/list-debug", [ "$-debug", "pager-debug", "util-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug" ], function(require, exports, module) {
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
        for (var teacher in result) {
            theacertbodyHtml += teacherTr(result[teacher]);
        }
        theacertbody.html(theacertbodyHtml);
    }
    function teacherTr(data) {
        var html = "";
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + data.configKey + "</td>" + '<td class="">' + data.configName + "</td>" + '<td class=" ">' + data.configContent.replace(/(,)/g, " ") + "</td>" + '<td class=" ">' + data.configMax + "</td>" + '<td class=" ">' + data.configMin + "</td>" + '<td class=" ">' + data.configNormal + "</td>" + '<td class=" ">' + data.configDesc + "</td>" + '<td class=" ">' + data.createTime + "</td>" + '<td class=" ">' + data.modifyTime + "</td>" + '<td class=" ">' + data.state + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + '<a class="blue" href="' + _contentPath + "/order/schedule/show/" + data.id + '.html">' + '<i class="icon-zoom-in bigger-130"></i>' + "</a>" + '<a class="green" href="' + _contentPath + "/order/schedule/edit/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + '<a class="red" href="#">' + '<i class="icon-trash bigger-130"></i>' + "</a>" + "</div>" + "</td>" + "</tr>";
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
            url: _contentPath + "/system/config/list/query.json?begin=" + begin,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.systemGlobalConfigs);
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
