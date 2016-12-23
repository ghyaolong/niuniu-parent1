/*
 * feedback list  --douzy
 */
define(function(require, exports, module) {
	var $ = require("$"), Pager = require("pager"), State = require("state");
	require("ace-elements");
	require("jggrid");
	require("gridlocale");
	require("validation")($);
	require("ace-elements");
	var state = new State();
	state.init();
	var Util = require("util");
	var util = new Util();

	var plugin = require("plugin");
	var theacertbody = $("#theacertbody");
	var pageSize = 0;
	var parmasArr = new Array();
	$('.date-picker').datepicker({
		autoclose : true
	}).next().on(ace.click_event, function() {
		$(this).prev().focus();
	});
	function relaodDate(result) {
		if (!!result) {
			var theacertbodyHtml = "";
			for ( var teacher in result.data) {
				theacertbodyHtml += teacherTr(result.data[teacher]);
			}
			$("#dataTotal").text("共检索出" + result.page.count + "条数据");
			theacertbody.html(theacertbodyHtml);
			$("[data-toggle='popover']").popover()
			pageSize = result.page.length;
			var option = {
				totalPages : result.page.total,
				currentPage : result.page.current,
				callback : fetchData
			};
			Pager.init($(".pagination"), option);
			
			$(".advertCon").click(function(){
	            var self=$(this);

	            var con=$(this).attr("data-content");

	            var $tbody=$("#refoundtbody");
	            $tbody.html("")

	            var html="";
	            var conArr=con.split(",");
	            for(var i=0;i<conArr.length;i++){
	                var con=conArr[i];
	                var html="";
	                if(con !== null || con !== undefined || con !== ''){
	                	 if(i == 0){
	                     	html+="<tr>";
	                        html+='<td><img width="100px"  height="180px"  class=" img-responsive"   src="'+con+'"></td>';
	                        html+='<td><a>修改</a></td>';
	                        html+="</tr>"
	                     }
	                }
	                $tbody.append(html);
	            }

	            $("#myModal").modal("show");
	        });
			
			$(".examine").click(function() {
				$("#examineId").val($(this).attr("data-id"));
				$("#examineModal").modal("show");
			});
			/* 通过按钮的处理函数 */
			$(".btn-success")
					.click(
							function() {
								var examineId = parseInt($("#examineId").val());
								if (examineId > 0) {
									$.ajax({
										url : _contentPath + '/business/audit/success.json?id=' + examineId,
										async : true,
										dataType : 'json',
										success : function(data) {
											if (!!data) {
												$("#examineModal")
														.modal("hide");
												// var result=
												// util.JsonEval(data);
												// return false;
												// if(result>0){
												location = location;
												// alert(成功);
												// }
											}
										},
										error : function(jqXHR,
												textStatus, errorThrown) {
											console.log("error="
													+ errorThrown);
										}
									});
								}
							});
			$(".btn-danger")
					.click(
							function() {
								var examineId = parseInt($("#examineId").val());
								var pass = $("#pass").val();
								if (examineId > 0) {
									$.ajax({
												url : _contentPath
														+ '/business/audit/defeated.json',
												data : {'content' : pass,
														"id" : examineId},
												async : true,
												dataType : 'json',
												success : function(data) {
													if (!!data) {
														// var result=
														// util.JsonEval(data);
														// if(result>0){
														$("#examineModal")
																.modal("hide");
														location = location;
														// }
													}
												},
												error : function(jqXHR,
														textStatus, errorThrown) {
													console.log("error="
															+ errorThrown);
												}
											});
								}
							});
		}
	}

	function teacherTr(data) {
		var certificationType = "";
		if (data.certificationType == 1)
			certificationType = "个人认证";
		else if (data.certificationType == 2)
			certificationType = "商户认证";
		

		var html = "";
		var state = "未审核";
		if (data.state == 2) {
			state = "已审核 "
		}
		if (data.state == 3) {
			state = "审核失败"
		}
		if (data.state == 4) {
			state = "失效"
		}
		if (data.state == 5) {
			state = "再次提交审核"
		}

		var certificationTypeStr = "商户认证";
		if (data.certificationType == 1) {
			certificationTypeStr = "个人认证"
		}

		var businessSiteTT = "";
		try {

			if (data.businessSite != null && data.businessSite.length > 0) {
				businessSiteTT = util.JsonEval(data.businessSite);

				businessSiteTT = businessSiteTT.length == 0 ? ''
						: (businessSiteTT[0].site + '  ' + businessSiteTT[0].siteName);
			}
		} catch (e) {
			businessSiteTT = data.businessSite;
		}
		html += '<tr class="odd">' + 
		'<td class=" ">'+ (data.id != null ? data.id : "") + '</td>'+ 
		'<td class=" ">'+ (data.phone != null ? data.phone : "") + '</td>'+ 
		'<td class=" ">'+ (data.businessName != null ? data.businessName : "")+ '</td>'+ 
		'<td class=" ">'+ (data.businessAddress != null ? data.businessAddress : "")+ '</td>'+ 
		'<td class=" ">'+ (data.businessTel != null ? data.businessTel : "")+ '</td>'+ 
		'<td class=" ">'+ (data.applyName != null ? data.applyName : "")+ '</td>'+ 
		'<td class=" ">'+ (data.identityCard != null ? data.identityCard : "")+ '</td>'+ 
		'<td class=" ">'+ (data.province != null ? data.province : "")+ '</td>'+ 
		'<td class=" ">'+ (data.city != null ? data.city : "")+ '</td>'+ 
		'<td class=" ">'+ (data.area != null ? data.area : "")+ '</td>'+ 
		'<td class=" ">'+ '<span class="profile-picture"><img width="50px" src="' +
		data.businssCredentialsUp + '" id="avatar2" alt="" class="editable img-responsive"></span>'
				+ '<a data-content="' + data.businssCredentialsUp + '" class="advertCon" >查看原图</a>'
				+ '</td>'
				+ '<td class=" ">'
				+ '<span class="profile-picture"><img width="50px" src="'
				+ data.businessCredentialsFace
				+ '" id="avatar2" alt="" class="editable img-responsive"></span>'
				+ '<a data-content="' + data.businessCredentialsFace + '" class="advertCon" >查看原图</a>'
				+ '</td>'
				
				+ '<td class=" ">'
				+ '<span class="profile-picture"><img width="50px" src="'
				+ data.businessCredentialsDown
				+ '" id="avatar2" alt="" class="editable img-responsive"></span>'
				+ '<a data-content="' + data.businessCredentialsDown + '" class="advertCon" >查看原图</a>'
				+ '</td>'+ 
				
				'<td class=" ">'+ (data.descb != null ? data.descb : "")+ '</td>'+ 
				'<td class=" ">'+ (data.businessIndustry != null ? data.businessIndustry : "")+ '</td>'+ 
				'<td class=" ">'+ state+ '</td>'+ 
				'<td class=" ">'+ certificationType+ '</td>'+ 
				'<td class=" ">'+ (data.createTime != null ? data.createTime : "")+ '</td>'+ 
				'<td class=" ">'+
				'<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
			       ( data.state == 1 ? 
			        '<a class="green examine" href="javascript:void(0);" data-id="'+data.id+'"><i class="icon-pencil bigger-130"></i></a>'
			        :'' )+'</div>'+
			        '</td>'+
					'</tr>';
		return html;
	}

	function searchBtn() {
		
		var onload = function(){
			var filed=$(this).attr("data-fild");
			parmasArr = new Array();
			parmasArr.push("startTime=" + $("#id-date-picker-3").val());
			parmasArr.push("endTime=" + $("#id-date-picker-4").val());
			fetchData(null, null, null, 1);
		};
		
		$("#serchBtn").click(function() {
			onload();
		});
		
		$("#serchByTelBtn").click(function() {
			onload();
		});
		
	}
	function enableTooltips(table) {
		$('.navtable .ui-pg-button').tooltip({
			container : 'body'
		});
		$(table).find('.ui-pg-div').tooltip({
			container : 'body'
		});
	}
	function fetchData(event, originalEvent, type, page) {
		var begin = (parseInt(page) - 1) * pageSize;
		var businessTel = $("#businessTel").val();
		$.ajax({
	        url: _contentPath+'/business/list/certification.json?begin='+begin+("&")+'businessTel='+businessTel+("&")+parmasArr.join("&"),
	        async: true,
	        dataType: 'json',
	        success: function(data){
	        	relaodDate(data.business);
	        },
	       
	        error: function(jqXHR , textStatus , errorThrown){
	        	console.log("error="+errorThrown);
	        }
		});
	}
	var func = {
		init : function() {
			util.ajaxGlobalSetting();
			fetchData(null, null, null, 0);
			searchBtn();
		}
	};

	func.init();

});