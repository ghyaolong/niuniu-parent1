define(function(require,exports,module){
    var $=require("$");
    var util=require("util");
    function baiduMap()
    {
        this.index=0;
        this.adds=[];
        this.myGeo=null;
        this.city=null;
        this.map=null;
    }
    /**
     * 导入百度api
     */
    baiduMap.prototype.importBD=function(){
        //new util().include("http://api.map.baidu.com/api?v=1.5&ak=A8773a52f6d9bc0ecaea7f15acdd13e0");
    }
    //显示百度地图弹出层
    baiduMap.prototype.divtoshow=function(city,areaArr,isXY){
        var self=this;
        //self.importBD();
        div_show = document.getElementById("div_show");
        div_show.innerHTML = "<div class=\"div_show_title\"> <span class=\"div_show_font\">地图</span> <input type=\"button\" value=\"关闭\"  class=\"div_show_btn\" /> </div><div id=\"div_hotelmap\" ></div>";
        $(".div_show_btn").click(function(){
            self.close_show();
        });
        self.getshade();
        self.getpopup();
        if(!!isXY)
            self.Longlatitude(city,areaArr); //经纬度查询时   city:point   areaArr：text
        else
            self.getbaidumap(city,areaArr);
        div_show.style.zIndex = "100";
        div_show.style.display = "block";
    }
    //创建遮罩层
    baiduMap.prototype.getshade=function(){
        var div = document.createElement("div");
        div.style.width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth) + "px";
        div.style.height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) + "px";
        div.style.backgroundColor = "#000000";
        div.style.position = "absolute";
        div.style.opacity = 0.5;
        div.style.left = 0;
        div.style.top = 0;
        div.id = "tohotelshade";
        div.style.zIndex = 100;
        document.getElementById("div_body").appendChild(div);
    }
    //创建用于显示百度地图的DIV
    baiduMap.prototype.getpopup=function(){
        var div = document.createElement("div");
        div.style.width = "100%";
        div.style.height = "100%";
        div.id = "tohotelmap";
        document.getElementById("div_hotelmap").appendChild(div);
    }
    //清除弹出层和遮罩层
    baiduMap.prototype.close_show=function(){
        var tohotelshade = document.getElementById("tohotelshade");
        var tohotelmap = top.document.getElementById("tohotelmap");
        var div_show = document.getElementById("div_show");
        var div_hotelmap = document.getElementById("div_hotelmap");
        div_show.style.zIndex = "-100";
        tohotelshade.parentNode.removeChild(tohotelshade);
        tohotelmap.parentNode.removeChild(tohotelmap);
        div_show.innerHTML = "";
    }
    //通过经纬度显示百度地图
    baiduMap.prototype.getbaidumap=function(city,areaArr){
        var self=this;
        self.map = new BMap.Map("tohotelmap");


        self.map.centerAndZoom(city, 10);
        self.map.enableScrollWheelZoom(true);
        self.index = 0;
        self.adds=areaArr;
        self.city=city;
        self.myGeo = new BMap.Geocoder();
        self.addMapControl();
        self.bdGEO();
    }
    //地图控件添加函数：
     baiduMap.prototype.addMapControl=function () {
         var self=this;
         self.map.addControl(new BMap.MapTypeControl());
         //向地图中添加比例尺控件
         var ctrl_sca = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});
         self.map.addControl(ctrl_sca);
     }
    baiduMap.prototype.bdGEO=function(){
        var self=this;
        var add = self.adds[self.index];
        self.geocodeSearch(add);
        self.index++;
    }
    // 编写自定义函数,创建标注
    baiduMap.prototype.addMarker= function (point,label) {
        var self=this;
        var marker = new BMap.Marker(point);
        self.map.addOverlay(marker);
        marker.setLabel(label);
        marker.setAnimation(BMAP_ANIMATION_BOUNCE);
    }
   baiduMap.prototype.geocodeSearch=function(add) {
       var self=this;
       if (self.index < self.adds.length) {
           setTimeout(function() {
               self.bdGEO();
           },400);
       }
       self.myGeo.getPoint(add, function (point) {
           if (point) {
               var address = new BMap.Point(point.lng, point.lat);
               self.addMarker(address, new BMap.Label(add, {offset: new BMap.Size(20, -10)}));
           }
       }, self.city);
   }
    /***
     * 通过经纬度 添加标注
     * @type {baiduMap}
     */
    baiduMap.prototype.Longlatitude=function(option,txt) {
        var self = this;
        self.map = new BMap.Map("tohotelmap");
        self.map.centerAndZoom(new BMap.Point(option.point.lng, option.point.lat), 17);
        self.map.enableScrollWheelZoom(true);
        self.addMapControl();
        var address = new BMap.Point(option.point.lng, option.point.lat);
        self.addMarker(address, new BMap.Label(option.point.txt, {offset: new BMap.Size(20, -10)}));

        self.myGeo=new BMap.Geocoder();
        console.log(option.adds.add);
        self.myGeo.getPoint(option.adds.add,function(point){
            console.log(point.lng);
            console.log(point.lat);
            var address = new BMap.Point(point.lng, point.lat);
            self.addMarker(address, new BMap.Label(option.adds.txt, {offset: new BMap.Size(20, -10)}));
        });
    }

    module.exports=baiduMap;
});