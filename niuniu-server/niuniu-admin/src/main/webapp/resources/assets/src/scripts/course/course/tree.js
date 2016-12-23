/**
 * 课程树
 */
define(function(require,exports,module){
    var $=require("$");
    require("zTree")($);
    var Util=require("util");
    var util=new  Util();
    function tree(){
        this.type=0
        this.isChecked=false
        this.allCheckArr=new Array()
        this.treeArr=new Array()
        this.checkedArr=new Array()
        this.course= {
            id: 0,
            pId: 0,
            name: "",
            open: false,
            type:0,
            file:""
        }
        this.$treeView=$("#tree")
        this.$courseTypeId=$("#courseTypeId")
    }
    tree.prototype.init=function(t){
        var self = this;
        self.type=t;
        util.ajaxGlobalSetting();
        self.showModal();
        self.getTreeData();

    }
    tree.prototype.getTreeData=function() {
        var self = this;
        $.ajax({
            url: _contentPath + '/course/manage/tree.json',
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
    /**
     * 加载树
     */
    tree.prototype.treeLoad=function(zNodes) {
        var self = this;
        var t = self.$treeView;
        t = $.fn.zTree.init(t, self.getSetting(), zNodes);
        demoIframe = $("#testIframe");
        demoIframe.bind("load", self.loadReady);
    }
    tree.prototype.loadReady=function() {
        var bodyH = demoIframe.contents().find("body").get(0).scrollHeight,
            htmlH = demoIframe.contents().find("html").get(0).scrollHeight,
            maxH = Math.max(bodyH, htmlH), minH = Math.min(bodyH, htmlH),
            h = demoIframe.height() >= maxH ? minH : maxH;
        if (h < 530) h = 530;
        demoIframe.height(h);
    }
    /**
     * 课程树 触发按钮
     * @returns {string}
     */
    //tree.prototype.bedTreeBtn=function(e) {
    //    e.append('');
    //}
    tree.prototype.showModal=function(){
        var self=this;
        //self.bedTreeBtn(e);
        self.getCheckedArr();
        self.bedTreeModal();
        //$("#treeBtn").click(function(){
        //    $("#treeModal").modal("show");
        //});
    }
    /**
     * 嵌入 树 Modal
     */
    tree.prototype.bedTreeModal=function() {
        $(document.body).append('<div class="modal fade" id="treeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
        '<h4 class="modal-title" id="myModalLabel">课程树</h4>' +
        '</div>' +
        '<div class="modal-body">' +
        //'<ul id="tree" class="ztree" style="width:560px; overflow:auto;"></ul>'+
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>' +
        '<button type="button"  class="btn btn-primary" id="addNodeBtn">确定</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>');
    }
    /**
     * tree 设置
     * @returns {{check: {enable: boolean}, edit: {showRenameBtn: boolean, showRemoveBtn: boolean}, view: {addHoverDom: Function, removeHoverDom: (*|courseTree.getSetting.view.removeHoverDom|courseTree.removeHoverDom|.view.removeHoverDom), dblClickExpand: boolean, showLine: boolean, selectedMulti: boolean}, data: {simpleData: {enable: boolean, idKey: string, pIdKey: string, rootPId: string}}, callback: {beforeClick: Function}}}
     */
    tree.prototype.getSetting=function() {
        var self = this;
        return {
            check: {
                enable: true
            },
            view: {
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
                    if (treeNode.isParent) {
                        zTree.expandNode(treeNode);
                        return false;
                    } else {
                        demoIframe.attr("src", treeNode.file + ".html");
                        return true;
                    }
                } ,
                onCheck:function(treeId, treeNode) {
                    var zTree = $.fn.zTree.getZTreeObj("tree");
                    var nodeArr = zTree.getCheckedNodes(true);

                    var idArr = new Array();
                    $.each(nodeArr, function (i) {
                        var node = nodeArr[i];
                        idArr.push(node.id);
                    });
                    if (self.type==1&&self.allCheckArr.length==idArr.length) {
                        self.$courseTypeId.val(0);
                    } else
                        self.$courseTypeId.val(idArr.join(","));
                }
            }
        };
    }
    tree.prototype.treeEval=function(data) {
        var self = this;
        if (!!data) {
            var courseTreeList = data.courseTree;
            if (!!courseTreeList) {
                $.each(courseTreeList, function (i) {
                    var courseTree = courseTreeList[i];
                    self.treeAnalytical(courseTree)
                });
            }
        }
    }
    /**
     * 树解析
     * @param courseTree
     */
    tree.prototype.treeAnalytical=function(courseTree) {
        var self=this;
        if(!!courseTree) {
            var course = courseTree.course;
            var courseList = courseTree.courseTreeList;
            self.allCheckArr.push(course.id);
            self.courseAnalytical(course); //每级父节点解析
            self.courseListAnalytical(courseList);
        }
    }
    tree.prototype.getCheckedArr=function() {
        var self = this;
        var courseTypeStr = self.$courseTypeId.val();
        if (!!courseTypeStr) {
            //
            if (!isNaN(parseInt(courseTypeStr)) && parseInt(courseTypeStr) == 0
                && self.type == 1) {
                self.isChecked = true;
            } else
                self.isChecked = false;
            self.checkedArr = courseTypeStr.split(",");
        }
    }
    /**
     * 父节点解析
     * @param course
     */
    tree.prototype.courseAnalytical=function(course) {
        var self=this;
        if(!!course) {
            var course = {
                id: course.id,
                name: course.name,
                pId: course.parentId,
                file: course.parentId > 0 ? "core/standardData" : "",
                type: course.nodeType,
                open: course.parentId > 0 ? false : true,
                checked: $.inArray("" + (self.isChecked ? 0 : course.id) + "", self.checkedArr) >= 0 ? true : false
            }
            self.treeArr.push(course);
        }
    }
    /**
     * 子节点List解析
     */
    tree.prototype.courseListAnalytical=function(courseList) {
        var self = this;
        if (!!courseList) {
            $.each(courseList, function (i) {
                var courseTree = courseList[i];
                self.treeAnalytical(courseTree)
            });
        }
    }
    /**
     * 获取tree对象
     * @returns {*}
     */
    tree.prototype.getTreeObj=function() {
        return $.fn.zTree.getZTreeObj("tree");
    }
    module.exports=tree;
});