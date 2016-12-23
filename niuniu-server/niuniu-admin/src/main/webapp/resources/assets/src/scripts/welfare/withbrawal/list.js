/*
 * teacher list  --douzy
 */
define(function(require,exports,module){
    var $=require("$"),Pager=require("pager"),State=require("state");;
    require("ace-elements");
    require("jggrid");
    require("gridlocale");
    var theacertbody = $("#theacertbody");
    var lastPage=0;
    var pageSize=0;
    var Util=require("util");
    var util=new  Util();
    var state=new State();
    state.init(_contentPath + '/system/config/get/withbrawalState.json');




    function teacherTr(data){
        var user=data.user;
        var withdrawalHistoryOrder=data.withdrawalHistoryOrder;
        var html="";
        html+='<tr class="odd">'+
        '<td class=" ">'+
            '<a href="'+_contentPath+'/members/user/show/'+user.phone+'.html" target="_blank">'+user.name+'</a>'+
        '</td>'+
        '<td class="">'+util.fmoney(withdrawalHistoryOrder.price/100,2)+'</td>'+
        '<td class=" ">'+withdrawalHistoryOrder.account+'</td>'+
        '<td class=" ">'+(withdrawalHistoryOrder.withdrawalChannel==1?"微信":"支付宝")+'</td>'+
        //'<td class=" ">'+withdrawalHistoryOrder.thirdPartyPayNo+'</td>'+
        '<td class=" ">'+withdrawalHistoryOrder.createTime+'</td>'+
        '<td class=" ">'+state.get(state.OrderProcessState(),withdrawalHistoryOrder.state)+'</td>'+
        '<td class=" ">'+util.fmoney(withdrawalHistoryOrder.freezeBalance/100,2)+'</td>'+
        '<td class=" ">'+
        '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' +
        (withdrawalHistoryOrder.state!=2?
            ('<a class="green applyWithdrawals" title="提现审核" data-wid="' + withdrawalHistoryOrder.id + '">' +
            '<i class="icon-legal bigger-130"></i>' +
            '</a>'+
            '<a class="green" href="'+_contentPath+'/withdrawals/edit/'+withdrawalHistoryOrder.id+'.html">'+
            '<i class="icon-pencil bigger-130"></i>'+
            '</a>'
            ):"")+
        //'<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
        //'<a class="green" href="'+_contentPath+'/withdrawals/edit/'+withdrawalHistoryOrder.id+'.html">'+
        //'<i class="icon-pencil bigger-130"></i>'+
        //'</a>'+
        //'</div>'+
        '</div>'+
        '</td>'+
        '</tr>';

        return html;
    }

    function fetchData(event, originalEvent, type, page){
        var begin=(parseInt(page)-1)*pageSize;

        $.ajax({
            url: _contentPath+'/withdrawals/list/query.json?begin='+begin,
            async: true,
            dataType: 'json',
            success: function(data){
                lastPage=page+1;
                relaodDate(data.WithdrawalHistoryOrders);
            },
            error: function(jqXHR , textStatus , errorThrown){
                console.log("error="+errorThrown);
            }
        });
    }
    function  relaodDate(result){
        var theacertbodyHtml="";
        if(!result)
            return false;
        for ( var teacher in result.data) {
            theacertbodyHtml+=teacherTr(result.data[teacher]);
        }
        theacertbody.html(theacertbodyHtml);
        $(".applyWithdrawals").click(function(){
            if(confirm("是否通过审核?")) {
                var wid = parseInt($(this).attr("data-wid"));
                if (wid > 0) {
                    $("#wid").val(wid);
                    $("#alipayForm").submit();

                    //$.ajax({
                    //    url: _contentPath + '/welfare/withdrawal/alipay/api.json',
                    //    async: true,
                    //    data: {wid: wid},
                    //    type: 'GET',
                    //    dataType: 'json',
                    //    success: function (data) {
                    //        console.log(data);
                    //        if (!!data) {
                    //            if (data.result) {
                    //                $("#modalWithdrawalPrice").val("");
                    //                fetchData(null, null, null, lastPage);
                    //            } else
                    //                alert("申请提现失败,请确保输入金额在可提范围!");
                    //            $("#myModal").modal("hide");
                    //        }
                    //    },
                    //    error: function (jqXHR, textStatus, errorThrown) {
                    //        console.log("error=" + errorThrown);
                    //    }
                    //});
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