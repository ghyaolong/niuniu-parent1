/*
 * product list  --douzy
 */
define("banjiajia/web/1.0.0/scripts/members/teacher/edit-debug", [ "$-debug", "ace-elements-debug", "validation-debug", "map-debug" ], function(require, exports, module) {
    var $ = require("$-debug");
    require("ace-elements-debug");
    require("validation-debug")($);
    var Map = require("map-debug");
    var shopDistrictMap = new Map();
    var localMap = new Map();
    var newTeacherForm = $("#newTeacherForm");
    var newTeacherButton = $("#newTeacherButton");
    var phoneInput = $("#phoneInput");
    var queryKey = $("#queryKey");
    var allSelectButton = $("#allSelectButton");
    var nowDistricts = null;
    //	$("#form-field-select-4").val($("#teacherSkills").val());
    var initTempSelect = $("#shoppingDistrictId").val().split(",");
    shoppingDistrictsAdd(initTempSelect);
    $(".chosen-select").chosen();
    $("#form-field-select-4").addClass("tag-input-style");
    $("#shoppingDistrictsSelect").addClass("tag-input-style");
    $("#courseTypeSelectId").addClass("tag-input-style");
    chose_mult_set_ini("#form-field-select-4", $("#teacherSkills").val());
    chose_mult_set_ini("#courseTypeSelectId", $("#courseTypeId").val());
    $(".search-field input").height(25);
    $(".date-picker").datepicker({
        autoclose: true
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });
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
            newTeacherForm.validation(function(obj, params) {
                if (obj.id == "queryKey") {
                    J.post(_contentPath + "/members/teacher/check/phone/" + queryKey.val() + ".json", null, function(data) {
                        params.err = data.result == 1 ? false : true;
                        if (params.err) {
                            if (data.result == 0) params.msg = "老师已经存在";
                            if (data.result == -1) params.msg = "用户不存在请先注册";
                            phoneInput.val("");
                        } else {
                            phoneInput.val(queryKey.val());
                        }
                    }, "json");
                }
            }, {
                reqmark: false
            });
            newTeacherButton.bind("click", function(event) {
                self.teacherButtonClick(event);
            });
        },
        teacherButtonClick: function(event) {
            if (newTeacherForm.valid(this, "error!") == false) {
                //$("#error-text").text("error!"); 1.0.4版本已将提示直接内置掉，简化前端。
                return false;
            }
            J("#teacherSkills").val(J("#form-field-select-4").val());
            J("#courseTypeId").val(J("#courseTypeSelectId").val());
            newTeacherForm.submit();
        }
    };
    var araeself = this;
    var arae = {
        provinces: null,
        nowProvince: null,
        nowCities: null,
        provinceMap: null,
        nowCity: null,
        nowDistricts: null,
        init: function(data) {
            araeself = this;
            provinces = data.locationArae;
            araeself.initMap();
            araeself.bindProvinceDate();
            allSelectButton.bind("click", function() {
                allSelectEvent();
            });
        },
        initMap: function() {
            var map = localMap;
            for (var int = 0; int < provinces.length; int++) {
                var provinceid = provinces[int].np.pn;
                map.put(provinceid, provinces[int]);
                var cities = provinces[int].cs;
                for (var c = 0; c < cities.length; c++) {
                    var cityid = cities[c].nc.cn;
                    map.put(cityid, cities[c]);
                }
            }
            provinceMap = map;
        },
        initOldSelect: function() {},
        bindProvinceDate: function() {
            var _province = $("#province");
            var provinceid = _province.val();
            _province.html("");
            var temp = "";
            for (var int = 0; int < provinces.length; int++) {
                var province = provinces[int];
                temp += ' <option val="' + province.np.p + '" value="' + province.np.pn + '">' + province.np.pn + "</option>";
            }
            _province.html(temp);
            _province.bind("change", function() {
                araeself.provinceOnChange();
            });
            nowProvince = provinceMap.get(provinceid);
            _province.val(provinceid);
            araeself.bindCityData();
        },
        provinceOnChange: function() {
            var _province = $("#province");
            var selectValue = _province.val();
            for (var int = 0; int < provinces.length; int++) {
                var provinceid = provinces[int].np.pn;
                if (provinceid == selectValue) {
                    nowProvince = provinces[int];
                    araeself.bindCityData();
                    break;
                }
            }
        },
        bindCityData: function() {
            //console.log("bindCityDate start");
            var _city = $("#city");
            var cityid = $("#city").val();
            _city.html("");
            nowCities = nowProvince.cs;
            var cities = nowCities;
            var temp = "";
            for (var int = 0; int < cities.length; int++) {
                var city = cities[int];
                temp += ' <option val="' + city.nc.c + '" value="' + city.nc.cn + '">' + city.nc.cn + "</option>";
            }
            _city.html(temp);
            _city.bind("change", function() {
                araeself.cityOnChange();
            });
            //			nowCity=nowCities[0];
            nowCity = provinceMap.get(cityid);
            araeself.bindDistrictData();
        },
        cityOnChange: function() {
            //console.log("bindDistrictDate start");
            var _city = $("#city");
            var selectValue = _city.val();
            for (var int = 0; int < nowCities.length; int++) {
                var cityid = nowCities[int].nc.cn;
                if (cityid == selectValue) {
                    nowCity = nowCities[int];
                    araeself.bindDistrictData();
                    break;
                }
            }
        },
        bindDistrictData: function() {
            //console.log("bindDistrictDate start");
            var _district = $("#district");
            var districtid = $("#district").val();
            _district.html("");
            var districts = nowCity.as;
            var temp = "";
            for (var int = 0; int < districts.length; int++) {
                var district = districts[int];
                temp += ' <option val="' + district.a + '"  value="' + district.an + '">' + district.an + "</option>";
            }
            _district.html(temp);
            _district.val(districtid);
            araeself.districtOnChange();
            _district.unbind();
        },
        districtOnChange: function() {}
    };
    var areaDistrict = {
        provinces: null,
        nowProvince: null,
        nowCities: null,
        nowCity: null,
        nowDistricts: null,
        init: function(data) {
            areaself = this;
            provinces = data.locationArae;
            areaself.bindProvinceDate();
        },
        initOldSelect: function() {},
        bindProvinceDate: function() {
            var _province = $("#sprovince");
            _province.html("");
            var temp = "";
            for (var int = 0; int < provinces.length; int++) {
                var province = provinces[int];
                temp += ' <option value="' + province.np.p + '">' + province.np.pn + "</option>";
            }
            _province.html(temp);
            _province.bind("change", function() {
                areaself.provinceOnChange();
            });
        },
        provinceOnChange: function() {
            var _province = $("#sprovince");
            var selectValue = _province.val();
            for (var int = 0; int < provinces.length; int++) {
                var provinceid = provinces[int].np.p;
                if (provinceid == selectValue) {
                    nowProvince = provinces[int];
                    areaself.bindCityData();
                    break;
                }
            }
        },
        bindCityData: function() {
            //console.log("bindCityDate start");
            var _city = $("#scity");
            _city.html("");
            nowCities = nowProvince.cs;
            var cities = nowCities;
            var temp = "";
            for (var int = 0; int < cities.length; int++) {
                var city = cities[int];
                temp += ' <option value="' + city.nc.c + '">' + city.nc.cn + "</option>";
            }
            _city.html(temp);
            _city.bind("change", function() {
                areaself.cityOnChange();
            });
            nowCity = nowCities[0];
            areaself.bindDistrictData();
        },
        cityOnChange: function() {
            //console.log("bindDistrictDate start");
            var _city = $("#scity");
            var selectValue = _city.val();
            for (var int = 0; int < nowCities.length; int++) {
                var cityid = nowCities[int].nc.c;
                if (cityid == selectValue) {
                    nowCity = nowCities[int];
                    areaself.bindDistrictData();
                    break;
                }
            }
        },
        bindDistrictData: function() {
            //console.log("bindDistrictDate start");
            var _district = $("#sdistrict");
            _district.html("");
            var districts = nowCity.as;
            var temp = "";
            for (var int = 0; int < districts.length; int++) {
                var district = districts[int];
                temp += ' <option value="' + district.an + '">' + district.an + "</option>";
            }
            _district.html(temp);
            _district.unbind();
            areaself.districtOnChange();
            _district.bind("change", function() {
                areaself.districtOnChange();
            });
        },
        districtOnChange: function() {
            $.ajax({
                url: _contentPath + "/common/resource/district/list.json?&area=" + $("#sdistrict").val() + "&" + new Date().getTime(),
                async: true,
                dataType: "json",
                success: function(data, textStatus) {
                    _marketId = $("#shoppingDistrictsSelect");
                    var temp = "";
                    nowDistricts = data.districts;
                    for (var int = 0; int < data.districts.length; int++) {
                        var market = data.districts[int];
                        temp += ' <option value="' + market.name + '">' + market.name + "</option>";
                    }
                    _marketId.html(temp);
                    $("#shoppingDistrictsSelect").trigger("chosen:updated");
                    $("#shoppingDistrictsSelect").change(function() {
                        var tempSelect = $("#shoppingDistrictsSelect").val();
                        shoppingDistrictsAdd(tempSelect);
                    });
                },
                error: function(jqXHR, textStatus, errorThrown) {}
            });
        }
    };
    $.ajax({
        url: _contentPath + "/resources/assets/sea-modules/gallery/common/locationarea.js",
        async: true,
        dataType: "json",
        success: function(data, textStatus) {
            //console.log("success");
            arae.init(data);
            areaDistrict.init(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {}
    });
    function shoppingDistrictsAdd(districts) {
        var districtsArray = districts;
        for (var i = 0; i < districtsArray.length; i++) {
            var district = districtsArray[i];
            shopDistrictMap.put(district, "");
        }
        if (districtsArray.length > 0) {
            setshoppingDistrictsSelect();
        }
    }
    function removeDistrictsSelect(obj) {
        var span = $(obj).parents("span");
        span.remove();
        shopDistrictMap.remove(span.attr("val"));
        setshoppingDistrictsSelect();
    }
    function setshoppingDistrictsSelect() {
        var keys = "";
        var html = "";
        shopDistrictMap.each(function(key) {
            keys += key + ",";
            html += '<span val="' + key + '" class="tag">' + key + '<button type="button" class="close">×</button></span>';
        });
        if (keys.length > 0) {
            keys = keys.substring(0, keys.length - 1);
        }
        $("#shoppingDistrictId").val(keys);
        $("#shoppingDistrictsSelectTags").html(html);
        $("#shoppingDistrictsSelectTags button").bind("click", function(event) {
            removeDistrictsSelect(this);
        });
    }
    function shoppingDistrictsRemove(district) {
        shopDistrictMap.remove(district);
    }
    function chose_mult_set_ini(select, values) {
        var arr = values.split(",");
        var length = arr.length;
        var value = "";
        for (var i = 0; i < length; i++) {
            value = arr[i];
            $(select + " option[value='" + value + "']").attr("selected", "selected");
        }
        $(select).trigger("chosen:updated");
    }
    function allSelectEvent() {
        for (var int = 0; int < nowDistricts.length; int++) {
            var district = nowDistricts[int].name;
            shopDistrictMap.put(district, "");
        }
        setshoppingDistrictsSelect();
    }
    func.init();
});
