define(function(require,exports,module){
    var $=require("$");
    require("chosen");
    function area(option){
        //***********select 对象初始*************************
        this.$province=option.area.province; //省 select
        this.$city=option.area.city;         //市 select
        this.$district=option.area.district; //区 select
        this.$serviceDistrict=option.service.district; //商圈   select
        this.$districtVal=option.service.districtVal;
        //***********区域data存储*************************
        this.provinces=null
        ,this.nowProvince =null
        ,this.nowCities=null
        ,this.nowCity =null
        ,this.nowDistricts=null;
    }
    area.prototype.init=function(){
        var self=this;
        self.getArea();
        self.$serviceDistrict.chosen();
        self.$serviceDistrict.addClass('tag-input-style');
        self.$serviceDistrict.next("div").find("li.search-field").find("input").css({height:'30px'});

    }
    /**
     * this转换
     * @param fun
     * @param parmas
     */
    area.prototype.callToos=function(fun,parmas){
        var self=this;
        fun.call(self,parmas)
    }
    /**
     * 省   绑定
     */
    area.prototype.bindProvinceDate=function(){
        var self=this;
        self.$province.html("");
        var temp = "";
        for ( var int = 0; int < self.provinces.length; int++) {
            var province = self.provinces[int];
            temp += ' <option val="' + province.np.p
            + '" value="' + province.np.pn
            + '">' + province.np.pn + '</option>';
        }
        self.$province.html(temp);

        self.$province.bind('change', function() {
            self.provinceOnChange();
        });
    }
    area.prototype.provinceOnChange =function() {
        var self=this;
        var selectValue = self.$province.val();
        for (var int = 0; int < self.provinces.length; int++) {
            var provinceid = self.provinces[int].np.pn;
            if (provinceid == selectValue) {
                self.nowProvince = self.provinces[int];
                self.bindCityData();
                break;
            }
        }
    }
    /**
     * 市  绑定
     */
    area.prototype.bindCityData=function(){
        var self=this;
        self.$city.html("");
        self.nowCities=self.nowProvince.cs;
        var cities=self.nowCities;
        var temp = "";
        for ( var int = 0; int < cities.length; int++) {
            var city = cities[int];
            temp += ' <option val="' + city.nc.c
            + '" value="' + city.nc.cn
            + '">' + city.nc.cn + '</option>';
        }
        self.$city.html(temp);

        self.$city.bind('change', function() {
            araeself.cityOnChange();
        });
        self.nowCity=self.nowCities[0];
        self.bindDistrictData();
    }
    area.prototype.cityOnChange=function() {
        var self = this;
        var selectValue = self.$city.val();
        for (var int = 0; int < self.nowCities.length; int++) {
            var cityid = self.nowCities[int].nc.cn;
            if (cityid == selectValue) {
                self.nowCity = self.nowCities[int];
                self.bindDistrictData();
                break;
            }
        }
    }
    /**
     * 区  绑定
     */
    area.prototype.bindDistrictData=function(){
        var self=this;
        self.$district.html("");
        var districts=self.nowCity.as;
        var temp = "";
        for ( var int = 0; int < districts.length; int++) {
            var district = districts[int];
            temp += ' <option val="' + district.a
            + '"  value="' + district.an
            + '">' + district.an+ '</option>';
        }
        self.$district.html(temp);
        self.$district.unbind();
        self.districtOnChange();
        self.$district.bind('change', function() {
            self.districtOnChange();
        });
    }
    /**
     * 商圈 绑定
     */
    area.prototype.districtOnChange=function(){
                   var self=this;
        self.getDistrict();
    }
    area.prototype.getDistrict=function(){
        var self=this;
        console.log(self.$district.val());
        $.ajax({
            url : _contentPath+'/common/resource/district/list.json?&area='+self.$district.val()+'&'+new Date().getTime(),
            async : true,
            dataType : 'json',
            success : function(data, textStatus) {
                var temp = "";
                self.nowDistricts= data.districts;
                for ( var int = 0; int < self.nowDistricts.length; int++) {
                    var market = self.nowDistricts[int];
                    temp += ' <option value="' + market.name + '">' + market.name+ '</option>';
                }
                self.$serviceDistrict.html(temp);
                self.$serviceDistrict.trigger("chosen:updated");
                self.$serviceDistrict.change(function(){
                    var tempSelect= self.$serviceDistrict.val();
                    self.shoppingDistrictsAdd(tempSelect);
                });
            },
            error : function(jqXHR, textStatus, errorThrown) {
                //console.log("error=" + errorThrown);
            }
        });
    }
    /**
     *
     * @param districts
     */
    area.prototype.shoppingDistrictsAdd=function(districts)
    {
        var self=this;
        if(!!districts) {
            var disArr = new Array();
            var districtsArray = districts;
            for (var i = 0; i < districtsArray.length; i++) {
                var district = districtsArray[i];
                disArr.push(district);
            }
            if (districtsArray.length > 0) {
                self.$districtVal.val(disArr.join(","));
            }
        }
    }
    /**
     * 获取省市区数据
     */
    area.prototype.getArea=function() {
        var self=this;
        $.ajax({
            url: _contentPath + '/resources/assets/sea-modules/gallery/common/locationarea.js',
            async: true,
            dataType: 'json',
            success: function (data, textStatus) {
                self.provinces=data.locationArae;
                self.bindProvinceDate();
                //areaDistrict.init(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                //console.log("error=" + errorThrown);
            }
        });
    }
    module.exports=area;
});