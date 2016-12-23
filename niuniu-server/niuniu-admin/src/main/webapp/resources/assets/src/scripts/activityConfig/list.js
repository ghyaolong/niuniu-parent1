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
            $(".examine").click(function(){
                $("#examineId").val($(this).attr("data-id"));
                $("#examineModal").modal("show");
            });

        }
    }

    function teacherTr(data){
 
        var str="";
        var html="";
        var photo = data.prizePic;
        var photoStr = "";
	    for(var i=0; i<photo.length;i++){
	    	photoStr += photo[i];
	    	if(i != photo.length){
	    		photoStr += ",";
	    	}
	    }
	    var rank = ""
	    	if(data.rank == 100){
	    		rank = "特等奖";
	    	}
	    if(data.rank == 1){
    		rank = "一等奖";
    	}
	    if(data.rank == 2){
    		rank = "二等奖";
    	}
	    if(data.rank == 3){
    		rank = "三等奖";
    	}
	    if(data.rank == 4){
    		rank = "四等奖";
    	}
        html+='<tr class="odd">'+
        '<td class=" ">'+data.id+'</td>'+
        '<td class=" ">'+data.advertId+'</td>'+
        '<td class=" ">'+data.scope+'</td>'+
        '<td class=" ">'+rank+'</td>'+
        '<td class=" ">'+data.numMax+'</td>'+
        '<td class=" ">'+data.prize+'</td>'+
        '<td class=""><a data-content="' + photo + '" class="advertCon" >查看</a></td>'+
    
        '<td class=" ">'+
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
                relaodDate(data.activityConfigs);
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