/*
 * teacher list  --douzy
 */
define(function(require,exports,module){
    var $=require("$"),Pager=require("pager");
    require("ace-elements");
    require("jggrid");
    require("gridlocale");
    var theacertbody = $("#theacertbody");
    var lastPage=0;
    var pageSize=0;
    var Util=require("util");
    var util=new  Util();

    function teacherTr(data){
        var teacher=data.teacher;
        var assets=data.teacherAssets;
        var html="";
        html+='<tr class="odd">'+
        '<td class="center  sorting_1">'+
        '<label>'+
        '<input type="checkbox" class="ace">'
        +'<span class="lbl"></span>'
        +'</label>'+
        '</td>'+
        '<td class=" ">'+
        '<a href="'+_contentPath+'/members/teacher/show/'+teacher.id+'.html" target="_blank">'+teacher.name+'</a>'+
        '</td>'+
        '<td class="">'+util.fmoney(assets.activeBalance/100,2)+'</td>'+
        '<td class=" ">'+util.fmoney(assets.freezeBalance/100,2)+'</td>'+
        '<td class=" ">'+util.fmoney(assets.priceCount/100,2)+'</td>'+
        '<td class=" ">'+assets.orderFinish+'</td>'+
        '<td class=" ">'+assets.serviceAverageGrade+'</td>'+
        '<td class=" ">'+assets.professionAverageGrade+'</td>'+
        '<td class=" ">'+assets.punctualityAverageGrade+'</td>'+
        '<td class=" ">'+
        '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
        '<a class="green applyWithdrawals" title="申请提现" data-tid="'+teacher.id+'">'+
        '<i class="icon-credit-card bigger-130"></i>'+
        '</a>'+
        '</div>'+
        '</td>'+
        '</tr>';

        return html;
    }

    function fetchData(event, originalEvent, type, page){
        var begin=(parseInt(page)-1)*pageSize;

        $.ajax({
            url: _contentPath+'/teacher/assets/list/query.json?begin='+begin,
            async: true,
            dataType: 'json',
            success: function(data){
                lastPage=page+1;
                relaodDate(data.teacherAssetsView);
            },
            error: function(jqXHR , textStatus , errorThrown){
                console.log("error="+errorThrown);
            }
        });
    }
    function  relaodDate(result){
        var theacertbodyHtml="";
        for ( var teacher in result.data) {
            theacertbodyHtml+=teacherTr(result.data[teacher]);
        }
        theacertbody.html(theacertbodyHtml);
        $(".applyWithdrawals").click(function(){
            if(confirm("确定申请?")) {
                var tid = parseInt($(this).attr("data-tid"));
                if (tid > 0) {
                    $("#myModal").modal("show");

                    $("#withdrawalExecBtn").unbind("click").click(function () {
                        var pri = $("#modalWithdrawalPrice").val();
                        var  withdrawAccount=$("#modalWithdrawAccount").val();
                        var  accountName=$("#modalAccountName").val();
                        if (parseInt(pri) >= 200) {
                            $.ajax({
                                url: _contentPath + '/welfare/withdrawal/apply.json',
                                async: true,
                                data: {tid: tid, pri: pri,withdrawAccount:withdrawAccount,accountName:accountName},
                                type: 'POST',
                                dataType: 'json',
                                success: function (data) {
                                    console.log(data);
                                    if (!!data) {
                                        if (data.result=="SUCCESS") {
                                            $("#modalWithdrawalPrice").val("");
                                            fetchData(null, null, null, lastPage);
                                        } else
                                            alert(data.result);
                                        $("#myModal").modal("hide");
                                    }
                                },
                                error: function (jqXHR, textStatus, errorThrown) {
                                    console.log("error=" + errorThrown);
                                }
                            });
                        } else
                            alert("提现金额需大于200元");
                    });
                }
            }
        });
        pageSize=result.page.length;
        var option={
            totalPages:result.page.total,
            currentPage:result.page.current,
            callback:fetchData
        };
        Pager.init($(".pagination"),option);
    }
    var func = {
        init : function() {
            util.ajaxGlobalSetting();
            fetchData(null, null, null, 0);
        }
    };
    func.init();
});