Date.prototype.Format = function (fmt) { //author: meizz     var o = {        "M+": this.getMonth() + 1, //月份         "d+": this.getDate(), //日         "H+": this.getHours(), //小时         "m+": this.getMinutes(), //分         "s+": this.getSeconds(), //秒         "q+": Math.floor((this.getMonth() + 3) / 3), //季度         "S": this.getMilliseconds() //毫秒     };    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));    for (var k in o)    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));    return fmt;}/* * product list  --douzy */define(function(require,exports,module){	var $=require("$"),Pager=require("pager"),Util=require("util"),State=require("state");	var util=new  Util();	util.ajaxGlobalSetting();	require("ace-elements");	require("jggrid");	require("gridlocale");	require("validation")($);	var plugin=require("plugin");	var Util=require("util");	var util=new Util();	var state=new State();	state.init(_contentPath + '/system/config/get/pay_channel_state.json');	var theacertbody = $("#theacertbody");	var pageSize=0;	var exportBegin=0;		$.fn.datetimepicker.dates['zh-CN'] = {			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],			daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],			today: "今天",			suffix: [],			meridiem: ["上午", "下午"]	};		$('.date-picker').datetimepicker({language: 'zh-CN',todayBtn: 1,autoclose:true}).next().on(ace.click_event, function(){		$(this).prev().focus();	});		var parmasArr=new Array();	function  relaodDate(result){		var theacertbodyHtml="";		for ( var teacher in result.data) {			theacertbodyHtml+=teacherTr(result.data[teacher]);		}				theacertbody.html(theacertbodyHtml);//		teacherPage.html(pagehtml(result.page));		pageSize=result.page.length;		var option={				totalPages:result.page.total,				currentPage:result.page.current,				callback:fetchData			};			Pager.init($(".pagination"),option);	}		function teacherTr(data){		var stateStr = "已领完";        if(data.state==2){        	stateStr = "未领完"        }		var html="";		html+='<tr class="odd">'+		'<td class="center  sorting_1">'+			'<label>'+				'<input type="checkbox" class="ace">'				+'<span class="lbl"></span>'			+'</label>'+		'</td>'+		'<td class=" ">'+data.userId+'</td>'+		'<td class=" ">'+(data.phoneNum == null ? "" : data.phoneNum)+'</td>'+		'<td class=" ">'+(!!data.issueTime?new Date(new Date(data.issueTime).valueOf() + 8*60*60*1000).Format("yyyy-MM-dd HH:mm:ss"):"")+'</td>'+		/*'<td class=" ">'+(!!data.issueTime?data.issueTime:"")+'</td>'+*/		'<td class="">'+(data.luckyBagAmount == null ? "" : util.fmoney(data.luckyBagAmount/100,2))+'</td>'+		'<td class="">'+(data.luckyBagNumber == null ? "" : data.luckyBagNumber)+'</td>'+		'<td class="">'+'</td>'+		'<td class=" ">'+(data.purseBalance == null ? "" : util.fmoney(data.purseBalance/100,2))+'</td>'+		'<td class=" ">'+(data.refundAmount == null ? "" : util.fmoney(data.refundAmount/100,2))+'</td>'+		'<td class=" ">'+stateStr+'</td>'+	'</tr>';				return html;	}	function searchBtn()	{		$("#serchBtn").click(function(){			var filed=$(this).attr("data-fild");			parmasArr=new Array();			parmasArr.push(filed+"="+encodeURI(encodeURI($("#searchValue").val())))			parmasArr.push("year="+$("#yearDropDown").attr("data-chk"));			parmasArr.push("startTime="+$("#id-date-picker-3").val());			parmasArr.push("endTime="+$("#id-date-picker-4").val());			fetchData(null,null,null,1);		});	}	//导出用户福袋明细信息	function exportBtn(){		$("#exportBtn").click(function(){			var filed=$(this).attr("data-fild");			parmasArr=new Array();			parmasArr.push(filed+"="+encodeURI(encodeURI($("#searchValue").val())))			parmasArr.push("year="+$("#yearDropDown").attr("data-chk"));			var startTime = $("#id-date-picker-3").val();			var endTime = $("#id-date-picker-4").val();			if(startTime == '' || endTime == ''){				alert('请选择导出时间');				return;			}			var hours = HourDiff(startTime, endTime);			if(hours >= 24){				alert('导出数据<最多只可导出间隔一天的数据>时间间隔不可大于一天');				return;			}			parmasArr.push("startTime=" + startTime );			parmasArr.push("endTime=" + endTime );			window.location = _contentPath + '/order/luckybag/export.json?begin=' + exportBegin + "&" + parmasArr.join("&");		});	}	//计算小时差的函数，通用      function  HourDiff(begintime,  endtime){        	var begintime_ms = Date.parse(new Date(begintime.replace(/-/g, "/"))); //begintime 为开始时间    	var endtime_ms = Date.parse(new Date(endtime.replace(/-/g, "/")));   // endtime 为结束时间    	var time_ms = new Date(endtime_ms).getTime()- new Date(begintime_ms).getTime();  //时间差的毫秒数    	var hours = Math.floor(time_ms/(3600*1000)) //计算出相差小时数        return  hours;      } 	function enableTooltips(table) {		$('.navtable .ui-pg-button').tooltip({container:'body'});		$(table).find('.ui-pg-div').tooltip({container:'body'});	}	function fetchData(event, originalEvent, type,page){		var begin=(parseInt(page)-1)*pageSize;		exportBegin = begin;		$.ajax({	        url: _contentPath+'/order/luckybag/list/query.json?begin='+begin+"&"+parmasArr.join("&"),	        async: true,	        dataType: 'json',	        success: function(data){	        	        	relaodDate(data.userLuckyBagDetails);	        },	       	        error: function(jqXHR , textStatus , errorThrown){	          console.log("error="+errorThrown);	        }	  });	}	var func = {			init : function() {				fetchData(null,null,null,0);				searchBtn();				exportBtn();				var plugin_bootstrap=new plugin().bootstrap();				plugin_bootstrap.dropdownInIt();				plugin_bootstrap.dropDownAlone();			}		};				func.init();		});