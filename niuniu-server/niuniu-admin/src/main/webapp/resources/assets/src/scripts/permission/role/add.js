/*
 * product list  --douzy
 */
define(function(require,exports,module){
    var $=require("$"),Util=require("util");
    var util=new  Util();
    util.ajaxGlobalSetting();
    require("ace-elements");
    require("validation")($);
    var newRoleForm=$("#newRoleForm");
    var newRoleButton=$("#newRoleButton");
    var func = {
        init : function(data) {
            var self = this;
            newRoleForm.validation(function(obj,params){
            },{reqmark:false} );
            newRoleButton.bind('click', function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick:function(event){
            if (newRoleForm.valid(this,"error!")==false){
                return false;
            }
            newRoleForm.submit();
        },
    };
    func.init();
});