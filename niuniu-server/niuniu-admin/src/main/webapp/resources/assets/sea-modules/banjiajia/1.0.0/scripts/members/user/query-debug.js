/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/members/user/query-debug", [ "$-debug", "util-debug", "ace-elements-debug", "validation-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Util = require("util-debug");
    var util = new Util();
    util.ajaxGlobalSetting();
    require("ace-elements-debug");
    require("validation-debug")($);
    var queryUserForm = $("#queryUserForm");
    var queryUserButton = $("#queryUserButton");
    var queryKey = $("#queryKey");
    $(".date-picker").datepicker({
        autoclose: true
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });
    var J = $;
    var func = {
        init: function(data) {
            var self = this;
            queryUserForm.validation(function(obj, params) {}, {
                reqmark: false
            });
            queryUserButton.bind("click", function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick: function(event) {
            if (queryUserForm.valid(this, "error!") == false) {
                //$("#error-text").text("error!"); 1.0.4版本已将提示直接内置掉，简化前端。
                return false;
            }
            queryUserForm.attr("action", _contentPath + "/members/user/show/" + queryKey.val() + ".html");
            queryUserForm.submit();
        }
    };
    func.init();
});
