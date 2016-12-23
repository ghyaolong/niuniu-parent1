/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/order/userremiburse/edit-debug", [ "$-debug", "ace-elements-debug", "validation-debug" ], function(require, exports, module) {
    var $ = require("$-debug");
    require("ace-elements-debug");
    require("validation-debug")($);
    var saveUserForm = $("#saveUserReimburseForm");
    var saveUserButton = $("#saveUserReimburseButton");
    var J = $;
    var func = {
        init: function(data) {
            var self = this;
            saveUserForm.validation(function(obj, params) {}, {
                reqmark: false
            });
            saveUserButton.bind("click", function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick: function(event) {
            if (saveUserForm.valid(this, "error!") == false) {
                //$("#error-text").text("error!"); 1.0.4版本已将提示直接内置掉，简化前端。
                return false;
            }
            saveUserForm.submit();
            saveUserButton.remove();
        }
    };
    func.init();
});
