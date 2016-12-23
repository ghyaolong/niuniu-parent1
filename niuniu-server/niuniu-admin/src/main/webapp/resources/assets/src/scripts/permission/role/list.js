/*
 * feedback list  --douzy
 */
define(function(require,exports,module){
    var $=require("$"),Pager=require("pager"),State=require("state");
    require("zTree")($);
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

    var permissionInitArr=new Array();
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
                            url: _contentPath + '/role/remove/' + code + '.json',
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

            $(".exchange").click(function(){
                var code=parseInt($(this).attr("attr-code"));
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
                                    saveRolePermission(code);
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
    }
     function saveRolePermission(roleId) {
         $("#saveRolePermissionBtn").unbind("click").click(function () {
             $.ajax({
                 url:_contentPath+'/rolePermission/add.json',
                 data:{roleId:roleId,permission:parmasArr.join(",")},
                 async: true,
                 dataType: 'json',
                 type:'POST',
                 success: function (data) {
                     if(!!data) {
                         var obj= util.JsonEval(data);
                         if(!!obj.result) {
                             location=location;
                             return false;
                         }
                     }
                     alert("保存失败!");
                 },
                 error: function (jqXHR, textStatus, errorThrown) {
                     console.log("error=" + errorThrown);
                 }
             });
         });
     }
    function teacherTr(data){
        var html="";
        html+='<tr class="odd">'+
        '<td class=" ">'+data.roleCode+'</td>'+
        '<td class="">'+data.roleName+'</td>'+
        '<td class=" ">'+data.roleNote+'</td>'+
        '<td class=" ">'+
        '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+
        '<a class="green" href="'+_contentPath+'/role/edit/'+data.roleId+'.html">'+
        '<i class="icon-pencil bigger-130"></i>'+
        '</a>'+
        '<a class="red exchange"  attr-code="'+data.roleId+'" title="分配权限">'+
        '<i class="icon-exchange bigger-130"></i>'+
        '</a>'+
        '<a class="red remove" href="javascript:void(0);"  attr-code="'+data.roleId+'">'+
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
            url: _contentPath+'/role/list.json?p='+begin,
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
                    checked:  $.inArray(menu.id, permissionInitArr) >= 0?true:false

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
    }
    var func = {
        init : function() {
            util.ajaxGlobalSetting();
            fetchData(null,null,null,0);
        }
    };

    func.init();


});