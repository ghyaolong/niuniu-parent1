/*
 * resume  --douzy
 */
define(function(require,exports,module){
    var $=require("$")
        ,Handlebars= require("handlebars")
        ,Util=require("util");
    var util=new  Util();

    require("ace-elements");
    function resume()
    {
        this.$addApendTemplate=$("#addApend-Template")
        this.$addSuccLabelTemplate=$("#addSuccLabel-Template")
        this.Flag=true
        this.removeFlag=true
        this.updFlag=true

    }
    /**
     * 通用title
     * @returns {{title: string, unitDescb: string, professionDescb: string}}
     */
    resume.prototype.addSetting=function(){
        return {
            title:"学习经历",
            unitDescb:"学校",
            professionDescb:"专业"
        };
    }
    /**
     * ajax 参数
     */
    resume.prototype.saveParmasSetting=function() {
        return {
            teacherId: $("#teacherId").val(),
            title: "",
            timeDescb: "",
            unitDescb: "",
            professionDescb: ""
        }
    }
    resume.prototype.init=function(){
        var self=this;
        util.ajaxGlobalSetting();
        self.certificate();
        self.loadEvent();
        self.addBox();
    }
    /**
     * 时间范围初始
     */
    resume.prototype.dateRange=function(){
        $('input[name=timeDescb]').daterangepicker({
            singleDatePicker: true,
            showDropdowns: true,
            format:'YYYY.MM',
            minDate:'1980.01',
            maxDate:'2020.01'
        }).prev().on(ace.click_event, function(){
            $(this).next().focus();
        });
    }
    /**
     * 保存
     */
    resume.prototype.saveResume=function(parmas,$labelFullObj){
        var self=this;
        if(self.Flag) {
            $.ajax({
                type: "POST",
                url: _contentPath+"/members/teacher/add/resume.json",
                data: parmas,
                dataType: "json",
                beforeSend:function(){
                    self.Flag=false;
                },
                success: function (data) {
                    if (data == "1") {
                        var addSuccLabelTemplate = self.$addSuccLabelTemplate.html();
                        var $SulLabelTemplate = Handlebars.compile(addSuccLabelTemplate);
                        $labelFullObj.$Ibox.html($SulLabelTemplate($labelFullObj));
                    }
                },complete:function() {
                    self.Flag = true;
                }
            })
        }
    }
    /**
     * 成功切换input
     */
    resume.prototype.successCheck=function(){

    }
    /**
     * save btn click
     */
    resume.prototype.saveBtn=function(){
        var self=this;
        $(".saveBtn").click(function(){
            var parmasSet=self.saveParmasSetting();
            var $iboxContent=$(this).parents("div.ibox-content");
            var boxId=$(this).attr("data-parent-id");
            parmasSet.title=$iboxContent.find("input[name='title']").val();
            parmasSet.timeDescb=$iboxContent.find("input[name='timeDescb']").val();
            parmasSet.unitDescb=$iboxContent.find("input[name='unitDescb']").val();
            parmasSet.professionDescb=$iboxContent.find("input[name='professionDescb']").val();
            var addsetting=self.getSettting(boxId);
            var labelFullObj= {
                $Ibox:$iboxContent,
                timeDescb: parmasSet.timeDescb,
                unitDescb: addsetting.unitDescb,
                unitDescbLabel: parmasSet.unitDescb,
                professionDescb: addsetting.professionDescb,
                professionDescbLabel: parmasSet.professionDescb
            }

            self.saveResume(parmasSet,labelFullObj);
        });
    }
    resume.prototype.getSettting=function(boxId){
        var self=this;
        var addsetting=self.addSetting();
        if(boxId=="work")
        {
            addsetting.title="工作经历";
            addsetting.unitDescb="公司";
            addsetting.professionDescb="职位";
        }else
        {
            addsetting.title="学习经历";
            addsetting.unitDescb="学校";
            addsetting.professionDescb="专业";
        }
        return   addsetting;
    }
    /**
     * 新增编辑窗口
     */
    resume.prototype.addBox=function() {
        var self=this;
        $(".addBox").click(function () {
            var boxId=$(this).attr("data-parent-id");
            var addsetting=self.getSettting(boxId);
            var addAppendTemplate=self.$addApendTemplate.html();
            var $AddTemplate=Handlebars.compile(addAppendTemplate);
            var $IboxContent=$("#"+boxId).find("div.ibox-content");
            if(!!$IboxContent&&$IboxContent.length>0)
                $("#"+boxId).find(".ibox-content:first").before($AddTemplate(addsetting));
            else
                $("#" + boxId).find("div.ibox").append($AddTemplate(addsetting));
            self.loadEvent();
        });
    }
    /**
     * remove   btn
     */
    resume.prototype.removeBox=function(){
        var self=this;
        $(".removeBtn").click(function(){
            var $iboxContent=$(this).parents("div.ibox-content");
            self.removePost({
                resuMeId: $(this).attr("data-id"),
                ibox:$iboxContent
            });
        });
    }
    /**
     * remove ajax
     * @param option
     */
    resume.prototype.removePost=function(option){
        var self=this;
        if(self.removeFlag) {
            $.ajax({
                type: "POST",
                url: _contentPath+"/members/teacher/remove/resume/" + option.resuMeId + ".json",
                dataType: "json",
                beforeSend:function(){
                    self.removeFlag=false;
                },
                success: function (data) {
                    if (data == "1")
                        option.ibox.remove();
                }, complete: function () {
                    self.removeFlag = true;
                }
            })
        }
    }
    /**
     * 编辑
     * @param parmas
     */
    resume.prototype.updResume=function(parmas) {
        var self=this;
        if(self.updFlag)
        {
            $.ajax({
                type: "POST",
                url: _contentPath+"/members/teacher/edit/resume.json",
                data:parmas,
                dataType: "json",
                beforeSend:function(){
                    self.updFlag=false;
                },
                success: function (data) {
                    if (data == "1")
                        location.href=location.href;
                }, complete: function () {
                    self.updFlag = true;
                }
            })
        }

    }
    /**
     * 编辑按钮
     */
    resume.prototype.updBtn=function(){
        var self=this;
        $(".updBtn").click(function(){
            var parmasSet=self.saveParmasSetting();
            var $iboxContent=$(this).parents("div.ibox-content");
            var boxId=$(this).attr("data-parent-id");
            parmasSet.id=$(this).next(".removeBtn").attr("data-id");
            parmasSet.title=$iboxContent.find("input[name='title']").val();
            parmasSet.timeDescb=$iboxContent.find("input[name='timeDescb']").val();
            parmasSet.unitDescb=$iboxContent.find("input[name='unitDescb']").val();
            parmasSet.professionDescb=$iboxContent.find("input[name='professionDescb']").val();
            var addsetting=self.getSettting(boxId);
            var labelFullObj= {
                $Ibox:$iboxContent,
                id:$(this).next(".removeBtn").attr("data-id"),
                timeDescb: parmasSet.timeDescb,
                unitDescb: addsetting.unitDescb,
                unitDescbLabel: parmasSet.unitDescb,
                professionDescb: addsetting.professionDescb,
                professionDescbLabel: parmasSet.professionDescb
            }
            self.updResume(parmasSet,labelFullObj);
        });
    }
    resume.prototype.loadEvent=function(){
        var self=this;
        self.dateRange();
        self.cancelBox();
        self.saveBtn();
        self.removeBox();
        self.updBtn();
    }
    /**
     * 取消编辑窗口
     */
    resume.prototype.cancelBox=function(){
        $(".cancelBtn").click(function(){
            $(this).parents("div.ibox-content").remove();
        });
    }

    /**
     * 证书
     */
    resume.prototype.certificate=function(){
        function init()
        {
            $Box.init();
            $profile.init();
        }
        var $Box= {
            init:function () {
                var slef=this;
                $(".addBoxPro").click(function () {
                    var boxId = $(this).attr("data-parent-id");
                    var addAppendTemplate = $("#addprofile-Template").html();
                    var $IboxContent = $("#" + boxId).find("div.ibox-content");
                    if (!!$IboxContent && $IboxContent.length > 0)
                        $("#" + boxId).find(".ibox-content:first").before(addAppendTemplate);
                    else
                        $("#" + boxId).find("div.ibox").append(addAppendTemplate);
                    slef.cancel();
                    $profile.init();
                });
            },
            cancel:function(){
                $(".cancelProBtn").click(function(){
                    $(this).parents("div.ibox-content").remove();
                });
            }
        }
        var saveFlag=true
            ,updFlag=true
            ,removeFlag=true;
        var $profile={
            init:function(){
                var self=this;
                self.click();
                self.editClick();
                self.removeBox();
                self.selInIt();
            },
            selInIt:function() {
                var $profileSel = $(".profileSel");
                $profileSel.each(function(i){
                    $(this).val($(this).attr("data-val"));
                });
            },
            parmasSetting:function(){
                return {
                    teacherId: $("#teacherId").val(),
                    profileName: "",
                    profileNum: "",
                    profileId:1,
                    descb: ""
                }
            },
            click:function() {
                var self = this;
                $(".saveProBtn").click(function () {
                    var parmasSet = self.parmasSetting();
                    var $iboxContent = $(this).parents("div.ibox-content");
                    var boxId = $(this).attr("data-parent-id");
                    parmasSet.profileName = $iboxContent.find("input[name='profileName']").val();
                    parmasSet.profileNum = $iboxContent.find("input[name='profileNum']").val();
                    parmasSet.descb = $iboxContent.find("textarea[name='descb']").val();
                    parmasSet.profileId = $iboxContent.find("select[name='profileN']").val();
                    var labelFullObj = {
                        $Ibox: $iboxContent,
                        profileName: parmasSet.profileName,
                        profileNum: parmasSet.profileNum,
                        descb: parmasSet.descb,
                        profileId: $iboxContent.find("select[name='profileN']").find("option:selected").text()
                    }
                    self.save(parmasSet, labelFullObj);
                });
            },
            save:function(parmas,$labelFullObj){
                 if(saveFlag)
                 {
                     $.ajax({
                         type: "POST",
                         url: _contentPath+"/members/teacher/add/profile.json",
                         data:parmas,
                         dataType: "json",
                         beforeSend:function(){
                             saveFlag=false;
                         },
                         success: function (data) {
                             if (data == "1")
                             {
                                 var addSuccLabelTemplate = $("#addprofileLabel-Template").html();
                                 var $SulLabelTemplate = Handlebars.compile(addSuccLabelTemplate);
                                 $labelFullObj.$Ibox.html($SulLabelTemplate($labelFullObj));
                             }
                         }, complete: function () {
                             saveFlag = true;
                         }
                     })
                 }
            },
            editClick:function(){
                var self=this;
                $(".updProBtn").click(function(){
                    var parmasSet=self.parmasSetting();
                    var $iboxContent=$(this).parents("div.ibox-content");
                    var boxId=$(this).attr("data-parent-id");
                    parmasSet.id=$(this).next(".removeProBtn").attr("data-id");
                    parmasSet.profileName = $iboxContent.find("input[name='profileName']").val();
                    parmasSet.profileNum = $iboxContent.find("input[name='profileNum']").val();
                    parmasSet.descb = $iboxContent.find("textarea[name='descb']").val();
                    parmasSet.profileId = $iboxContent.find("select[name='profileN']").val();
                    var labelFullObj = {
                        $Ibox: $iboxContent,
                        profileName: parmasSet.profileName,
                        profileNum: parmasSet.profileNum,
                        descb: parmasSet.descb,
                        profileId: $iboxContent.find("select[name='profileN']").text()
                    }
                    self.edit(parmasSet);
                });
            }
            ,edit:function(parmas){
                if(updFlag)
                {
                    $.ajax({
                        type: "POST",
                        url: _contentPath+"/members/teacher/edit/profile.json",
                        data:parmas,
                        dataType: "json",
                        beforeSend:function(){
                            updFlag=false;
                        },
                        success: function (data) {
                            if (data == "1")
                                location.href=location.href;
                        }, complete: function () {
                            updFlag = true;
                        }
                    })
                }
            },
            removeBox:function(){
                var self=this;
                $(".removeProBtn").click(function(){
                    var $iboxContent=$(this).parents("div.ibox-content");
                    self.remove({
                        proId: $(this).attr("data-id"),
                        ibox:$iboxContent
                    });
                });
            }
            ,remove:function(option){
                if(removeFlag) {
                    $.ajax({
                        type: "POST",
                        url: _contentPath+"/members/teacher/remove/profile/" + option.proId + ".json",
                        dataType: "json",
                        beforeSend: function () {
                            removeFlag = false;
                        },
                        success: function (data) {
                            if (data == "1")
                                option.ibox.remove();
                        }, complete: function () {
                            removeFlag = true;
                        }
                    })
                }
            }
        }
        init();
    }
    new resume().init();
});