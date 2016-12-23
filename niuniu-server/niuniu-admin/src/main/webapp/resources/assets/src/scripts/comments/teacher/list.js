/*
 * product list  --douzy
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
            $(".ace-switch-6").click(function(){
                if(confirm("确定要更改?"))
                {
                    updState($(this).attr("data-id"));
                }
            });
            pageSize = result.page.length;
            var option = {
                totalPages: result.page.total,
                currentPage: result.page.current,
                callback: fetchData
            };
            Pager.init($(".pagination"), option);
        }
    }

    function updState(id)
    {
        $.ajax({
            url: _contentPath+'/comments/teacher/upd/state/'+id+'.json',
            type:'POST',
            dataType: 'json',
            success: function(data){

            },
            error: function(jqXHR , textStatus , errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }

    function teacherTr(data){
        var teacherScore=data.teacherScore;
        var teacher=data.teacher;
        var content=teacherScore.userComment.length>20?teacherScore.userComment.substring(0,20)+"......":teacherScore.userComment;
        var html="";
        html+='<tr class="odd">'+
        '<td class=" "><a data-content="'+teacherScore.userComment+'" data-trigger="focus" data-toggle="popover"  role="button"  tabindex="0" title="内容详细">'+content+'</a></td>'+
        '<td class="">'+teacherScore.userName+'</td>'+
        '<td class=" ">'+teacher.name+'</td>'+
        '<td class=" ">'+teacherScore.starScore+'</td>'+
        '<td class=" ">'
        +"专业:"+teacherScore.knowledge+"</br>服务:"+teacherScore.service+"</br>守时:"+teacherScore.punctuality+
        '</td>'+
        '<td class=" ">'+util.getLocalTime(teacherScore.createTime/1000)+'</td>'+
        '<td class=" ">'
        +'<input type="hidden" name="state" id="stateInput">'
        +'<input type="checkbox" data-id="'+teacherScore.id+'" id="stateId" '+(teacherScore.state==1?"checked='true'":"")+' class="ace ace-switch ace-switch-6">'
        +'<span class="lbl"></span>'+
        '</td>'+
        '</tr>';
        return html;
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    function searchBtn()
    {

        $("#serchBtn").click(function(){
            var filed=$(this).attr("data-fild");
            parmasArr=new Array();
            parmasArr.push(filed+"="+$("#searchValue").val())
            fetchData(null,null,null,1);
        });
    }
    function fetchData(event, originalEvent, type,page){
        var begin=(parseInt(page)-1)*pageSize;
        $.ajax({
            url: _contentPath+'/comments/teacher/list/query.json?begin='+begin+"&"+parmasArr.join("&"),
            async: true,
            dataType: 'json',
            success: function(data){
                relaodDate(data.teacherComments);
            },
            error: function(jqXHR , textStatus , errorThrown){
                console.log("error="+errorThrown);
            }
        });
    }
    var func = {
        init : function() {
            util.ajaxGlobalSetting();
            searchBtn();
            fetchData(null,null,null,0);
            var plugin_bootstrap=new plugin().bootstrap();
            plugin_bootstrap.dropdownInIt();
        }
    };

    func.init();

});