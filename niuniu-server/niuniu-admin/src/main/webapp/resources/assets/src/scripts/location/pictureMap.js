/**
 *  绘制 点面
 */
define(function(require,exports,module){
    var $=require("$");
    require("inspinia");
    require("ace-elements");
    var Util=require("util");
    var util=new  Util();
    var Handlebars=require("handlebars");
    var picMapDataEx=require("picMapDataEx");
    var picMapData=new picMapDataEx();
    function  pictureMap() {
        this.map = null;
        this.arr = new Array();//经纬度存储
        this.result = new Array();//结果输出
        this.$pictureBtn = $("#pictureBtn");
        this.$result = $("#result");
        this.$selCity = $("#selCity");
        this.$revoke = $("#revoke");
        this.polyanArr = new Array();
        this.$cityName= $("#city");
        //this.$polygon=self.polygon;
        this.testPoint={};
    }
    pictureMap.prototype.init=function(){
        var self=this;
        self.createMap();
        self.selRange();
        picMapData.area.init();
    }
    pictureMap.prototype.createMap=function(){
        var self=this;
        self.map = new BMap.Map("allmap");
        self.map.centerAndZoom("西安市", 12); //默认西安市
        self.map.enableScrollWheelZoom();
        self.mapClick();
        self.selCity();
        self.resultEvent();
        self.revokeEvent();
    }
    pictureMap.prototype.mapClick=function() {
        var self = this;
        self.map.addEventListener("click", function (e) {
            var pointObj = {x: e.point.lng, y: e.point.lat};
            self.arr.push(pointObj);
            self.mapPolyline(self.arr);
            self.testPoint.x=pointObj.x;
            self.testPoint.y=pointObj.y;
            $("#locationCheck").append("<br /><span>经度:" + pointObj.x + "纬度:" + pointObj.y + "</span>");
        });
    }
    pictureMap.prototype.mapPolyline=function(lineArr){
        var self=this;
        var pointArr=[];
        for(var a in lineArr){
            var pointObj=lineArr[a];
            pointArr.push(new BMap.Point(pointObj.x,pointObj.y));
        }
        var colorStr = Math.floor(Math.random() * 0xFFFFFF).toString(16).toUpperCase();
        var col="#" + "000000".substring(0, 6 - colorStr) + colorStr;
        var polygon = new BMap.Polyline(pointArr, {strokeColor:col, strokeWeight:7, strokeOpacity:0.8});  //创建多边形

        self.polyanArr.push(polygon);
        self.map.addOverlay(polygon);   //增加多边形
    }
    pictureMap.prototype.resultEvent=function(){
        var self=this;
        self.$pictureBtn.click(function(){
            // self.$result.append("</br>"+$("#sqname").val()+"</br>");
            for(var a in self.arr)
            {
                var pointObj=self.arr[a];
                self.result.push(pointObj.x+","+pointObj.y);
            }
            var point= {
                province:$("#province").val(),
                city: $("#city").val(),
                area:$("#district").val().join(","),
                name: $("#sqname").val(),
                range: self.result.join(" "),
                state: 1
            }
            self.addRange(point);
            self.removeLocal();
            //self.$result.append("</br>"+$("#sqname").val()+"|"+self.result.join(" ")+"</br>");
            self.arr=new Array();
            self.result=new Array();
            self.polyanArr=new Array();
        });
    }
    pictureMap.prototype.selCity=function(){
        var self=this;
        self.$selCity.click(function(){
            util.ajaxGlobalSetting();
            self.theLocation();
            self.getRange();
        });
    }
    pictureMap.prototype.getRange=function(){
        var self=this;
        //绘制已生成的商圈
        picMapData.getRangeByCity(self.$cityName.val(),function(data,err){
            if(!err) {
                var customDistricts=data.customDistricts;
                if(!!customDistricts) {
                    var resetArr = new Array();
                    $.each(customDistricts,function(i){//组装商圈坐标
                        var customDistrict=customDistricts[i];
                        var pointArr= customDistrict.range.split(" ");
                        $.each(pointArr,function(j){//分解坐标 转换为point对象
                            var point=pointArr[j].split(",");
                            resetArr.push({x:point[0],y:point[1]});
                        });
                        self.mapPolyline(resetArr);
                        resetArr=new Array();
                    });
                    picMapData.init(self.map);
                    self.removeLocal();
                    self.arr=new Array();
                    self.result=new Array();
                    self.polyanArr=new Array();
                }
            }else {
                alert("数据拉取失败!");
            }
        });
    }
    /**
     * 删除商圈
     */
    pictureMap.prototype.removeLocal=function(){
        var self=this;
        $(".removeLocal").unbind()
        $(".removeLocal").click(function(){
            if(confirm("确定删除此商圈?")) {
                var id= parseInt($(this).attr("data-id"));
                $.ajax({
                    url: _contentPath + '/custom/district/del/range/' + id + '.json',
                    type: "POST",
                    dataType: 'json',
                    success: function (data) {
                        if(!!data) {
                            if (!!data.result && data.result >= 1) {
                                location.reload();
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("error=" + errorThrown);
                    }
                })
            }
        });
    }
    pictureMap.prototype.addRange=function(point) {
        var self=this;
        $.ajax({
            url: _contentPath + '/custom/district/range/new.json',
            type: "POST",
            data: point,
            dataType: 'json',
            success: function (data) {
                if (!!data) {
                    if (!!data.result && data.result >= 1) {
                        var myTemplate = Handlebars.compile($("#addlocationTemplate").html());
                        $('#locationList tbody').append(myTemplate(data.customDistrict));
                        self.removeLocal();
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("error=" + errorThrown);
            }
        });
    }
    pictureMap.prototype.theLocation=function(){
        var self=this;
        var city = document.getElementById("city").value;
        if(city != ""){
            self.map.centerAndZoom(city,12);      // 用城市名设置地图中心点
        }
    }
    pictureMap.prototype.revokeEvent=function(){
        var self=this;
        self.$revoke.click(function(){
            self.arr=new Array();
            for(var polyan in self.polyanArr){
                self.map.removeOverlay(self.polyanArr[polyan]);
            }
        });
    };
    pictureMap.prototype.selRange=function(){
        var self=this;
        $("#selRange").click(function(){
            $.ajax({
                url: _contentPath + '/system/config/get/getRange.json',
                type: "GET",
                data: {city:$("#city").val(),x:self.testPoint.x,y:self.testPoint.y},
                dataType: 'json',
                success: function (data) {
                    if (!!data) {
                        var dresult=data.result;
                        if (!!dresult) {
                            $("#serviceRange").html("");
                            var str=new Array();
                            $.each(dresult,function(i){
                                str.push(dresult[i]);
                            });
                            $("#serviceRange").append("<br />坐标所属商圈集:"+(str.length<=0?"无":str.join(",")));
                        }
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("error=" + errorThrown);
                }
            });
        });
    }

    $(function(){
        new pictureMap().init();
    })
    module.exports=pictureMap;
});

