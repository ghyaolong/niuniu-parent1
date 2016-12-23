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


        var content=data.content.length>20?data.content.substring(0,20)+"......":data.content;
        var html="";
        html+='<tr class="odd">'+
        '<td class=" ">'+data.title+'</td>'+
        '<td class=""><a data-content="'+data.content+'" data-trigger="focus" data-toggle="popover"  role="button"  tabindex="0" title="内容详细">'+content+'</a></td>'+
        '<td class=" ">'+data.appVersion+'</td>'+
        '<td class=" ">'+data.uadetail+'</td>'+
        '<td class=" ">'+data.udid+'</td>'+
        '<td class=" ">'+data.channelId+'</td>'+
        '<td class=" ">'+data.phone+'</td>'+
        '<td class=" ">'+data.ip+'</td>'+
        '<td class=" ">'+state+'</td>'+
        '<td class=" ">'+data.createTime+'</td>'+
        '</tr>';
        return html;
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    function fetchData(event, originalEvent, type,page){
        var begin=(parseInt(page)-1)*pageSize;
        $.ajax({
            url: _contentPath+'/feedback/list/query.json?begin='+begin,
            async: true,
            dataType: 'json',
            success: function(data){
                relaodDate(data.feedBacks);
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
        }
    };

    func.init();

});