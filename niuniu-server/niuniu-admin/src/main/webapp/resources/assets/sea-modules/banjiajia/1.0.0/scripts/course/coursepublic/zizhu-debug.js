/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/course/coursepublic/zizhu-debug", [ "$-debug", "pager-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug");
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
            currentPage: result.page.currPage,
            callback: fetchData
        };
        Pager.init($(".pagination"), option);
    }
    function teacherTr(data) {
        var html = "";
        //		var skillsValus="";
        //		var skillArray=data.skills.split(',');
        //		for ( var i = 0; i < skillArray.length; i++) {
        //			var skill = $('#skillId'+skillArray[i]).val();
        //			if(skill!=null&& skill.length>0)
        //			skillsValus+=skill;
        //		}
        //		if(skillsValus.length>0){
        //			skillsValus=skillsValus.substring(0,skillsValus.length-1);
        //		}
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + '<a href="' + _contentPath + "/course/couserpublic/show/" + data.id + '.html">' + data.id + "</a>" + "</td>" + '<td class="">' + data.courseIds + "</td>" + '<td class=" ">' + data.teacherId + "</td>" + '<td class=" ">' + data.startTime + "</td>" + '<td class=" ">' + data.endTime + "</td>" + '<td class=" ">' + data.state + "</td>" + '<td class=" ">' + data.serviceAreas + "</td>" + '<td class=" ">' + data.coursePublicNum + "</td>" + '<td class=" ">' + data.createTime + "</td>" + '<td class=" ">' + data.modifyTime + "</td>" + '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + '<a class="blue" href="' + _contentPath + "/course/couserpublic/show/" + data.id + '.html">' + '<i class="icon-zoom-in bigger-130"></i>' + "</a>" + '<a class="green" href="' + _contentPath + "/course/couserpublic/edit/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + '<a class="red" href="#">' + '<i class="icon-trash bigger-130"></i>' + "</a>" + "</div>" + "</td>" + "</tr>";
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
    function fetchData(page) {
        var begin = (parseInt(page) - 1) * pageSize;
        $.ajax({
            url: _contentPath + "/course/coursepublic/list/query.json?begin=" + begin,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.coursePublics);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    var func = {
        init: function() {
            fetchData(0);
        }
    };
    func.init();
});
