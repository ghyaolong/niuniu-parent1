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

            $(".remove").click(function(){
                var code=parseInt($(this).attr("attr-code"));
                if(!!code&&code>0) {
                    if(confirm("确定删除?")) {
                        $.ajax({
                            url: _contentPath + '/permission/remove/' + code + '.json',
                            async: true,
                            dataType: 'json',
                            type:'POST',
                            success: function (data) {
                                if(parseInt(data.result)>0) {
                                    location = location;
                                }else
                                    alert("删除失败!");
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log("error=" + errorThrown);
                            }
                        })
                    }
                }
            });
        }
    }

    function teacherTr(data){
        var html="";
        html+='<tr class="odd">'+
        '<td class=" ">'+data.permissionCode+'</td>'+
        '<td class="">'+data.permissionName+'</td>'+
        '<td class="">'+data.permissionAction+'</td>'+
        '<td class=" ">'+data.permissionNote+'</td>'+
        '<td class=" ">'+
        '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
        '<a class="green" href="'+_contentPath+'/permission/edit/'+data.id+'.html">'+
        '<i class="icon-pencil bigger-130"></i>'+
        '</a>'+
        '<a class="red remove" href="javascript:void(0);" attr-code="'+data.id+'">'+
        '<i class="icon-trash bigger-130"></i>'+
        '</a>'+
        '</div>'+
        '</td>'+
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
            url: _contentPath+'/permission/list.json?p='+begin,
            async: true,
            dataType: 'json',
            success: function(data){
                relaodDate(data.result.content);
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