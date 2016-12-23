/*
 * product list  --douzy
 */
define(function(require,exports,module){
    var $=require("$");
    require("ace-elements");
    require("validation")($);
    var  Map=require("map");
    var shopDistrictMap=new Map();
    var newTeacherForm=$("#newTeacherForm");
    var newTeacherButton=$("#newTeacherButton");
    var phoneInput=$("#phoneInput");
    var queryKey=$("#queryKey");
    $(".chosen-select").chosen();
    $('#form-field-select-4').addClass('tag-input-style');
    $('#shoppingDistrictsSelect').addClass('tag-input-style');
    $('.search-field input').height(25);
    $('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
        $(this).prev().focus();
    });

    var func = {

        init : function(data) {
            var self = this;

            newTeacherForm.validation(function(obj,params){
            },{reqmark:false} );
            newTeacherButton.bind('click', function(event) {
                self.teacherButtonClick(event);
            });
        },
        teacherButtonClick:function(event){

            if (newTeacherForm.valid(this,"error!")==false){
                //$("#error-text").text("error!"); 1.0.4版本已将提示直接内置掉，简化前端。

                return false;
            }
            $("#stateInput").val($('#stateId[type="checkbox"]:checked').length);
            newTeacherForm.submit();
        }
    };

    func.init();




});