/*
 * product list  --douzy
 */
define(function(require, exports, module) {
	var $ = require("$");
	require("ace-elements");
	require("validation")($);
	var tree = require("tree");
	var courseTree = new tree();
	var Map = require("map");
	var shopDistrictMap = new Map();
	$.fn.datetimepicker.dates['zh-CN'] = {
			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			today: "今天",
			suffix: [],
			meridiem: ["上午", "下午"]
	};
	
	$('.date-picker').datetimepicker({language: 'zh-CN',todayBtn: 1,autoclose:true}).next().on(ace.click_event, function(){
		$(this).prev().focus();
	}); 

	var J = $;
	
	$("#prizePic4").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
            	var image=eval('(' + data + ')');
            	var flag = image.success;
            	if(flag == false){
            		alert("图片过大，上传失败");
            		return;
            	};
                $("#photoUrl4").val(image.image.url);
                $("#prizePic4").css({width:"100px",height:"100px"});
                $("#prizePic4").attr("src",image.image.url);
                
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });

	$("#prizePic3").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
            	var image=eval('(' + data + ')');
            	var flag = image.success;
            	if(flag == false){
            		alert("图片过大，上传失败");
            		return;
            	};
                $("#photoUrl3").val(image.image.url);
                $("#prizePic3").attr("src",image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
	 
	$("#prizePic2").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
            	var image=eval('(' + data + ')');
            	var flag = image.success;
            	if(flag == false){
            		alert("图片过大，上传失败");
            		return;
            	};
                $("#photoUrl2").val(image.image.url);
                $("#prizePic2").attr("src",image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
	$("#prizePic1").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
            	var image=eval('(' + data + ')');
            	var flag = image.success;
            	if(flag == false){
            		alert("图片过大，上传失败");
            		return;
            	};
                $("#photoUrl1").val(image.image.url);
                $("#prizePic1").attr("src",image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
	
	$("#prizePic0").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
            	var image=eval('(' + data + ')');
            	var flag = image.success;
            	if(flag == false){
            		alert("图片过大，上传失败");
            		return;
            	};
                $("#photoUrl0").val(image.image.url);
                $("#prizePic0").attr("src",image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
	$("#smallPrizePic0").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
            	var image=eval('(' + data + ')');
            	var flag = image.success;
            	if(flag == false){
            		alert("图片过大，上传失败");
            		return;
            	};
                $("#smallPhotoUrl5").val(image.image.url);
                $("#smallPrizePic0").attr("src",image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
function uploadAction(){
	
	
	$("#uploadImgs").click(function () {
		//上传照片
		var prizePic0Val=$("#prizePic0").val();
		var prizePic1Val=$("#prizePic1").val();
		var prizePic2Val=$("#prizePic2").val();
		var prizePic3Val=$("#prizePic3").val();
		var prizePic4Val=$("#prizePic4").val();
		var prizePic5Val=$("#smallPrizePic0").val();
		var suffix0Val=prizePic0Val.substring(prizePic0Val.lastIndexOf('.'));
		var suffix1Val=prizePic0Val.substring(prizePic1Val.lastIndexOf('.'));
		var suffix2Val=prizePic0Val.substring(prizePic2Val.lastIndexOf('.'));
		var suffix3Val=prizePic0Val.substring(prizePic3Val.lastIndexOf('.'));
		var suffix4Val=prizePic0Val.substring(prizePic4Val.lastIndexOf('.'));
		var suffix5Val=prizePic0Val.substring(prizePic5Val.lastIndexOf('.'));
		
		alert(suffix0Val+prizePic0Val);
		
		$.ajax({
            //提交数据的类型 POST GET
            type:"POST",
			enctype:"multipart/form-data",
            //提交的网址
            url:_contentPath+"/common/resource/image/upload.html",
            //提交的数据
            data:{suffix0:suffix0Val,prizePic0:prizePic0Val,suffix1:suffix1Val,prizePic1:prizePic1Val,suffix2:suffix2Val,prizePic2:prizePic2Val,suffix3:suffix3Val,prizePic3:prizePic3Val,suffix4:suffix4Val,prizePic4:prizePic4Val,suffix5:suffix5Val,smallPrizePic0:prizePic5Val},
            success:function(data){
					alert("上传成功!!");
            },
            error:function(data){
            	
            	alert("上传失败!!");
            	
            }
         });
	});
	
		
		
		}
	
	var func = {
        init : function() {
        	uploadAction();
        }
    };
 

	func.init();

});