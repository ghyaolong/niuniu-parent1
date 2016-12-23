/*
 * order list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/order/order/list-debug", [ "$-debug", "pager-debug", "state-debug", "ace-elements-debug", "jggrid-debug", "gridlocale-debug", "validation-debug", "form-debug", "util-debug", "plugin-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Pager = require("pager-debug"), State = require("state-debug");
    require("ace-elements-debug");
    require("jggrid-debug");
    require("gridlocale-debug");
    require("validation-debug")($);
    require("ace-elements-debug");
    require("form-debug")($);
    var state = new State();
    state.init(_contentPath + "/system/config/get/order_state.json");
    var detailState = new State();
    detailState.init(_contentPath + "/system/config/get/order_detail_state.json");
    var Util = require("util-debug");
    var util = new Util();
    var lastPage = 1;
    var plugin = require("plugin-debug");
    var theacertbody = $("#theacertbody");
    var pageSize = 0;
    var Flag = true;
    var parmasArr = new Array();
    function relaodDate(result) {
        if (!!result) {
            var theacertbodyHtml = "";
            for (var teacher in result.data) {
                theacertbodyHtml += teacherTr(result.data[teacher]);
            }
            $("#dataTotal").text("共检索出" + result.page.count + "条数据");
            theacertbody.html(theacertbodyHtml);
            pageSize = result.page.length;
            var option = {
                totalPages: result.page.total,
                currentPage: result.page.current,
                callback: fetchData
            };
            Pager.init($(".pagination"), option);
            $("tr.parent").css("cursor", "pointer").attr("title", "点击展开/关闭子订单").click(function() {
                var self = $(this);
                self.siblings(".child-" + this.id).toggle();
                self.siblings(".btn-" + this.id).toggle();
            });
            refundFun();
            refunForm();
        }
    }
    function teacherTr(data) {
        var html = "";
        var address = data.orderAddress.address;
        var orderDetail = data.ordersDetails;
        html += '<tr class=" parent" id="row' + data.order.orderNo + '" s=0>' + '<td class="center  sorting_1">' + "<label>" + '<input type="checkbox" class="ace">' + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class=" ">' + data.order.orderNo + "</td>" + '<td class="">' + data.order.title + "</td>" + '<td class=" ">' + data.order.userName + "</td>" + '<td class=" ">' + state.get(state.OrderProcessState(), data.order.orderState) + "</td>" + '<td class=" ">' + util.fmoney(data.order.dealPrice / 100, 2) + "</td>" + '<td class=" ">' + data.orderAddress.babysNames + "</td>" + '<td class=" ">' + data.orderAddress.badysBirthday + "</td>" + '<td class=" ">' + data.order.userId + "</td>" + '<td class=" ">' + data.orderAddress.phone + "</td>" + '<td class="" >' + '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">' + '<a class="blue" target="_blank" href="' + _contentPath + "/order/detail/" + data.order.orderNo + '.html">' + '<i class="icon-zoom-in bigger-130"></i>' + "</a>" + "</div>" + "</td>" + "</tr>";
        var isBtn = 0;
        $.each(orderDetail, function(i) {
            var odetail = orderDetail[i];
            var isRefund = parseInt(odetail.orderState) === 1 && parseInt(data.order.orderState) == 200 ? true : false;
            var obj = {
                title: odetail.title,
                state: detailState.get(detailState.OrderProcessState(), odetail.orderState),
                starTime: util.getLocalTime(odetail.startTime / 1e3),
                endTime: util.getLocalTime(odetail.endTime / 1e3),
                teacherName: odetail.teacherName,
                price: util.fmoney(odetail.orderUnitPrice / 100, 2),
                countPrice: util.fmoney(odetail.orderCountPrice / 100, 2),
                goodsCount: odetail.goodsCount,
                createTime: util.getLocalTime(odetail.createTime / 1e3)
            };
            isBtn = parseInt(odetail.orderState) === 1 ? isBtn + 1 : isBtn;
            html += '<tr class="warning child-row' + data.order.orderNo + '" style="display:none">' + '<td class="center  sorting_1">' + "<label>" + (isRefund ? '<input type="checkbox" name="refundChk' + data.order.orderNo + '" data-detail=' + odetail.orderDetailNo + " data-title=" + obj.title + " data-state=" + obj.state + ' data-start="' + obj.starTime + '" data-end="' + obj.endTime + '" data-teacherName=' + obj.teacherName + " data-create=" + obj.createTime + " data-price=" + obj.price + " data-countPrice=" + obj.countPrice + " data-goodsCount=" + obj.goodsCount + ">" : "") + '<span class="lbl"></span>' + "</label>" + "</td>" + '<td class="">' + odetail.orderDetailNo + "</td>" + '<td class="">' + odetail.title + "</td>" + '<td class="">' + odetail.teacherName + "</td>" + '<td class="">' + detailState.get(detailState.OrderProcessState(), odetail.orderState) + "</td>" + '<td class="">' + util.fmoney(odetail.unitPirce / 100, 2) + "/" + util.fmoney(odetail.countPrice / 100, 2) + "</td>" + '<td class="">' + odetail.goodsCount + "</td>" + '<td class="">' + util.getLocalTime(odetail.startTime / 1e3) + "</td>" + '<td class="">' + util.getLocalTime(odetail.endTime / 1e3) + "</td>" + '<td class="">' + util.getLocalTime(odetail.createTime / 1e3) + "</td>" + '<td class="">' + (isRefund ? "可退款" : "不可退款") + "</td>" + "</tr>";
        });
        if (parseInt(data.order.orderState) == 200 && isBtn >= 1) {
            //父订单为支付成功状态且有可退款订单 才显示退款按钮
            html += '<tr class="btn-row' + data.order.orderNo + '" style="display:none">' + '<td class="" colspan="11">' + '<button class="btn btn-large btn-block btn-primary Refund" id="Refund" type="button" data-order="' + data.order.orderNo + '">退款</button></td></tr>';
        }
        return html;
    }
    function searchBtn() {
        $("#serchBtn").click(function() {
            var filed = $(this).attr("data-fild");
            parmasArr = new Array();
            parmasArr.push(filed + "=" + encodeURI(encodeURI($("#searchValue").val())));
            parmasArr.push("orderState=" + $("#stateDropDown").attr("data-chk"));
            fetchData(null, null, null, 1);
        });
    }
    function enableTooltips(table) {
        $(".navtable .ui-pg-button").tooltip({
            container: "body"
        });
        $(table).find(".ui-pg-div").tooltip({
            container: "body"
        });
    }
    function fetchData(event, originalEvent, type, page) {
        var begin = (parseInt(page) - 1) * pageSize;
        $.ajax({
            url: _contentPath + "/order/list/query.json?begin=" + begin + "&" + parmasArr.join("&"),
            async: true,
            dataType: "json",
            success: function(data) {
                lastPage = page + 1;
                relaodDate(data.orderViews);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    function refunForm() {
        $(".refundExecBtn").unbind("click").click(function() {
            if (Flag) {
                $(this).parent().prev("div").children("form").ajaxSubmit({
                    beforeSubmit: function() {
                        Flag = false;
                    },
                    complete: function() {
                        Flag = true;
                        $("#myModal").modal("hide");
                    },
                    success: function(data) {
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
        });
    }
    /**
	 * 获取退款清单
	 */
    function refundFun() {
        $(".Refund").unbind("click").click(function() {
            var orderNo = $(this).attr("data-order");
            var refundChkArr = new Array();
            var html = "";
            var i = 0;
            $("input:checkbox[name=refundChk" + orderNo + "]").each(function() {
                if ($(this).is(":checked")) i++;
                if (i == 0) {
                    alert("请至少选择一个订单");
                    return;
                }
                refundChkArr.push($(this).attr("data-detail"));
                html += "<tr><td>" + $(this).attr("data-detail") + "</td>" + "<td>" + $(this).attr("data-state") + "</td>" + "<td>" + $(this).attr("data-price") + "</td>" + "<td>" + $(this).attr("data-goodsCount") + "</td>" + "<td>" + $(this).attr("data-countPrice") + "</td></tr>";
            });
            if (i > 0) {
                $("#refoundtbody").html(html);
                $.ajax({
                    url: _contentPath + "/order/refund/detail.json?orderNo=" + orderNo + "&detailNos=" + refundChkArr.join(","),
                    async: true,
                    dataType: "json",
                    success: function(data) {
                        if (!!data) {
                            var result = data.result;
                            if (!!result && result.state == 1) {
                                $("#modalOrderNo").val(orderNo);
                                $("#modalOrderDetailNos").val(refundChkArr.join(","));
                                $("#modalRefundPrice").val(util.fmoney(parseInt(result.content) / 100, 2));
                                $("#myModal").modal("show");
                            } else alert("退款状态异常!");
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log("error=" + errorThrown);
                    }
                });
            }
        });
    }
    var func = {
        init: function() {
            util.ajaxGlobalSetting();
            fetchData(null, null, null, 0);
            searchBtn();
            var plugin_bootstrap = new plugin().bootstrap();
            plugin_bootstrap.dropdownInIt();
            plugin_bootstrap.dropDownAlone();
        }
    };
    func.init();
});
