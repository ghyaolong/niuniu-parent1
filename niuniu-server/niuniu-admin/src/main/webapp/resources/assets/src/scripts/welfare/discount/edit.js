/*
 * product list  --douzy
 */
define(function(require,exports,module){
    var $=require("$");
    require("ace-elements");
    require("validation")($);
    require("bootstrao-datetimepicker");
    var saveUserForm=$("#discountForm");
    var saveUserButton=$("#discountButton");
    $('.form_datetime').datetimepicker({format: 'yyyy-mm-dd hh:ii:ss'});
    var func = {
        init : function(data) {
            var self = this;

            saveUserForm.validation(function(obj,params){
            },{reqmark:false} );
            saveUserButton.bind('click', function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick:function(event){
            if (saveUserForm.valid(this,"error!")==false){
                return false;
            }
            saveUserForm.submit();
        }
    };
    func.init();
});