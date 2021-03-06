/*
 * feedback list  --douzy
 */
define(function(require,exports,module){
    var $=require("$"),Pager=require("pager"),State=require("state");
    require("ace-elements");
    require("jggrid");
    require("gridlocale");
    require("validation")($);
    require("ace-elements");
    var state=new State();
    state.init();
    var Util=require("util");
    var util=new  Util();

    var plugin=require("plugin");
    var theacertbody = $("#theacertbody");
    var pageSize=0;
    var parmasArr=new Array();
    $('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
		$(this).prev().focus();
	});
    function  relaodDate(result){
        if(!!result) {
            var theacertbodyHtml = "";
            for (var teacher in result.data) {
                theacertbodyHtml += teacherTr(result.data[teacher]);
            }
            $("#dataTotal").text("共检索出"+result.page.count+"条数据");
            theacertbody.html(theacertbodyHtml);
            $("[data-toggle='popover']").popover();
            pageSize = result.page.length;
            var option = {
                totalPages: result.page.total,
                currentPage: result.page.current,
                callback: fetchData
            };
            Pager.init($(".pagination"), option);

            $(".advertCon").click(function(){
                var self=$(this);

                var con=$(this).attr("data-content");

                var $tbody=$("#refoundtbody");
                $tbody.html("")

                var html="";
                var conArr=con.split("@");
                var arrLength = conArr.length - 1;
                for(var i=0;i<arrLength;i++){
                    var con=conArr[i];
                    var html="";
//                    var advert=con.split("|");
                    if(i == 0){
                    	html+="<tr>";
                        html+='<td><img width="100px"  height="180px"  class=" img-responsive"   src="'+con+'"></td>';
                        html+='<td rowspan="'+arrLength+'">'+conArr[arrLength]+'</td>';
                        html+="</tr>"
                    }else{
                    	html+="<tr>";
                        html+='<td><img width="100px"  height="180px"  class=" img-responsive"   src="'+con+'"></td>';
                        html+="</tr>"
                    }
                    $tbody.append(html);
                }

                $("#myModal").modal("show");
            });
            $(".examine").click(function(){
                $("#examineId").val($(this).attr("data-id"));
                $("#examineModal").modal("show");
            });
            $(".btn-success").click(function(){
                var examineId= parseInt($("#examineId").val());
                if(examineId>0) {
                    $.ajax({
                        url: _contentPath + '/advert/adopt/' + examineId + '.json',
                        async: true,
                        dataType: 'json',
                        success: function (data) {
                            if(!!data) {
                                $("#examineModal").modal("hide");
                             //var result= util.JsonEval(data);
                             //   if(result>0){
                                    location=location;
                                //}
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log("error=" + errorThrown);
                        }
                    });
                }
            });
            $(".btn-danger").click(function(){
                var examineId= parseInt($("#examineId").val());
                if(examineId>0) {
                    $.ajax({
                        url: _contentPath + '/advert/noThrough/' + examineId + '.json',
                        async: true,
                        dataType: 'json',
                        success: function (data) {
                            if(!!data) {
                                //var result= util.JsonEval(data);
                                //if(result>0){
                                $("#examineModal").modal("hide");
                                    location=location;
                                //}
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log("error=" + errorThrown);
                        }
                    });
                }
            });
        }
    }

    function teacherTr(data){
        var state="";
        if(data.state==1)
            state="正常";
        else if(data.state==2)
            state="已处理";
        else if(data.state==3)
            state="已回复";
        var advertObj=util.JsonEval(data.advertContent);
        var str="";
        if(advertObj!=null){
        	$.each(advertObj.photo,function(i){
        		var advert=advertObj.photo[i];
        		str+=advert+"@";
        	});
        	str+=advertObj.content;
        	/*if(str.length>0){
    			str=str.substring(0,str.length-1);
    		}*/
        }
        var html="";
        var statStr="投放中";
        if(data.state==1){
        	 statStr="投放中"
        }
        if(data.state==2){
        	statStr="审核中"
        }
        if(data.state==3){
        	statStr="投放完成"
        }
        
        if(data.state==4){
        	statStr="审核失败"
        }
        if(data.state==5){
        	statStr="超时完成"
        }
        html+='<tr class="odd">'+
        '<td class=" ">'+data.id+'</td>'+
        '<td class=" ">'+data.phone+'</td>'+
        '<td class=" ">'+data.advertName+'</td>'+
        '<td class=""><a data-content="'+str+'" class="advertCon" >查看</a></td>'+
        '<td class=" ">'+data.advertType+'</td>'+
        '<td class=" ">'+data.redEnvelopeType+'</td>'+
        '<td class=" ">'+data.redEnvelopeCount+'</td>'+
        '<td class=" ">'+util.fmoney(data.redEnvelopeAmount/100,2)+'</td>'+
        '<td class=" ">'+data.advertConditionCity+'</td>'+
        '<td class=" ">'+data.advertConditionArea+'</td>'+
        '<td class=" ">'+data.advertConditionProvince+'</td>'+
        '<td class=" ">'+data.advertCondtionDistance+'</td>'+
        '<td class=" ">'+(data.adverCondtionSex==1?"男":"女")+'</td>'+
        '<td class=" ">'+data.advertCondtionAgeScope+'</td>'+
        '<td class=" ">'+statStr+'</td>'+
        '<td class=" ">'+data.startTime+'</td>'+
        '<td class=" ">'+data.endTime+'</td>'+
        '<td class=" ">'+data.createTime+'</td>'+
        '<td class=" ">'+
        '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
       ( (data.state==1 || data.state==4 || data.state==3) ? //添加广告状态3(发送完成)操作权限，为了屏蔽违法广告信息 2016-09-23
        '<a class="green examine" href="javascript:void(0);" data-id="'+data.id+'"><i class="icon-pencil bigger-130"></i></a>'
       :'' )+'</div>'+
        '</td>'+
        '</tr>';
        return html;
    }
    
    function searchBtn() {
		$("#serchBtn").click(function () {
			
			parmasArr = new Array();
			parmasArr.push("startTime="+$("#id-date-picker-3").val());
			parmasArr.push("endTime="+$("#id-date-picker-4").val());
			fetchData(null, null, null, 1);
		});
	}
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    function fetchData(event, originalEvent, type,page){
        var begin=(parseInt(page)-1)*pageSize;
        $.ajax({
            url: ajaxUrl+begin+"&"+parmasArr.join("&"),
            async: true,
            dataType: 'json',
            success: function(data){
                relaodDate(data.adverts);
            },
            error: function(jqXHR , textStatus , errorThrown){
                console.log("error="+errorThrown);
            }
        });
    }
    var func = {
        init : function() {
            util.ajaxGlobalSetting();
            fetchData(null,null,null,0);
        	searchBtn();
        }
    };

    func.init();

});