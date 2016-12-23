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

			$(".advertCon")
					.click(
							function() {
								var self = $(this);

								var con = $(this).attr("data-content");

								var $tbody = $("#refoundtbody");
								$tbody.html("")

								var html = "";

								var conArr = con.split("@");

								for (var i = 0; i < conArr.length - 1; i++) {
									var con = conArr[i];

									var advert = con.split("|");
									html += "<tr>";
									html += '<td><img width="100px"  height="180px"  class=" img-responsive"   src="'
											+ advert[0] + '"></td>';
									html += "<td>" + advert[1] + "</td>";
									html += "<td><a href='" + advert[0]
											+ "' target='_blank'>查看原图</a></td>";
									html += "</tr>"
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
									$
											.ajax({
												url : _contentPath
														+ '/business/adopt/'
														+ examineId + '.json',
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
								if (examineId > 0) {
									$
											.ajax({
												url : _contentPath
														+ '/business/noThrough/'
														+ examineId + '.json',
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
		var state = "";
		if (data.state == 1)
			state = "正常";
		else if (data.state == 2)
			state = "已处理";
		else if (data.state == 3)
			state = "已回复";

		var html = "";
		var statStr = "未审核";
		if (data.state == 2) {
			statStr = "已审核 "
		}
		if (data.state == 3) {
			statStr = "审核失败"
		}
		if (data.state == 4) {
			statStr = "失效"
		}
		if (data.state == 5) {
			statStr = "再次提交审核"
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
		html += '<tr class="odd">' + '<td class=" ">'
				+ data.id
				+ '</td>'
				+ '<td class=" ">'
				+ data.phone
				+ '</td>'
				+ '<td class=" ">'
				+ data.businessName
				+ '</td>'
				+ '<td class=" ">'
				+ data.businessAddress
				+ ' '
				+ data.businessTel
				+ '</td>'
				+
				// '<td class=""><a data-content="" class="advertCon"
				// >查看</a></td>'+
				/*'<td class=" ">'
				+ data.businessTel
				+ '</td>'*/
				+ '<td class=" " >'
				+ data.businessSite
				+ '</td>'
				+ '<td class=" ">'
				+ data.businessQq
				+ '</td>'
				+

				'<td class=" ">'
				+ data.businessWeixin
				+ '</td>'
				+ '<td class=" ">'
				+ data.businessWeibo
				+ '</td>'
				+ '<td class=" ">'
				+ data.certificationType
				+ '</td>'
				+ '<td class=" ">'
				+ '<span class="profile-picture"><img width="50px" src="'
				+ data.businssCredentialsUp
				+ '" id="avatar2" alt="" class="editable img-responsive"></span>'
				+ '<a href="'
				+ data.businssCredentialsUp
				+ '" target="_blank">查看原图</a>'
				+ '</td>'
				+ '<td class=" ">'
				+ '<span class="profile-picture"><img width="50px" src="'
				+ data.businessCredentialsDown
				+ '" id="avatar2" alt="" class="editable img-responsive"></span>'
				+ '<a href="'
				+ data.businessCredentialsDown
				+ '" target="_blank">查看原图</a>'
				+ '</td>'
				+ '<td class=" ">'
				+ '<span class="profile-picture"><img width="50px" src="'
				+ data.businessCredentialsFace
				+ '" id="avatar2" alt="" class="editable img-responsive"></span>'
				+ '<a href="'
				+ data.businessCredentialsFace
				+ '" target="_blank">查看原图</a>'
				+ '</td>'
				+ '<td class=" ">'
				+ data.descb
				+ '</td>'
				+ '<td class=" ">'
				+ data.businessLogo
				+ '</td>'
				+ '<td class=" ">'
				+ data.businessIndustry
				+ '</td>'
				+ '<td class=" ">'
				+ data.state
				+ '</td>'
				+ '<td class=" ">'
				+ data.createTime
				+ '</td>'
				+ '<td class=" ">'
				+ data.modfiyTime
				+ '</td>'
				+ '<td class=" ">'
				+ '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'
				+ '<a class="green examine" href="javascript:void(0);" data-id="'
				+ data.id + '"><i class="icon-pencil bigger-130"></i></a>'
				+ '</div>' + '</td>' + '</tr>';
		return html;
	}

	function searchBtn() {
		$("#serchBtn").click(function() {

			parmasArr = new Array();
			parmasArr.push("startTime=" + $("#id-date-picker-3").val());
			parmasArr.push("endTime=" + $("#id-date-picker-4").val());
			fetchData(null, null, null, 1);
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
		$.ajax({
			url : ajaxUrl + begin + "&" + parmasArr.join("&"),
			async : true,
			dataType : 'json',
			success : function(data) {
				console.log("============business:" + data.business);
				relaodDate(data.business);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("error=" + errorThrown);
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