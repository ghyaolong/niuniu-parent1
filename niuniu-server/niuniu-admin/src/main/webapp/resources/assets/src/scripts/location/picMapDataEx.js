/**
 * 地图绘制   数据处理
 */
define(function(require,exports,module) {
    var $ = require("$");
    var Handlebars=require("handlebars");
    require("chosen");
    require("form")($);
    var option = {
        area: {
            province: $("#province"),
            city: $("#city"),
            district: $("#district")
        }
    };
    function pictureData() {
        this.map=null;
    }
    pictureData.prototype.init = function (map) {
        var self=this;
        self.map=map;
        $('#myModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget) // Button that triggered the modal
            var id = button.data('id'),
                province=button.data('province'),
                city=button.data('city'),
                areaArr=button.data('area'),
                name=button.data('name');
            $('#rangeId').val(id);
            $("#nameModal").val(name);
            var $areaModal=$("#areaModal");
            $areaModal.val(areaArr);
            $("#districtModal").change(function(){
                $areaModal.val("");
                $areaModal.val((!!$(this).val()?$(this).val().join(","):""));
            });
           var op={
                area: {
                    province: $("#provinceModal"),
                    city: $("#cityModal"),
                    district: $("#districtModal"),
                    areaArr:areaArr
                },check: {
                   ck_province: province,
                   ck_city: city
               }
            };
            self.editArea.init(op);
            self.save();
        });
    }
    /**
     * 根据城市名  获取商圈列表
     * @param city
     * @param callback
     * @param err
     */
    pictureData.prototype.getRangeByCity = function (city,callback,err) {
        var self=this;
        $.ajax({
            url: _contentPath + '/custom/district/get/range/' + city + '.json',
            async: true,
            dataType: 'json',
            success: function (data) {
                err=false;
                self.tableCallBack(data,err);
                if(!!callback)
                    callback(data,err);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                err=true;
                if(!!callback)
                    callback(null,err);
                console.log("error=" + errorThrown);
            }
        })
    }
    /**
     * 商圈列表
     */
    pictureData.prototype.tableCallBack=function(data,err) {
        var self=this;
        if(!err) {
            var customDistricts = data.customDistricts;
            if(!!customDistricts) {
                 var myTemplate = Handlebars.compile($("#locationTemplate").html());
                 Handlebars.registerHelper("converState",function(state,options){
                     return parseInt(state)==1?"正常":"不显示";
                 });
                 $('#locationList tbody').html(myTemplate(data));
            }
        }
    }
    /**
     * 省市区
     * @type {{init: Function}}
     */
    pictureData.prototype.area= {
        //***********select 对象初始*************************
        $province: option.area.province, //省 select
        $city: option.area.city,         //市 select
        $district: option.area.district, //区 select
        ck_province:"",
        ck_city:"",
        provinces:null,
        nowProvince :null,
        nowCities:null,
        nowCity :null,
        nowDistricts:null,
        init: function () {
            var self=this;
            $(".chosen-select").chosen();
            $("#district_chosen").find("input[class='default']").css({height:'30px'});
            self.getArea();
        },
        getArea: function () {
            var self = this;
            $.ajax({
                url: _contentPath + '/resources/assets/sea-modules/gallery/common/locationarea.js',
                async: true,
                dataType: 'json',
                success: function (data, textStatus) {
                    self.provinces = data.locationArae;
                    self.bindProvinceDate();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    //console.log("error=" + errorThrown);
                }
            });
        },
        bindProvinceDate: function () {
            var self = this;
            self.$province.html("");
            var temp = "";
            for (var int = 0; int < self.provinces.length; int++) {
                var province = self.provinces[int];
                temp += ' <option val="' + province.np.p
                + '" value="' + province.np.pn
                + '">' + province.np.pn + '</option>';
            }
            self.$province.html(temp);
            self.$province.bind('change', function () {
                self.provinceOnChange();
            });
        },
        provinceOnChange: function () {
            var self = this;
            var selectValue = self.$province.val();
            for (var int = 0; int < self.provinces.length; int++) {
                var provinceid = self.provinces[int].np.pn;
                if (provinceid == selectValue) {
                    self.nowProvince = self.provinces[int];
                    self.bindCityData();
                    break;
                }
            }
        },
        bindCityData: function () {
            var self = this;
            self.$city.html("");
            self.nowCities = self.nowProvince.cs;
            var cities = self.nowCities;
            var temp = "";
            for (var int = 0; int < cities.length; int++) {
                var city = cities[int];
                temp += ' <option val="' + city.nc.c
                + '" value="' + city.nc.cn
                + '" >' + city.nc.cn + '</option>';
            }
            self.$city.html(temp);

            self.$city.bind('change', function () {
                self.cityOnChange();
            });
            self.nowCity = self.nowCities[0];
            self.bindDistrictData();
        }, cityOnChange: function () {
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
        }, bindDistrictData: function () {
            var self = this;
            self.$district.html("");
            var districts = self.nowCity.as;
            var temp = "";
            for (var int = 0; int < districts.length; int++) {
                var district = districts[int];
                temp += ' <option val="' + district.a
                + '"  value="' + district.an
                + '" >' + district.an + '</option>';
            }
            self.$district.html(temp);
            $(".chosen-select").trigger("chosen:updated");
        }
    }


    /**
     * 省市区
     * @type {{init: Function}}
     */
    pictureData.prototype.editArea= {
        //***********select 对象初始*************************
        $province: null, //省 select
        $city: null,         //市 select
        $district: null, //区 select
        areaArr:null,
        ck_province:"",
        ck_city:"",
        provinces:null,
        nowProvince :null,
        nowCities:null,
        nowCity :null,
        nowDistricts:null,
        init: function (op) {
            var self=this;
            self.$province=!!op?op.area.province:self.$province;
            self.$city=!!op?op.area.city:self.$city;
            self.$district=!!op?op.area.district:self.$district;
            self.ck_province=!!op?op.check.ck_province:self.ck_province;
            self.ck_city=!!op?op.check.ck_city:self.ck_city;
            self.areaArr=!!op?op.area.areaArr:self.ck_city;
            $("#districtModal").chosen();
            $("#districtModal_chosen").css({width:'100px'});
            $("#districtModal_chosen").find("input[class='default']").css({height:'30px'});
            self.getArea();
        },
        getArea: function () {
            var self = this;
            $.ajax({
                url: _contentPath + '/resources/assets/sea-modules/gallery/common/locationarea.js',
                async: true,
                dataType: 'json',
                success: function (data, textStatus) {
                    self.provinces = data.locationArae;
                    self.bindProvinceDate();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    //console.log("error=" + errorThrown);
                }
            });
        },
        bindProvinceDate: function () {
            var self = this;
            self.$province.html("");
            var temp = "";
            for (var int = 0; int < self.provinces.length; int++) {
                var province = self.provinces[int];
                var chk="";
                if(self.ck_province==province.np.pn)
                    chk="selected='true'";
                temp += ' <option val="' + province.np.p
                + '" value="' + province.np.pn
                + '" '+chk+'>' + province.np.pn + '</option>';
            }
            self.$province.html(temp);
            self.$province.bind('change', function () {
                self.provinceOnChange();
            });
            if(!!self.ck_province)
                self.$province.change();
        },
        provinceOnChange: function () {
            var self = this;
            var selectValue = self.$province.val();
            for (var int = 0; int < self.provinces.length; int++) {
                var provinceid = self.provinces[int].np.pn;
                if (provinceid == selectValue) {
                    self.nowProvince = self.provinces[int];
                    self.bindCityData();
                    break;
                }
            }
        },
        bindCityData: function () {
            var self = this;
            self.$city.html("");
            self.nowCities = self.nowProvince.cs;
            var cities = self.nowCities;
            var temp = "";
            for (var int = 0; int < cities.length; int++) {
                var city = cities[int];
                var chk=""
                if(self.ck_city==city.nc.cn)
                    chk="selected='true'";
                temp += ' <option val="' + city.nc.c
                + '" value="' + city.nc.cn
                + '" '+chk+'>' + city.nc.cn + '</option>';
            }
            self.$city.html(temp);

            self.$city.bind('change', function () {
                self.cityOnChange();
            });
            if(!!self.ck_city)
                self.$city.change();
            self.nowCity = self.nowCities[0];
            self.bindDistrictData();
        }, cityOnChange: function () {
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
        }, bindDistrictData: function () {
            var self = this;
            self.$district.html("");
            var districts = self.nowCity.as;
            var temp = "";
            for (var int = 0; int < districts.length; int++) {
                var district = districts[int];
                self.areaArr
                temp += ' <option val="' + district.a
                + '"  value="' + district.an
                + '" >' + district.an + '</option>';
            }
            self.$district.html(temp);
            $("#districtModal").trigger("chosen:updated");
        }
    }
    pictureData.prototype.save=function() {
        var self=this;
        $("#save").click(function(){
              $("#districtForm").ajaxSubmit({
                  success:function(data) {
                      $('#myModal').modal('hide')
                      self.getRangeByCity($("#city").val(),null);

                  }
              })
        });
    }
    module.exports=pictureData;
});
