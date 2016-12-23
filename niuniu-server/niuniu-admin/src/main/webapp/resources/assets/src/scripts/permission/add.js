/*
 * product list  --douzy
 */
define(function(require,exports,module){
    var $=require("$"),Util=require("util");
    var util=new  Util();
    util.ajaxGlobalSetting();
    require("ace-elements");
    require("validation")($);
    var newPermissionForm=$("#newPermissionForm");
    var newPermissionButton=$("#newPermissionButton");
    var func = {
        init : function(data) {
            var self = this;
            newPermissionForm.validation(function(obj,params){
            },{reqmark:false} );
            newPermissionButton.bind('click', function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick:function(event){
            if (newPermissionForm.valid(this,"error!")==false){
                return false;
            }
            newPermissionForm.submit();
        },
    };
    func.init();
});