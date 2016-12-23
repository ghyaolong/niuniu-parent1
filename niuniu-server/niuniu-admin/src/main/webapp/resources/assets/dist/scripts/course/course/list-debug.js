/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/course/course/list-debug", [ "$-debug", "pager-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug", "util-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug");
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    var theacertbody = $("#theacertbody");
    var Util = require("util-debug");
    var util = new Util();
    var pageSize = 0;
    var J = $;
    function relaodDate(result) {
        var theacertbodyHtml = "";
        for (var courseType in result) {
            theacertbodyHtml += courseTypeTr(result[courseType]);
        }
        theacertbody.html(theacertbodyHtml);
    }
    function courseTypeTr(data) {
        var html = "";
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + '<a href="' + _contentPath + "/course/service/show/" + data.id + '.html">' + data.id + "</a>" + "</td>" + '<td class=" ">' + data.name + "</td>" + '<td class="">' + data.descb + "</td>" + '<td class=""><image src="' + data.pic + '"/></td>' + '<td class="">' + data.url + "</td>" + '<td class="">' + data.price + "</td>" + '<td class="">' + data.courseType + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + //		'<a class="blue" href="'+_contentPath+'/course/service/show/'+data.id+'.html">'+
        //			'<i class="icon-zoom-in bigger-130"></i>'+
        //		'</a>'+
        '<a class="green" href="' + _contentPath + "/course/edit/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + //		'<a class="red" href="#">'+
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
            url: _contentPath + "/course/list/query.json?begin=" + begin,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.courses);
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
        }
    };
    func.init();
});
