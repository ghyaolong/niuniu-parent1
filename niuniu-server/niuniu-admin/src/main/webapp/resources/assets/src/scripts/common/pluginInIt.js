/**
 * 插件 js
 */
define(function(require,exports,module) {
   var $=require("$");
    require("ace-elements");
    function plugin() {

    }
    /**
     * bootstrap 插件集
     */
    plugin.prototype.bootstrap = function () {
        return {
            dropdownInIt: function () {
                $(".searchDropDown li").click(function(){
                    var checkText=$(this).children("a").text();
                    $(this).attr("class","active");
                    $(this).siblings().removeClass();
                    var $dropBtn=$(this).parent().prev("button");
                    $dropBtn.html(checkText+"<span class=\"caret\"></span>");
                    $("#serchBtn").attr("data-fild",$(this).attr("data-hid"));
                });
            },dropDownAlone:function() {
                $("#stateDropDown li").click(function(){
                    var checkText=$(this).children("a").text();
                    $(this).attr("class","active");
                    $(this).siblings().removeClass();
                    var $dropBtn=$(this).parent().prev("button");
                    $dropBtn.html(checkText+"<span class=\"caret\"></span>");
                    $("#stateDropDown").attr("data-chk",$(this).attr("data-val"));
                });
                $("#yearDropDown li").click(function(){
                    var checkText=$(this).children("a").text();
                    $(this).attr("class","active");
                    $(this).siblings().removeClass();
                    var $dropBtn=$(this).parent().prev("button");
                    $dropBtn.html(checkText+"<span class=\"caret\"></span>");
                    $("#yearDropDown").attr("data-chk",$(this).attr("data-val"));
                });
            }
        }
    }
    module.exports=plugin;
});