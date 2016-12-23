/**
 * douzy
 */
define(function(require,exports,module) {
    var $=require("$"),Handlebars= require("handlebars");
    require("zTree")($);
    var Util=require("util");
    var util=new  Util();
    var permissionInitArr=new Array();

    function userShow() {
        this.page=0;
        this.userId=$("#userId").val();
        this.pageSize=10;
        this.$IboxContent=$("#iboxContent");
        this.$CashFlowTemplate=$("#CashFlow-Template");
        this.$masCount=$("#masCount");
        this.$loadNextPage=$("#loadNextPage");
        this.Flag=true;
        this.year=2016;
    }
    userShow.prototype.searchByYear=function() {
        var self=this;
        $("#yearDropDown li").click(function(){
            var checkText=$(this).children("a").text();
            $(this).attr("class","active");
            $(this).siblings().removeClass();
            var $dropBtn=$(this).parent().prev("button");
            $dropBtn.html(checkText+"<span class=\"caret\"></span>");
            $("#yearDropDown").attr("data-chk",$(this).attr("data-val"));
            self.year=$(this).attr("data-val");
            self.get()
        });
    }
    userShow.prototype.init=function() {
        var self = this;
        self.get();
        self.nextPage();
        self.bindRole();
        self.selectPermission();
        self.searchByYear();
    }
    userShow.prototype.get=function() {
        var self = this;
        var parmas = self.getParmas();
        $.ajax({
            url: _contentPath + '/members/user/show/cashflow.json',
            data: parmas,
            dataType: 'json',
            beforeSend:function(){
                self.Flag=false;
                //self.$loadNextPage.hide();
            },
            success: function (data) {
                if(!!data) {
                    var CashFlows = data.userCashFlows;
                    if(parseInt(CashFlows.page.current)+1>=CashFlows.page.total) {
                        self.$loadNextPage.addClass("disabled");
                        self.$loadNextPage.html("<i class=\"fa fa-arrow-down\"></i> 全部加载完成");
                    }
                    else {
                        self.$loadNextPage.show();
                    }
                    self.pageSize=CashFlows.page.length;
                    self.$masCount.text("共" + CashFlows.page.count + "条记录");
                    var cashFlowTemplate = self.$CashFlowTemplate.html();
                    Handlebars.registerHelper("cashFlowType", function (value) {
                        return parseInt(value) == 1 ? "充值" : "消费";
                    });
                    Handlebars.registerHelper("linkOrder", function (value) {
                        return _contentPath + "/order/detail/" + value + ".html"
                    });
                    var tempCompile = Handlebars.compile(cashFlowTemplate);
                    self.$IboxContent.append(tempCompile(CashFlows));
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            },complete:function() {
                self.Flag = true;
            }
        });
    }
    userShow.prototype.getParmas=function() {
        var self = this;
        return {
            begin: self.page*self.pageSize,
            userId: self.userId,
            year:self.year
        }
    }
    userShow.prototype.nextPage=function() {
        var self = this;
        $("#loadNextPage").click(function () {
            if(self.Flag) {
                self.page = self.page + 1;
                self.get();
            }
        });
    }
    /**
     * 分配权限事件绑定
     */
    userShow.prototype.bindRole=function() {
        var self = this;
        $("#saveRole").click(function () {
            var roleArr = self.getCheckRole();
            if (roleArr.length > 0) {
                console.log(roleArr.join(""));
                $.ajax({
                    url: _contentPath + '/role/save/userRole.json',
                    data: {userPhone: $("#roleUserPhone").val(), roles: roleArr.join(",")},
                    type: 'POST',
                    dataType: 'json',
                    beforeSend: function () {
                    },
                    success: function (data) {
                        var obj=util.JsonEval(data);
                        if(!!obj){
                            switch (parseInt(obj.result)) {
                                case 0:
                                    alert("权限分配成功!");
                                    location=location;
                                    break;
                                case 1:
                                    alert("用户不存在!");
                                    break;
                                case 2:
                                    alert("原角色数据无法删除!");
                                    break;
                                case 3:
                                    alert("部分权限分配成功,个别分配失败,请刷新重新分配!");
                                    location=location;
                                    break;
                            }
                        }
                    }
                });
            } else
                alert("请至少选择一项需要分配的权限!");
        });
    }
    /**
     * 获取当前选中权限
     * @returns {Array}
     */
    userShow.prototype.getCheckRole=function() {
        var chkRole = new Array();
        $('input:checkbox[class=ace]').each(function () {
            if ($(this).is(":checked"))
                chkRole.push(parseInt($(this).attr("data-id")));
        });
        return chkRole;
    }
    /**
     * 查看角色权限
     */
    userShow.prototype.selectPermission=function(){
        $(".selRole").click(function(){
            var code=parseInt($(this).attr("data-id"));
            if(!!code&&code>0) {
                $.ajax({
                    url:_contentPath+'/rolePermission/getPermission/'+code+'.json',
                    async: true,
                    dataType: 'json',
                    type:'GET',
                    success: function (data) {
                        if(!!data) {
                            var obj= util.JsonEval(data);
                            if(!!obj.result) {
                                permissionInitArr=new Array();
                                var  rolePermissions=obj.result;
                                $.each(rolePermissions,function(i){
                                    var rolePermission=rolePermissions[i];
                                    permissionInitArr.push(rolePermission.permissionId);
                                });
                                $("#myModal").modal({show:true,backdrop: 'static'});
                                permissionTree.getTreeData();
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("error=" + errorThrown);
                    }
                });
            }

        });
    }
    var treeArr=new Array();
    var permissionTree={
        getSetting:function(){
            var self=this;
            return {
                check: {
                    enable: true
                },
                edit:{
                    showRenameBtn:true,
                    showRemoveBtn:true
                }
                ,
                view: {
                    //addHoverDom: function(treeId, treeNode){self.addHoverDom(treeId, treeNode)},
                    //removeHoverDom: self.removeHoverDom,
                    dblClickExpand: false,
                    showLine: true,
                    selectedMulti: false
                },
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "pId",
                        rootPId: ""
                    }
                },
                callback: {
                    beforeClick: function (treeId, treeNode) {
                        var zTree = $.fn.zTree.getZTreeObj("tree");
                        if (treeNode.isParent) {
                            zTree.expandNode(treeNode);
                            return false;
                        } else {
                            demoIframe.attr("src", treeNode.file + ".html");
                            return true;
                        }
                    },
                    onCheck:function(treeId, treeNode) {
                        var zTree = $.fn.zTree.getZTreeObj("tree");
                        var nodeArr = zTree.getCheckedNodes(true);
                        parmasArr=new Array();
                        var idArr = new Array();
                        $.each(nodeArr, function (i) {
                            var node = nodeArr[i];
                            idArr.push(node.id);
                        });
                        parmasArr.push(idArr.join(","));
                    }
                }
            };
        },
        treeAnalytical:function(menu){
            var self=this;
            if(!!menu) {
                self.menuAnalytical(menu); //每级父节点解析
            }
        },
        menuAnalytical:function(menu){
            var self=this;

            if(!!menu) {
                var menuObj= {
                    id: menu.id,
                    name: menu.permissionName,
                    pId: menu.permissionParent,
                    file: parseInt(menu.permissionParent) > 0 ? "core/standardData" : "",
                    open:parseInt(menu.permissionParent)>0?true:false,
                    checked:  $.inArray(menu.id, permissionInitArr) >= 0?true:false,
                    chkDisabled:true

                }
                treeArr.push(menuObj);
            }
        },
        treeLoad:function(zNodes){
            var self=this;
            var t = $("#tree");
            t = $.fn.zTree.init(t, self.getSetting(), zNodes);
            demoIframe = $("#testIframe");
            demoIframe.bind("load", self.loadReady);
        },
        treeEval:function(data){
            var self=this;
            if(!!data) {
                var menuTreeList=data.permissionTree;
                if(!!menuTreeList) {
                    $.each(menuTreeList,function(i){
                        var menuTree=menuTreeList[i];
                        self.treeAnalytical(menuTree)
                    });
                }
            }
        },
        getTreeData:function(){
            var self=this;
            $.ajax({
                url: _contentPath + '/permission/tree.json',
                async: true,
                dataType: 'json',
                success: function (data) {
                    treeArr=new Array();
                    self.treeEval(data);
                    self.treeLoad(treeArr);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("error=" + errorThrown);
                }
            });
        }
    };
    new userShow().init();
});