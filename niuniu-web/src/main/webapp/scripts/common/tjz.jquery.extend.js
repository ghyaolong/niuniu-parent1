jQuery.fn.extend({
    //固定table中的表头
    //作者:wxj
    //height：table的高度
    //rowNum：table的表头占几行（几个tr）
    klxFixedRow: function (height, rowNum) {
        this.each(function () {
            var jO = $(this);
            if (jO[0].tagName.toLowerCase() == 'table') {
                jO.wrap('<div style="position: relative;">')
                    .wrap('<div style="display: inline-block;overflow-y:auto;overflow-x:hidden;width:100%;height:' + height + 'px;">');
                jO.width(function (i, w) {
                    return w;
                });
                jO.find("th").width(function (i, w) {
                    return w;
                });
                var tb = jO.clone(true).removeAttr("id");

                tb.find("tr").slice(rowNum).remove();
                jO.after(tb.css({"position": "absolute", "top": "0"}));
            } else {
                alert("klxFixed方法目前只支持对table元素使用！");
            }
        });
    },
    //表格排序函数
    //作者:wxj
    //eventTr=触发点击事件的行
    //fixedRow=不需要排序的前几行
    //fun：自定义排序规则
    klxSort: function (eventTr, fixedRow, sortFun, cellBack) {
        fixedRow = fixedRow - 1;
        this.each(function () {
            var jO = $(this);
            if (jO[0].tagName.toLowerCase() == "table") {
                var tb = jO;
                tb.find("tr:eq(" + eventTr + ") th").css("cursor", "pointer")
                    .click(function () {
                        $(".sort").removeClass("sort");
                        $(this).addClass("sort");
                        var idx = $(this).index();
                        var trArr = tb.find("tr:gt(" + fixedRow + ")").toArray();
                        trArr.sort(function (tr1, tr2) {
                            var value = false;
                            if (jQuery.isFunction(sortFun)) {
                                value = sortFun.call(this, idx, tr1, tr2);
                            }
                            if (value == false) {
                                var a = $.trim($(tr1).find("td").eq(idx).text()).replace("%", "");
                                var b = $.trim($(tr2).find("td").eq(idx).text()).replace("%", "");
                                value = b - a;
                            }
                            return value;
                        });
                        tb.children("tr:gt(" + fixedRow + ")").remove();
                        tb.append(trArr);
                        if (jQuery.isFunction(cellBack)) {
                            cellBack.call(this);
                        }
                    });
            } else {
                alert("该方法只支持table元素");
            }
        });
    },

    klxHover: function () {
        this.css("cursor", "pointer");
        this.on("click", function () {
            $(this).toggleClass("seleced");
        });
        this.hover(function () {
            $(this).addClass("trHover");
        }, function () {
            $(this).removeClass("trHover");
        });
    },

        tjzBind:function(arr){
        	debugger;
        for(var key in arr){
            if(this.is("select")){
                if(arr[key].selected){
                    this.append( "<option value='"+arr[key].value+"' selected='"+arr[key].selected+"'>"+arr[key].text+"</option>");
                }else{
                    this.append( "<option value='"+arr[key].value+"'>"+arr[key].text+"</option>");
                }
            }
            else if(this.is(".radio-group")){
                if(arr[key].selected){
                    this.append( '<label> <input type="radio" name="'+this.attr("name")+'" checked="checked" value="'+arr[key].value+'"/>'+arr[key].text+'</label>');
                }else{
                    this.append( '<label> <input type="radio" name="'+this.attr("name")+'" value="'+arr[key].value+'"/>'+arr[key].text+'</label>');
                }
            }
            else if(this.is(".checkbox-group")){
                if(arr[key].selected){
                    this.append( '<label> <input type="checkbox" name="'+this.attr("name")+'" checked="checked" value="'+arr[key].value+'"/>'+arr[key].text+'</label>');
                }else{
                    this.append( '<label> <input type="checkbox" name="'+this.attr("name")+'" value="'+arr[key].value+'"/>'+arr[key].text+'</label>');
                }
                //this.append('<input type="hidden" name="'+this.attr("name")+'"/>')
                //var cbox=this;
                //this.click(function(){
                //    cbox.find("input:hidden").val(function(){
                //        var vs="";
                //        cbox.find("input:checked").each(function(){
                //            vs+=this.value+",";
                //        });
                //        return vs;
                //    });
                //});
            }
        }
    }
});