Date.prototype.Format = function (fmt) { //author: meizz     var o = {        "M+": this.getMonth() + 1, //月份         "d+": this.getDate(), //日         "H+": this.getHours(), //小时         "m+": this.getMinutes(), //分         "s+": this.getSeconds(), //秒         "q+": Math.floor((this.getMonth() + 3) / 3), //季度         "S": this.getMilliseconds() //毫秒     };    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));    for (var k in o)    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));    return fmt;}/* * product list  --douzy */define(function(require,exports,module){	var $=require("$"),Pager=require("pager"),Util=require("util"),State=require("state");	var util=new  Util();	util.ajaxGlobalSetting();	require("ace-elements");	require("jggrid");	require("gridlocale");	require("validation")($);	var plugin=require("plugin");	var Util=require("util");	var util=new Util();	var theacertbody = $("#theacertbody");	var pageSize=0;		$.fn.datetimepicker.dates['zh-CN'] = {			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],			daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],			today: "今天",			suffix: [],			meridiem: ["上午", "下午"]	};		$('.date-picker').datetimepicker({minView: "month", language: 'zh-CN',todayBtn: 1,autoclose:true}).next().on(ace.click_event, function(){		$(this).prev().focus();	});		var parmasArr=new Array();	function  relaodDate(result){		var theacertbodyHtml="";		for ( var teacher in result.data) {			theacertbodyHtml+=teacherTr(result.data[teacher]);		}				theacertbody.html(theacertbodyHtml);		pageSize=result.page.length;		var option={				totalPages:result.page.total,				currentPage:result.page.current,				callback:fetchData			};		Pager.init($(".pagination"),option);			}		function teacherTr(data){		var html="";		html+='<tr class="odd">'+		'<td class=" ">'+(data.incomeDate != null ? data.incomeDate : "")+'</td>'+		'<td class=" ">'+(data.incomeUserId != null ? data.incomeUserId : "")+'</td>'+		'<td class=" ">'+(data.phone != null ? data.phone : "")+'</td>'+		'<td class=" ">'+(data.nickName != null ? data.nickName : "")+'</td>'+		'<td class="">'+(data.starAgentTotal != null ? data.starAgentTotal : 0)+'</td>'+		'<td class=" ">'+(data.starAgentIncome != null ? data.starAgentIncome : 0)+'</td>'+		'<td class=" ">'+(data.centreAgentTotal != null ? data.centreAgentTotal : 0)+'</td>'+		'<td class=" ">'+(data.centreAgentIncome != null ? data.centreAgentIncome : 0)+'</td>'+		'<td class=" ">'+(data.countyAgentTotal != null ? data.countyAgentTotal : 0)+'</td>'+		'<td class=" ">'+(data.countyAgentIncome != null ? data.countyAgentIncome : 0)+'</td>'+		'<td class=" ">'+(data.promotionAgentIncome != null ? data.promotionAgentIncome : 0)+'</td>'+		'</tr>';		return html;	}		function initFunction(){				$("#serchBtn").click(function(){			var filed=$(this).attr("data-fild");			parmasArr=new Array();			parmasArr.push(filed+"="+encodeURI(encodeURI($("#searchValue").val())));			parmasArr.push("startTime="+$("#id-date-picker-3").val());			parmasArr.push("endTime="+$("#id-date-picker-4").val());			fetchData(null,null,null,1);		});				$("#exportBtn").click(function(){			var filed=$(this).attr("data-fild");			parmasArr=new Array();			parmasArr.push(filed+"="+encodeURI(encodeURI($("#searchValue").val())));			parmasArr.push("startTime="+$("#id-date-picker-3").val());			parmasArr.push("endTime="+$("#id-date-picker-4").val());			window.location = _contentPath + '/agent/recommended/export.json?' + parmasArr.join("&");		});			}		function enableTooltips(table) {		$('.navtable .ui-pg-button').tooltip({container:'body'});		$(table).find('.ui-pg-div').tooltip({container:'body'});	}	function fetchData(event, originalEvent, type,page){		var begin=(parseInt(page)-1)*pageSize;		$.ajax({	        url: _contentPath+'/agent/recommended/list/query.json?begin='+begin+"&"+parmasArr.join("&"),	        async: true,	        dataType: 'json',	        success: function(data){	        	        	relaodDate(data.agentRecommendeds);	        },	       	        error: function(jqXHR , textStatus , errorThrown){	          console.log("error="+errorThrown);	        }	  });	}	var func = {			init : function() {				fetchData(null,null,null,0);				initFunction();				var plugin_bootstrap=new plugin().bootstrap();				plugin_bootstrap.dropdownInIt();				plugin_bootstrap.dropDownAlone();			}		};			func.init();		});