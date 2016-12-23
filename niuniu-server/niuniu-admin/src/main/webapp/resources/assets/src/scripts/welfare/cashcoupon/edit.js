/*
 * product list  --douzy
 */
define(function(require,exports,module){
    var $=require("$");
    var tree=require("tree");
    var courseTree=new tree();
    var Util=require("util");
    var util=new  Util();
    require("ace-elements");
    require("validation")($);
    require("bootstrao-datetimepicker");
    var cashcouponForm=$("#cashcouponForm");
    var cashcouponButton=$("#cashcouponButton");
    $('.form_datetime').datetimepicker({format: 'yyyy-mm-dd hh:ii:ss'});
    function timTime(dt) {
        var create = dt.substring(0, 19).replace(/-/g, '/');
        return new Date(create).getTime();
    }
    var func = {
        init : function(data) {
            var self = this;
            courseTree.init(1);
            cashcouponForm.validation(function(obj,params){
                var create = $("#createTime").val();
                var end=$(obj).val();
                if(timTime(create)>timTime(end)) {
                    params.err=true;
                    params.msg="超时时间应大于创建时间";
                }
            },{reqmark:false} );
            cashcouponButton.bind('click', function(event) {
                self.userButtonClick(event);
            });
        },
        userButtonClick:function(event){
            if (cashcouponForm.valid(this,"error!")==false){
                return false;
            }
            $("#denominationInp").val(util.priceY($("#denominationInp").val()));
            cashcouponForm.submit();
        }
    };
    func.init();
});