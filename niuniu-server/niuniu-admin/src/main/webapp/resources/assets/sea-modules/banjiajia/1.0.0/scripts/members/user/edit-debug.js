/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/members/user/edit-debug", [ "$-debug", "ace-elements-debug", "validation-debug" ], function(require, exports, module) {
    var $ = require("$-debug");
    require("ace-elements-debug");
    require("validation-debug")($);
    var saveUserForm = $("#saveUserForm");
    var saveUserButton = $("#saveUserButton");
    var birthdayInput = $("#birthdayInput");
    $(".date-picker").datepicker({
        autoclose: true
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });
    $("#avatarUpload").dropzone({
        url: _contentPath + "/common/resource/image/upload.json",
        maxFiles: 10,
        maxFilesize: 3,
        previewsContainer: false,
        init: function() {
            this.on("success", function(file, data) {
                var image = eval("(" + data + ")");
                console.log("File " + image.image.url + "uploaded");
                J("#avatarInput").val(image.image.url);
                J("#avatarUpload").attr("src", image.image.url);
            });
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        }
    });
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
        }
    };
    func.init();
});
