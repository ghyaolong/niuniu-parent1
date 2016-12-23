define(function(require,exports,module){
    var $=require("$");
    require("ace-elements");
    require("inspinia");
    require("zTree")($);
    require("form")($);
    require("colpick")($);
    require("validation")($);
    var Util=require("util");
    var util=new  Util();

    function courseTree() {
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
    }
    courseTree.prototype.init=function() {
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
        self.picUpload();
        self.area.init();
        self.editNodePost();
    }
    courseTree.prototype.formValid=function(){
        $("#editNodeForm").validation(function(obj,params){},{reqmark:false});
    }
    /**
     * 加载树
     */
    courseTree.prototype.treeLoad=function(zNodes){
        var self=this;
        var t = self.$treeView;
        t = $.fn.zTree.init(t, self.getSetting(), zNodes);
        demoIframe = $("#testIframe");
        demoIframe.bind("load", self.loadReady);
        //var zTree = $.fn.zTree.getZTreeObj("tree");
        //zTree.selectNode(zTree.getNodeByParam("id", 101));
    }
    courseTree.prototype.getSetting=function() {
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
    courseTree.prototype.getTreeData=function() {
        var self=this;
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
    courseTree.prototype.treeEval=function(data) {
        var self=this;
        if(!!data) {
            var courseTreeList=data.courseTree;
            if(!!courseTreeList) {
                $.each(courseTreeList,function(i){
                      var courseTree=courseTreeList[i];
                      self.treeAnalytical(courseTree)
                });
            }
        }
    }
    /**
     * 树解析
     * @param courseTree
     */
    courseTree.prototype.treeAnalytical=function(courseTree) {
        var self=this;
        if(!!courseTree) {
            var course = courseTree.course;
            var courseList = courseTree.courseTreeList;
            self.courseAnalytical(course); //每级父节点解析
            self.courseListAnalytical(courseList);
        }
    }
    /**
     * 父节点解析
     * @param course
     */
    courseTree.prototype.courseAnalytical=function(course) {
         var self=this;
         if(!!course) {
             var course= {
                 id: course.id,
                 name: course.name,
                 pId: course.parentId,
                 file: course.parentId > 0 ? "core/standardData" : "",
                 type:course.nodeType,
                 open: course.parentId > 0 ? false : true

             }
             self.treeArr.push(course);
         }
    }
    /**
     * 子节点List解析
     */
    courseTree.prototype.courseListAnalytical=function(courseList){
        var self=this;
        if(!!courseList) {
            $.each(courseList,function(i){
                var courseTree=courseList[i];
                self.treeAnalytical(courseTree)
            });
        }
    }
    courseTree.prototype.loadReady=function() {
        var bodyH = demoIframe.contents().find("body").get(0).scrollHeight,
            htmlH = demoIframe.contents().find("html").get(0).scrollHeight,
            maxH = Math.max(bodyH, htmlH), minH = Math.min(bodyH, htmlH),
            h = demoIframe.height() >= maxH ? minH : maxH;
            if (h < 530) h = 530;
            demoIframe.height(h);
    }
    courseTree.prototype.addHoverDom=function(treeId, treeNode) {
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
    courseTree.prototype.removeHoverDom=function(treeId, treeNode){
        $("#addBtn_"+treeNode.tId).unbind().remove();
        $("#removeBtn_"+treeNode.tId).unbind().remove();
        $("#editBtn_"+treeNode.tId).unbind().remove();
    }
    /**
     * 获取tree对象
     * @returns {*}
     */
    courseTree.prototype.getTreeObj=function() {
        return $.fn.zTree.getZTreeObj("tree");
    }
    /**
     * 新增节点
     */
    courseTree.prototype.addNode=function(addBtn,treeNode) {
        var self = this;
        if (!!addBtn) {
            addBtn.bind("click", function () {
                if(treeNode.type==3) {
                    alert("不能在此节点下新增节点,该节点已是最后一级节点!");
                    return false;
                }
                $("#modalParentId").val(treeNode.id);
                $("#modalLevel").val(parseInt(treeNode.level) + 1);
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
    courseTree.prototype.editNode=function(editBtn,treeNode){
        var self = this;
        if (!!editBtn) {
            editBtn.bind("click",function(){
                var Id=parseInt(treeNode.id);
                self.getNode(Id,function(data){
                     if(!!data) {
                         var course=data.Course;
                        if(!!course) {
                            self.isBabyLimit=false;
                            self.isBabyCourse=false;
                            $(".babyLimitBox").remove();
                            //属于宝宝定价节点   开放宝宝定价编辑功能
                            if(parseInt(course.nodeType)==2) {
                                self.descPicUpload();
                                self.isBabyLimit=true;
                                self.boxNum = 0;
                                $("#babyCharge").show();
                                $("#modalEditBabylimit").val(course.babyLimit);

                                if(!!course.descPic)
                                    $("#descPicHref").attr("href",course.descPic);
                                $("#modalEditDescPic").val(course.descPic);

                                if (!!course.babyChargeRule) {
                                    var babyChargeRule = JSON.parse(course.babyChargeRule);
                                    self.boxNum = course.babyLimit;
                                    if(parseInt(self.boxNum)===babyChargeRule.charge.length) {
                                        self.addBoxInit(babyChargeRule);
                                    }
                                }
                                self.addBox();
                            }
                            else
                                $("#babyCharge").hide();

                            //具体课程节点
                            if(parseInt(course.nodeType)==3) {
                                self.isBabyCourse=true;
                                  self.typeIsChargeDesc(course);
                            }


                            $("#courseId").val(course.id);
                            $("#modalEditName").val(course.name);
                            $("#modalEditDescb").val(course.descb);
                            $("#modalEditPic").val(course.pic);
                            if(!!course.pic)
                                $("#picHref").attr("href",course.pic);
                            $("#modalEditUrl").val(course.url);
                            $("#modalEditPrice").val(util.fmoney(course.price/100,2));
                            //$("#modalEditCourseType").val(course.courseType);
                            $("#modalEditTimeUnit").val(course.timeUnit);
                            $("#modalEditParentId").val(course.parentId);
                            $("#modalEditLevel").val(course.level);
                            $("#modalEditParentId").val(course.parentId);
                            $("#stateInput").val(course.state);
                            $("#modalEditProvince").find("option[value='"+course.province+"']").attr("selected", true);
                            $("#modalEditProvince").change();
                            $("#modalEditCity option[value="+course.city+"]").attr("selected", true);
                            var $stateId=$("input[name='checkbox']");
                            if (course.state) {
                                $stateId.prop("checked",true);
                            } else
                                $stateId.prop("checked",false);
                            $("#modalEditLevel").val(course.nodeLevel);

                            $(".ace-switch-6").unbind("click").click(function () {
                                  var $chk=$(this);
                                if(self.Flag) {
                                    self.updStateValid(Id, function (data) {
                                        if (!!data) {
                                            //var $chk = $("[name='checkbox']");
                                            if (!!data.result) {
                                                if (course.state)
                                                    $chk.prop("checked", true);
                                                else
                                                    $chk.prop("checked",false);
                                                alert("请先更新子节点状态！");
                                            } else {
                                                $("#stateInput").val($chk.is(":checked"));
                                            }
                                        } else
                                            alert("系统错误!");
                                    });
                                }
                            });

                            self.$editModal.modal({show:true,backdrop: 'static'});


                        }
                     }
                });
            });
        }
    }
    /**
     * nodeType:3  具体课程节点
     */
    courseTree.prototype.typeIsChargeDesc=function(course){
          var self=this;
          var $babyChargeDesc=$("#babyChargeDesc");
          $babyChargeDesc.show();
          var chargeDesc= course.chargeDesc;
          if(!!chargeDesc) {
             var chargeDescObj=JSON.parse(chargeDesc);
              $("#modalEditCourseHourSize").val(course.courseHourSize);
              $("#modalEditApplyToBaby").val(chargeDescObj.apply_to_baby);
              $("#modalEditGoal").val(chargeDescObj.goal);
          }
    }
    /**
     * 课程节点  json组装
     */
    courseTree.prototype.getChargeDescObj=function() {
        var self = this;
        return {
            "apply_to_baby": $("#modalEditApplyToBaby").val(),
            "goal": $("#modalEditGoal").val()
        }
    }
    /**
     * 收费规则  初始
     * @param count
     */
    courseTree.prototype.addBoxInit=function(babyChargeRule) {
        var self=this;
        if(!!babyChargeRule) {
            var len = babyChargeRule.charge.length;
            $("#modalEditBabyDesc").val(babyChargeRule.desc);

            for (var i = 0; i < len; i++) {
                var charge = babyChargeRule.charge[i];
                self.$babyLimitContent.append(self.getAddBoxHtml(charge));
                self.eventBind();
                self.formValid();
            }
        }
    }
    /**
     * 新增收费规则
     */
    courseTree.prototype.addBox=function() {
        var self = this;
        $("#addBox").unbind("click").click(function () {
            var babyLimit = $("#modalEditBabylimit").val();
            if (!!babyLimit && parseInt(babyLimit) > 0) {
                if (self.boxNum+1 <= parseInt(babyLimit)) {
                    self.$babyLimitContent.append(self.getAddBoxHtml(null));
                    self.boxNum++;
                       self.eventBind();
                    self.formValid();
                } else
                    alert("收费规则已达上限.");
            } else
                alert("请先填写预约宝宝上限!");
        });
    }
    /**
     * 删除规则 & 取色器 事件绑定
     */
    courseTree.prototype.eventBind=function(){
        var self=this;
        self.removeBox();
        self.colorPick();
    }
    /**
     * 删除收费规则
     */
    courseTree.prototype.removeBox=function() {
        var self = this;
        $(".removeBabyLimitBox").unbind("click").click(function () {
            $(this).parents(".babyLimitBox").remove();
            if (self.boxNum - 1 > 0)
                self.boxNum--;
        });
    }
    /**
     * 收费规则 数量校验
     */
    courseTree.prototype.babyLimitValid=function() {
        var self = this;
        var result = true;
        var babyLimit = parseInt($("#modalEditBabylimit").val());
        var len = parseInt($(".babyLimitBox").length);
        result = babyLimit === len;
        var babyLimitObj = self.babyLimitParse();
        var chargeArr =babyLimitObj.charge;

        //层级价格校验
        $.each(chargeArr, function (i) {
            if (i > 0) {
                var charge = chargeArr[i]; //当前

                var chargePrev = chargeArr[i - 1];//上一个

                if (charge.price < chargePrev.price) {
                    result = false;
                    return;
                }
            }
        });
        return result;
    }
    /**
     * 取色器设置
     */
    courseTree.prototype.colorPick=function() {
        $(".fontColor").colpick({
            layout:'hex',
            submit:0,
            colorScheme:'dark',
            onChange:function(hsb,hex,rgb,el,bySetColor) {
                $(el).css('border-color', '#' + hex);
                if (!bySetColor) $(el).val("#" + hex);

            }
        });
    }
    /**
     * 收费规则 json组装
     */
    courseTree.prototype.babyLimitParse=function() {
        var chargeArr = [];
        var len = $(".babyLimitBox").length;
        for (var i = 0; i < len; i++) {
            var $babyLimitBox = $(".babyLimitBox").eq(i);

            var chargeObj = {
                "price":(util.priceY($babyLimitBox.find("input[id='babyLimitPrice']").val())),
                "size":parseInt($babyLimitBox.find("input[id='babyLimitSize']").val()),
                "font_color": $babyLimitBox.find("input[id='babyLimitFontColor']").val(),
                "title": $babyLimitBox.find("input[id='babyLimitTitle']").val()
            }
            chargeArr.push(chargeObj);
        }
        var result = {
            "desc": $("#modalEditBabyDesc").val(),
            "charge": chargeArr
        };

        return result;
    }
    courseTree.prototype.priceValid=function() {
        var self = this;
        var len = $(".babyLimitBox").length;
        for (var i = 0; i < len; i++) {
            var $babyLimitBox = $(".babyLimitBox").eq(i);
            var priStr = $babyLimitBox.find("input[id='babyLimitPrice']").val();
            var reg = /^\d+(?:\.\d{2})?$/;
            if (!reg.test(priStr) || isNaN(priStr))
                return false;
        }
        return true;
    }
    /**
     * 收费规则 HTML
     */
    courseTree.prototype.getAddBoxHtml=function(charge) {
        var pri=!!charge?charge.price/100:"";
         var size=!!charge?charge.size:"";
        var  fontColor=!!charge?charge.font_color:"";
        var title=!!charge?charge.title:"";
        return '<div class="babyLimitBox"><div class="form-group">' +
            '<label for="form-field-1" class="col-sm-3 control-label no-padding-right">价格(元)</label>' +
            '<div class="col-sm-9">' +
            '<div class="col-xs-10">' +
            '<input type="text"  value="'+pri+'" check-type="required smalDigits" required-message="价格不能为空且必须为整数" class="col-xs-10"  id="babyLimitPrice">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label for="form-field-1" class="col-sm-3 control-label no-padding-right">宝宝数量</label>' +
            '<div class="col-sm-9">' +
            '<div class="col-xs-10">' +
            '<input type="text"  value="'+size+'"  class="col-xs-10"  id="babyLimitSize" check-type="required digits" required-message="宝宝数量不能为空且必须为正整数"><a href="javascript:void(0)"><i class="fa fa-minus-square-o removeBabyLimitBox" style="display: block; margin-left: 20px; float: right; margin-top: 10px;"></i></a>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label for="form-field-1" class="col-sm-3 control-label no-padding-right">标题字体颜色</label>' +
            '<div class="col-sm-9">' +
            '<div class="col-xs-10">' +
            '<input type="text"  value="'+fontColor+'"  class="col-xs-10 fontColor"   id="babyLimitFontColor">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label for="form-field-1" class="col-sm-3 control-label no-padding-right">标题</label>' +
            '<div class="col-sm-9">' +
            '<div class="col-xs-10">' +
            '<input type="text"  value="'+title+'"  class="col-xs-10"  id="babyLimitTitle">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<hr></div>';
    }
    /**
     * 编辑节点
     */
    courseTree.prototype.editNodePost=function() {
        var self = this;
        $("#editNodeBtn").click(function () {
            //if ($("#editNodeForm").valid(this,"校验失败,请检查输入数据是否符合要求!")==false){
            //    //$("#error-text").text("error!"); 1.0.4版本已将提示直接内置掉，简化前端。
            //    return false;
            //}
            //当前节点为宝宝定价才进行数量校验
            if (self.isBabyLimit) {
                if (!self.priceValid()) {
                    alert("价格必须为整数且保留两位小数");
                    return false;
                }
                //数量相等
                if (self.babyLimitValid()) {
                    $("#babyChargeRule").val(JSON.stringify(self.babyLimitParse()));
                } else {
                    alert("预约宝宝上限与价格规则不匹配或价格填写错误,请修正!");
                    return false;
                }
            } else
                $("#babyChargeRule").val("");
            //3级类目校验
            if (self.isBabyCourse) {
                var hourSize = $("#modalEditCourseHourSize").val();
                if (isNaN(hourSize)) {
                    alert("课程节数必须为整数且大于0!");
                    return false;
                }
                if (parseInt(hourSize) <= 0) {
                    alert("课程节数必须为整数且大于0!");
                    return false;
                }
                $("#babyChargeDescHD").val(JSON.stringify(self.getChargeDescObj()));
            } else
                $("#babyChargeDescHD").val("");

            $("#modalEditPrice").val(util.priceY($("#modalEditPrice").val()));
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
    courseTree.prototype.getNode=function(Id,callbacl) {
        $.ajax({
            url: _contentPath + '/course/manage/get/node/' + Id + '.json',
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
    /**
     * 校验是否可进行状态更新
     * @param Id
     * @param callback
     */
    courseTree.prototype.updStateValid=function(Id,callback) {
        var self=this;
        $.ajax({
            url: _contentPath + '/course/manage/upd/state/valid/' + Id + '.json',
            async: true,
            dataType: 'json',
            beforeSend:function(){
                self.Flag=false;
            },
            complete:function(){
                self.Flag=true;
            },
            success: function (data) {
                callback(data)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    /**
     * 图片上传
     */
    courseTree.prototype.picUpload=function() {
        $("#picUpload").dropzone({
            url: _contentPath + '/common/resource/image/upload.json',
            maxFiles: 10,
            maxFilesize: 3,
            previewsContainer: false,
            init: function () {
                this.on("success", function (file, data) {
                    var image = eval('(' + data + ')');
                    $("#modalEditPic").val(image.image.url);
                    //$("#picUpload").attr("src", image.image.url);
                    $("#picHref").attr("href",image.image.url);
                    alert("上传成功");
                });
                this.on("removedfile", function (file) {
                    console.log("File " + file.name + "removed");
                });
            }
        });
    }
    courseTree.prototype.descPicUpload=function(){
        $("#descPicUpload").dropzone({
            url: _contentPath + '/common/resource/image/upload.json',
            maxFiles: 10,
            maxFilesize: 3,
            previewsContainer: false,
            init: function () {
                this.on("success", function (file, data) {
                    var image = eval('(' + data + ')');
                    $("#modalEditDescPic").val(image.image.url);
                    //$("#picUpload").attr("src", image.image.url);
                    $("#descPicHref").attr("href",image.image.url);
                    alert("上传成功");
                });
                this.on("removedfile", function (file) {
                    console.log("File " + file.name + "removed");
                });
            }
        });
    }
    courseTree.prototype.area= {
        //***********select 对象初始*************************
        $province: $("#modalEditProvince"), //省 select
        $city: $("#modalEditCity"),         //市 select
        ck_province:"",
        ck_city:"",
        provinces:null,
        nowProvince :null,
        nowCities:null,
        nowCity :null,
        nowDistricts:null,
        init: function () {
            var self=this;
            self.getArea();
        },
        getArea: function () {
            var self = this;
            $.ajax({
                url: _contentPath + '/resources/assets/sea-modules/gallery/common/locationarea.js',
                async: true,
                dataType: 'json',
                success: function (data, textStatus) {
                    self.provinces = data.locationArae;
                    self.bindProvinceDate();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    //console.log("error=" + errorThrown);
                }
            });
        },
        bindProvinceDate: function () {
            var self = this;
            self.$province.html("");
            var temp = "";
            for (var int = 0; int < self.provinces.length; int++) {
                var province = self.provinces[int];
                temp += ' <option val="' + province.np.p
                + '" value="' + province.np.pn
                + '">' + province.np.pn + '</option>';
            }
            self.$province.html(temp);
            self.$province.bind('change', function () {
                self.provinceOnChange();
            });
        },
        provinceOnChange: function () {
            var self = this;
            var selectValue = self.$province.val();
            for (var int = 0; int < self.provinces.length; int++) {
                var provinceid = self.provinces[int].np.pn;
                if (provinceid == selectValue) {
                    self.nowProvince = self.provinces[int];
                    self.bindCityData();
                    break;
                }
            }
        },
        bindCityData: function () {
            var self = this;
            self.$city.html("");
            self.nowCities = self.nowProvince.cs;
            var cities = self.nowCities;
            var temp = "";
            for (var int = 0; int < cities.length; int++) {
                var city = cities[int];
                temp += ' <option val="' + city.nc.c
                + '" value="' + city.nc.cn
                + '" >' + city.nc.cn + '</option>';
            }
            self.$city.html(temp);
            self.nowCity = self.nowCities[0];
        }
    }
    $(document).ready(function(){
        new courseTree().init();
    });
});