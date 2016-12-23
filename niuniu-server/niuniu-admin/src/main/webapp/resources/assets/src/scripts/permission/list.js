define(function(require,exports,module){
    var $=require("$"),Pager=require("pager");
    require("ace-elements");
    require("inspinia");
    require("zTree")($);
    require("form")($);
    require("colpick")($);
    require("validation")($);
    var Util=require("util");
    var util=new  Util();

    function menuTree() {
        this.treeArr=new Array()
        this.course= {
            id: 0,
            pId: 0,
            name: "",
            open: false,
            type:0,
            file:""
        }
        this.Flag=true;
        this.isBabyLimit=false    //node Type =2
        this.isBabyCourse=false   //node Type =3
        this.boxNum=0
        this.$addNodeBtn=$("#addNodeBtn")
        this.$addNodeForm=$("#addNodeForm")
        this.$myModal=$("#myModal")
        this.$editModal=$("#editModal")
        this.$treeView=$("#tree")
        this.$babyLimitContent=$("#babylimitContent")
        this.pageSize=0;
        this.theacertbody = $("#theacertbody")
    }
    menuTree.prototype.init=function() {
        var self = this;
        util.ajaxGlobalSetting();
        self.formValid();
        self.getTreeData();
        self.$addNodeBtn.click(function () {

            self.$addNodeForm.ajaxSubmit({
                success: function (data) {
                    if (!!data) {
                        location=location;
                    } else
                        alert("新增节点失败,请重试");
                    self.$myModal.modal("hide");
                }
            });
        });
        self.editNodePost();
    }
    menuTree.prototype.formValid=function(){
        $("#editNodeForm").validation(function(obj,params){},{reqmark:false});
    }
    /**
     * 加载树
     */
    menuTree.prototype.treeLoad=function(zNodes){
        var self=this;
        var t = self.$treeView;
        t = $.fn.zTree.init(t, self.getSetting(), zNodes);
        demoIframe = $("#testIframe");
        demoIframe.bind("load", self.loadReady);
        //var zTree = $.fn.zTree.getZTreeObj("tree");
        //zTree.selectNode(zTree.getNodeByParam("id", 101));
    }
    menuTree.prototype.getSetting=function() {
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
                addHoverDom: function(treeId, treeNode){self.addHoverDom(treeId, treeNode)},
                removeHoverDom: self.removeHoverDom,
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
                }
            }
        };
    }
    menuTree.prototype.getTreeData=function() {
        var self=this;
        $.ajax({
            url: _contentPath + '/permission/tree.json',
            async: true,
            dataType: 'json',
            success: function (data) {
                self.treeEval(data);
                self.treeLoad(self.treeArr);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    menuTree.prototype.treeEval=function(data) {
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
    }
    /**
     * 树解析
     * @param courseTree
     */
    menuTree.prototype.treeAnalytical=function(menu) {
        var self=this;
        if(!!menu) {
            self.menuAnalytical(menu); //每级父节点解析
        }
    }
    /**
     * 父节点解析
     * @param course
     */
    menuTree.prototype.menuAnalytical=function(menu) {
        var self=this;
        if(!!menu) {
            var menuObj= {
                id: menu.id,
                name: menu.permissionName,
                pId: menu.permissionParent,
                file: parseInt(menu.permissionParent) > 0 ? "core/standardData" : "",
                open:  parseInt(menu.permissionParent)<=0?false:true

            }
            self.treeArr.push(menuObj);
        }
    }
    menuTree.prototype.loadReady=function() {
        var bodyH = demoIframe.contents().find("body").get(0).scrollHeight,
            htmlH = demoIframe.contents().find("html").get(0).scrollHeight,
            maxH = Math.max(bodyH, htmlH), minH = Math.min(bodyH, htmlH),
            h = demoIframe.height() >= maxH ? minH : maxH;
        if (h < 530) h = 530;
        demoIframe.height(h);
    }
    menuTree.prototype.addHoverDom=function(treeId, treeNode) {
        var self=this;
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
        var addStr ="" ;
        //"<span class='button remove' id='removeBtn_" + treeNode.tId+ "' title='add node' onfocus='this.blur();'></span>"

        addStr += "<span class='button add' id='addBtn_" + treeNode.tId + "'></span>";
        addStr += "<span class='button edit' id='editBtn_" + treeNode.tId + "'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_" + treeNode.tId);
        var editbtn = $("#editBtn_" + treeNode.tId);
        self.addNode(btn,treeNode);
        self.editNode(editbtn,treeNode);
    }
    menuTree.prototype.teacherTr=function(data) {
        var html = "";
        html += '<tr class="odd">' +
        '<td class=" ">' + data.roleCode + '</td>' +
        '<td class="">' + data.roleName + '</td>' +
        '<td class=" ">' + data.roleNote + '</td>' +
        '</tr>';
        return html;
    }
    menuTree.prototype.fetchData=function(permissionId,event, originalEvent, type,page) {
        var self = this;
        var begin = (parseInt(page) - 1) * self.pageSize;
        $.ajax({
            url: _contentPath + '/role/list.json?p=' + begin,
            async: true,
            dataType: 'json',
            success: function (data) {
                self.relaodDate(data.result.content);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    menuTree.prototype.relaodDate=function(result) {
        var self = this;
        if (!!result) {
            var theacertbodyHtml = "";
            for (var teacher in result.data) {
                theacertbodyHtml += self.teacherTr(result.data[teacher]);
            }
            $("#dataTotal").text("共检索出" + result.page.count + "条数据");
            self.theacertbody.html(theacertbodyHtml);
            $("[data-toggle='popover']").popover();
            self.pageSize = result.page.length;
            var option = {
                totalPages: result.page.total,
                currentPage: result.page.current,
                callback: self.fetchData
            };
            Pager.init($(".pagination"), option);
        }
    }
    menuTree.prototype.removeHoverDom=function(treeId, treeNode){
        $("#addBtn_"+treeNode.tId).unbind().remove();
        $("#removeBtn_"+treeNode.tId).unbind().remove();
        $("#editBtn_"+treeNode.tId).unbind().remove();
    }
    /**
     * 获取tree对象
     * @returns {*}
     */
    menuTree.prototype.getTreeObj=function() {
        return $.fn.zTree.getZTreeObj("tree");
    }
    /**
     * 新增节点
     */
    menuTree.prototype.addNode=function(addBtn,treeNode) {
        var self = this;
        if (!!addBtn) {
            addBtn.bind("click", function () {
                if(treeNode.type==3) {
                    alert("不能在此节点下新增节点,该节点已是最后一级节点!");
                    return false;
                }
                $("#modalParentId").val(treeNode.id);
                self.$myModal.modal({show:true,backdrop: 'static'});
                return false;
            });
        }
    }
    /**
     * 编辑节点
     * @param editBtn
     * @param treeNode
     */
    menuTree.prototype.editNode=function(editBtn,treeNode){
        var self = this;
        if (!!editBtn) {
            editBtn.bind("click",function(){
                var Id=parseInt(treeNode.id);
                self.getNode(Id,function(data){
                    if(!!data) {
                        var menu = data.permission;
                        if (!!menu) {
                            $("#menuEditId").val(menu.id);
                            $("#permissionEditName").val(menu.permissionName);
                            $("#permissionEditCode").val(menu.permissionCode);
                            $("#permissionEditAction").val(menu.permissionAction);
                            $("#permissionEditNote").val(menu.permissionNote);
                            $("#permissionEditParent").val(menu.permissionParent);
                            $("#permissionEditType").val(menu.permissionType);
                            $("input:radio[value="+menu.permissionMenu+"]").attr('checked','true');
                            self.$editModal.modal({show:true,backdrop: 'static'});
                        }
                    }
                });
                self.fetchData(Id,null,null,null,0);
            });
        }
    }
    /**
     * 编辑节点
     */
    menuTree.prototype.editNodePost=function() {
        var self = this;
        $("#editNodeBtn").click(function () {
            //当前节点为宝宝定价才进行数量校验
            $("#editNodeForm").ajaxSubmit({
                success: function (data) {
                    if (!!data) {
                        var obj = null;
                        try {
                            obj = eval(data);
                        } catch (err) {
                            obj = eval("(" + data + ")");
                        }
                        if (parseInt(obj.result) >= 1) {
                            location.href = location.href;
                        } else
                            alert("编辑失败,请重试.");
                    }
                }
            });
        });
    }
    /**
     * 获取某个节点对象
     */
    menuTree.prototype.getNode=function(Id,callbacl) {
        $.ajax({
            url: _contentPath + '/permission/edit/' + Id + '.json',
            async: true,
            dataType: 'json',
            success: function (data) {
                callbacl(data)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    $(document).ready(function(){
        new menuTree().init();
    });
});