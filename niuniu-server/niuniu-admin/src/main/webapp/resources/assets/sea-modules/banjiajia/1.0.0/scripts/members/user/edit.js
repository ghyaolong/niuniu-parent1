define("banjiajia/web/1.0.0/scripts/members/user/edit",["$","ace-elements","validation"],function(require,exports,module){var $=require("$");require("ace-elements"),require("validation")($);var saveUserForm=$("#saveUserForm"),saveUserButton=$("#saveUserButton"),birthdayInput=$("#birthdayInput");$(".date-picker").datepicker({autoclose:!0}).next().on(ace.click_event,function(){$(this).prev().focus()}),$("#avatarUpload").dropzone({url:_contentPath+"/common/resource/image/upload.json",maxFiles:10,maxFilesize:3,previewsContainer:!1,init:function(){this.on("success",function(file,data){var image=eval("("+data+")");console.log("File "+image.image.url+"uploaded"),J("#avatarInput").val(image.image.url),J("#avatarUpload").attr("src",image.image.url)}),this.on("removedfile",function(a){console.log("File "+a.name+"removed")})}});var J=$,func={init:function(){var a=this;saveUserForm.validation(function(){},{reqmark:!1}),saveUserButton.bind("click",function(b){a.userButtonClick(b)})},userButtonClick:function(){return 0==saveUserForm.valid(this,"error!")?!1:(saveUserForm.submit(),void 0)}};func.init()});