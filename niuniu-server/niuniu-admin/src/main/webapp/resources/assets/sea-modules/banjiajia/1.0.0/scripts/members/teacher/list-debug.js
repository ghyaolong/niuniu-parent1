/*
 * teacher list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/members/teacher/list-debug", [ "$-debug", "pager-debug", "util-debug", "area-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug", "baiduMap-debug", "plugin-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), Util = require("util-debug"), Area = require("area-debug");
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    var BDMap = require("baiduMap-debug");
    var theacertbody = $("#theacertbody");
    var plugin = require("plugin-debug");
    var pageSize = 0;
    var parmasArr = "";
    var util = new Util();
    util.include(_contentPath + "/resources/assets/src/scripts/common/BMapLibCityList.js");
    function teacherTr(data) {
        var html = "";
        var skillsValus = "";
        if (!!data && !!data.skills) {
            var skillArray = data.skills.split(",");
            for (var i = 0; i < skillArray.length; i++) {
                var skill = $("#skillId" + skillArray[i]).val();
                if (skill != null && skill.length > 0) skillsValus += skill;
            }
            if (skillsValus.length > 0) {
                skillsValus = skillsValus.substring(0, skillsValus.length - 1);
            }
        }
        html += '<tr class="odd">' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + '<a href="' + _contentPath + "/members/teacher/show/" + data.id + '.html" target="_blank">' + data.name + "</a>" + "</td>" + '<td class="">' + data.phone + "</td>" + '<td class=" ">' + data.qq + "</td>" + '<td class=" ">' + data.weixin + "</td>" + '<td class=" ">' + skillsValus + "</td>" + '<td class=" ">' + data.city + "</td>" + '<td class=" "><a href="javascript:void(0)" class="shoppingDistrict" data-city="' + data.city + '" data-shopDis="' + data.shoppingDistrict + '">查看</a></td>' + //data.shoppingDistrict
        '<td class=" ">' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + '<a class="blue" href="' + _contentPath + "/members/teacher/show/" + data.id + '.html">' + '<i class="icon-zoom-in bigger-130"></i>' + "</a>" + '<a class="green" href="' + _contentPath + "/members/teacher/edit/" + data.id + '.html">' + '<i class="icon-pencil bigger-130"></i>' + "</a>" + "</div>" + "</td>" + "</tr>";
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
    function searchBtn() {
        $("#serchBtn").click(function() {
            var filed = $(this).attr("data-fild");
            parmasArr = "";
            parmasArr = "&" + filed + "=" + encodeURI(encodeURI($("#searchValue").val()));
            fetchData(null, null, null, 1);
        });
    }
    function fetchData(event, originalEvent, type, page) {
        var begin = (parseInt(page) - 1) * pageSize;
        $.ajax({
            url: _contentPath + "/members/teacher/list/query.json?begin=" + begin + parmasArr,
            async: true,
            dataType: "json",
            success: function(data) {
                relaodDate(data.teacherInfos);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    function relaodDate(result) {
        var theacertbodyHtml = "";
        for (var teacher in result.data) {
            theacertbodyHtml += teacherTr(result.data[teacher]);
        }
        theacertbody.html(theacertbodyHtml);
        //var map=new BMap.Map("tohotelmap");
        //var map = new BMap.Map("map_container");
        //map.centerAndZoom("西安", 12);
        //console.log(map);
        //var cityList = new BMapLib.CityList({
        //	container: 'container',
        //	map: map
        //});
        //	console.log(cityList);
        //cityList.getBusiness('科技一路', function(json){
        //	console.log('商圈');
        //	console.log(json);
        //});
        //var myCityListObject = new BMapLib.CityList();
        //console.log(myCityListObject);
        //myCityListObject.getBusiness("科技一路",function(str){
        //	console.log(str);
        //});
        $(".shoppingDistrict").click(function() {
            var shopDisStr = $(this).attr("data-shopDis");
            var dataCity = $(this).attr("data-city");
            var arr = shopDisStr.split(",");
            //var shopDisArr=new Array();
            //for(var i=0;i<arr.length;i++) {
            //	shopDisArr.push(arr[i]);
            //}
            new BDMap().divtoshow(dataCity, arr);
        });
        pageSize = result.page.length;
        var option = {
            totalPages: result.page.total,
            currentPage: result.page.current,
            callback: fetchData
        };
        Pager.init($(".pagination"), option);
    }
    var func = {
        init: function() {
            util.ajaxGlobalSetting();
            //new util().include("http://api.map.baidu.com/api?v=1.2");
            fetchData(null, null, null, 0);
            searchBtn();
            new plugin().bootstrap().dropdownInIt();
            var option = {
                area: {
                    province: $("#province"),
                    city: $("#city"),
                    district: $("#district")
                },
                service: {
                    district: $("#shoppingDistrictsSelect"),
                    districtVal: $("#shoppingDistrictId")
                }
            };
            var area = new Area(option);
            area.init();
        }
    };
    func.init();
});
