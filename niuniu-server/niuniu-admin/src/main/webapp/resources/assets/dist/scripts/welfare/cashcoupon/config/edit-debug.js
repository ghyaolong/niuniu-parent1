/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/welfare/cashcoupon/config/edit-debug", [ "$-debug", "util-debug", "ace-elements-debug", "validation-debug", "tree-debug" ], function(require, exports, module) {
    var $ = require("$-debug"), Util = require("util-debug");
    var util = new Util();
    require("ace-elements-debug");
    require("validation-debug")($);
    var tree = require("tree-debug");
    var courseTree = new tree();
    var newUserForm = $("#newUserForm");
    var newUserButton = $("#newUserButton");
    var phoneInput = $("#phoneInput");
    $(".date-picker").datepicker({
        autoclose: true
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });
    //	$('#avatarInput').ace_file_input({
    //		no_file:'No File ...',
    //		btn_choose:'Choose',
    //		btn_change:'Change',
    //		droppable:true,
    //		onchange:null,
    //		thumbnail:true,//| true | large
    //		//whitelist:'gif|png|jpg|jpeg'
    //		//blacklist:'exe|php'
    //		//onchange:''
    //		//
    //	});
    var J = $;
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
    var func = {
        init: function(data) {
            var self = this;
            courseTree.init(1);
            newUserForm.validation(function(obj, params) {}, {
                reqmark: false
            });
            newUserButton.bind("click", function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick: function(event) {
            if (newUserForm.valid(this, "error!") == false) {
                //$("#error-text").text("error!"); 1.0.4版本已将提示直接内置掉，简化前端。
                return false;
            }
            $("#denominationInp").val(util.priceY($("#denominationInp").val()));
            newUserForm.submit();
        }
    };
    func.init();
});
