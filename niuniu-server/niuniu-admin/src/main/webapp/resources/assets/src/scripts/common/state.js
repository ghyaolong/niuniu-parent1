/**
 * 状态值转换
 */
define(function(require,exports,module){
    var $=require("$");
    function state(){
        this.stateObj=null;
    }
    state.prototype.init=function(url) {
        var self = this;
        $.ajax({
            url: url,
            async: true,
            dataType: 'json',
            success: function (data) {
                if (!!data)
                    self.stateObj = data;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    /**
     * 订单状态
     * @constructor
     */
    state.prototype.OrderProcessState=function() {
        var self = this;
        return self.stateObj;
    };
    state.prototype.get=function(obj,index) {
        return !!obj[index] ? obj[index] : "";
    };
    module.exports=state;
});


