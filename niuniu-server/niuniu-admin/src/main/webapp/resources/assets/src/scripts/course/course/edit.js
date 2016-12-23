/*
 * product list  --douzy
 */
define(function(require,exports,module){
    var $=require("$");
    require("ace-elements");
    require("validation")($);
    require("bootstrao-datetimepicker");
    var courseForm=$("#courseForm");
    var courseButton=$("#courseButton");
    var Util=require("util");
    var util=new  Util();
    $("#courseUpload").dropzone({
        url: _contentPath+'/common/resource/image/upload.json',
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer:false,
        init: function() {
            this.on("success", function(file,data) {
                var image=eval('(' + data + ')');
                console.log("File " + image.image.url + "uploaded");
                $("#courseInput").val(image.image.url);
                $("#courseUpload").attr("src",image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
    var func = {
        init : function(data) {
            util.ajaxGlobalSetting();
            var self = this;
            courseForm.validation(function(obj,params){
            },{reqmark:false} );
            courseButton.bind('click', function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick:function(event){
            if (courseForm.valid(this,"error!")==false){
                return false;
            }
            $("#stateInput").val($('#stateId[type="checkbox"]:checked').length);
            $("#courseTypeId").val($("#courseTypeSelectId").val());
            courseForm.submit();
        }
    };
    func.init();
});