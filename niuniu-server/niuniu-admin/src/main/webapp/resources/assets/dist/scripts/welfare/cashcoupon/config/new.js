define("banjiajia/web/1.0.0/scripts/welfare/cashcoupon/config/new",["$","util","tree","ace-elements","validation"],function(require,exports,module){var $=require("$"),Util=require("util"),util=new Util;util.ajaxGlobalSetting();var tree=require("tree"),courseTree=new tree;require("ace-elements"),require("validation")($);var newUserForm=$("#newUserForm"),newUserButton=$("#newUserButton"),phoneInput=$("#phoneInput");$(".date-picker").datepicker({autoclose:!0}).next().on(ace.click_event,function(){$(this).prev().focus()});var J=$;$("#avatarUpload").dropzone({url:_contentPath+"/common/resource/image/upload.json",maxFiles:10,maxFilesize:3,previewsContainer:!1,init:function(){this.on("success",function(file,data){var image=eval("("+data+")");console.log("File "+image.image.url+"uploaded"),J("#avatarInput").val(image.image.url),J("#avatarUpload").attr("src",image.image.url)}),this.on("removedfile",function(a){console.log("File "+a.name+"removed")})}});var func={init:function(){var a=this;courseTree.init(1),newUserForm.validation(function(){},{reqmark:!1}),newUserButton.bind("click",function(b){a.userButtonClick(b)})},userButtonClick:function(){return 0==newUserForm.valid(this,"error!")?!1:($("#denominationInp").val(util.priceY($("#denominationInp").val())),newUserForm.submit(),void 0)}};func.init()});
