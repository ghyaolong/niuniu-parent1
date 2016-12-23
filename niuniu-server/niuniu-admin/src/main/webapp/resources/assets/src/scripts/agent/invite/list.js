/*
 * order list  --douzy
 */
define(function(require,exports,module){
	var $=require("$"),Pager=require("pager");
	require("ace-elements");
	require("jggrid");
	require("gridlocale");
	require("validation")($);
	require("ace-elements");
	require("form")($);


	 var Util=require("util");
	var util=new  Util();
	var lastPage=1;
	var plugin=require("plugin");
	var theacertbody = $("#theacertbody");
	var pageSize=0;
	var Flag=true;
	var parmasArr=new Array();
	function  relaodDate(result){
		if(!!result) {
			var theacertbodyHtml = "";
			for (var teacher in result.data) {
				theacertbodyHtml += teacherTr(result.data[teacher]);
			}
			$("#dataTotal").text("共检索出"+result.page.count+"条数据");
			theacertbody.html(theacertbodyHtml);
			pageSize = result.page.length;
			var option = {
				totalPages: result.page.total,
				currentPage: result.page.current,
				callback: fetchData
			};
			Pager.init($(".pagination"), option);
			$('tr.parent')
				.css("cursor","pointer")
				.attr("title","点击展开/关闭子订单")
				.click(function(){
					var self=$(this);
					self.siblings('.child-'+this.id).toggle();
					self.siblings('.btn-'+this.id).toggle();
				});
			refundFun();
			refunForm();
		}
	}
	
	function teacherTr(data){
		var html = new Array();
		html.push('<tr class=" parent" id="row'+data.id+'" s=0>');
		html.push('<td>'+data.targetPhone+'</td>');
		html.push('<td>'+data.targetNickName+'</td>');
		html.push('<td>'+(data.targetSex==1?'男':'女')+'</td>');
		html.push('<td>'+(data.targetRedpackMoney/100)+'</td>');
		html.push('<td>'+data.targetPhoneSpecification+'</td>');
		html.push('<td>'+data.targetRegisterTime+'</td>');
		html.push('<td>'+data.targetInstallTime+'</td>');
		html.push('</tr>');
		return html.join(" ");
	}
	function searchBtn() {
		$("#serchBtn").click(function () {
			parmasArr = new Array();
			if((Number($("#form-field-select-1").val())>0)){
				parmasArr.push( "agentLevel=" + $("#form-field-select-1").val());
			}
			fetchData(null, null, null, 1);
		});
	}
	function enableTooltips(table) {
		$('.navtable .ui-pg-button').tooltip({container:'body'});
		$(table).find('.ui-pg-div').tooltip({container:'body'});
	}
	function fetchData(event, originalEvent, type,page,menuId){

		var begin=(parseInt(page)-1)*pageSize;
		$.ajax({
	        url: _contentPath+'/agent/invite/list/query.json?&begin='+begin+"&"+parmasArr.join("&"),
	        async: true,
	        dataType: 'json',
	        success: function(data){
				lastPage=page+1;
	        	relaodDate(data.inviteList);
	        },
	        error: function(jqXHR , textStatus , errorThrown){
	          console.log("error="+errorThrown);
	        }
	  });
	}
   function refunForm() {
	   $(".refundExecBtn").unbind("click").click(function () {
		   if(Flag) {
			   $(this).parent().prev("div").children("form").ajaxSubmit({
				   beforeSubmit: function () {
					   Flag = false;
				   },
				   complete: function () {
					   Flag = true;
					   $("#myModal").modal("hide");
				   },
				   success: function (data) {
					   var data = util.JsonEval(data);
					   if (!!data) {
						   var result = data.result;
						   if (!!result && result.state == 1) {
							   fetchData(null, null, null, lastPage);
						   } else {
							   alert("退款失败");
						   }
					   } else {
						   alert("退款失败");
					   }
				   }
			   });
		   }
	   })
   }
   function fetchSectionData(menuId) {
		  $("#form-field-select-2").html("");
		  
		  $.ajax({
			  url: _contentPath + '/newsmenu/query.json?menuId=' + menuId + "&sysId=" + sysId,
			  async: true,
			  dataType: 'json',
			  success: function (data) {
				  if (!!data) {
					  var menuList = data.menuList;
					  var html='<option value="">全部</option>';
					  for ( var menu in menuList) {
						  html+='<option value="'+menuList[menu].id+'">'+menuList[menu].name+'</option>';
					  }
					  $("#form-field-select-2").html(html);
				  }
			  },
			  error: function (jqXHR, textStatus, errorThrown) {
				  console.log("error=" + errorThrown);
			  }
		  });

	 }
	/**
	 * 获取退款清单
	 */
	function refundFun() {
	  $(".Refund").unbind("click").click(function(){

		  var orderNo=$(this).attr("data-order");
		  var refundChkArr=new Array();
		  var html="";
		  var i=0;
		  $('input:checkbox[name=refundChk'+orderNo+']').each(function(){
			  if($(this).is(":checked"))
				  i++;
			  if(i==0) {
				  alert("请至少选择一个订单");
				  return;
			  }
			  refundChkArr.push($(this).attr("data-detail"));
			  html+="<tr><td>"+$(this).attr("data-detail")+"</td>"
				  +"<td>"+$(this).attr("data-state")+"</td>"
				  +"<td>"+$(this).attr("data-price")+"</td>"
				  +"<td>"+$(this).attr("data-goodsCount")+"</td>"
			      +"<td>"+$(this).attr("data-countPrice")+"</td></tr>";

		  });
		  if(i>0) {
			  $("#refoundtbody").html(html);
			  $.ajax({
				  url: _contentPath + '/order/refund/detail.json?orderNo=' + orderNo + "&detailNos=" + refundChkArr.join(","),
				  async: true,
				  dataType: 'json',
				  success: function (data) {
					  if (!!data) {
						  var result = data.result;
						  if (!!result && result.state == 1) {
							  $("#modalOrderNo").val(orderNo);
							  $("#modalOrderDetailNos").val(refundChkArr.join(","));
							  $("#modalRefundPrice").val(util.fmoney(parseInt(result.content) / 100, 2));
							  $("#myModal").modal("show");
						  }else
						  	alert("退款状态异常!");
					  }
				  },
				  error: function (jqXHR, textStatus, errorThrown) {
					  console.log("error=" + errorThrown);
				  }
			  });
		  }
	  });
	
	}
	var func = {
			init : function() {
				util.ajaxGlobalSetting();
				fetchData(null,null,null,0);
				searchBtn();
//				 var plugin_bootstrap=new plugin().bootstrap();
//				plugin_bootstrap.InIt();
//				plugin_bootstrap.dropDownAlone();
				$('#form-field-select-1').bind('change',function(){
					if(Number($("#form-field-select-1").val())>0){
						fetchSectionData($("#form-field-select-1").val());
					}
				});
				$("#addContentBtn").click(function(){
				
					
					window.open(_contentPath+"/user/agent/new.html");  
				});
			}
		};
		
		func.init();
		
});