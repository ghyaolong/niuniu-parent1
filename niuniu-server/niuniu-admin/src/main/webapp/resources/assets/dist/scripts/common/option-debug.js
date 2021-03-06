define("banjiajia/web/1.0.0/scripts/common/option-debug", [ "$-debug" ], function(require, exports, module) {
    var $ = require("$-debug");
    function SkuOption(keys, data) {
        this.keys = keys;
        //属性集
        this.data = data;
        //属性结果集
        this.splitStr = ";";
        //属性结果集 分割符
        this.SKUResult = {};
    }
    SkuOption.prototype = {
        init: function() {
            var self = this;
            var i, j, skuKeys = self.getObjKeys(self.data);
            for (i = 0; i < skuKeys.length; i++) {
                var skuKey = skuKeys[i];
                //一条SKU信息key
                var sku = self.data[skuKey];
                //一条SKU信息value
                var skuKeyAttrs = skuKey.split(self.splitStr);
                //SKU信息key属性值数组
                var len = skuKeyAttrs.length;
                //对每个SKU信息key属性值进行拆分组合
                var combArr = self.arrayCombine(skuKeyAttrs);
                for (j = 0; j < combArr.length; j++) {
                    ////console.log("dddddd:"+combArr[j]);
                    self.add2SKUResult(combArr[j], sku);
                }
                ////console.log(sku.imgs);
                //结果集接放入SKUResult
                self.SKUResult[skuKey] = {
                    count: sku.count,
                    code: [ sku.skuCode ],
                    stock: [ sku.stock ],
                    prices: [ sku.price ],
                    images: [ sku.imgs ]
                };
            }
            return self.SKUResult;
        },
        //获取属性集对象
        getObjKeys: function(obj) {
            if (obj !== Object(obj)) throw new TypeError("Invalid object");
            var keys = [];
            for (var key in obj) if (Object.prototype.hasOwnProperty.call(obj, key)) keys[keys.length] = key;
            return keys;
        },
        /**
		 * 从数组中生成指定长度的组合
		 */
        arrayCombine: function(targetArr) {
            if (!targetArr || !targetArr.length) {
                return [];
            }
            var self = this;
            var len = targetArr.length;
            var resultArrs = [];
            // 所有组合
            for (var n = 1; n < len; n++) {
                var flagArrs = self.getFlagArrs(len, n);
                while (flagArrs.length) {
                    var flagArr = flagArrs.shift();
                    var combArr = [];
                    for (var i = 0; i < len; i++) {
                        flagArr[i] && combArr.push(targetArr[i]);
                    }
                    resultArrs.push(combArr);
                }
            }
            return resultArrs;
        },
        getFlagArrs: function(m, n) {
            if (!n || n < 1) {
                return [];
            }
            var resultArrs = [], flagArr = [], isEnd = false, i, j, leftCnt;
            for (i = 0; i < m; i++) {
                flagArr[i] = i < n ? 1 : 0;
            }
            resultArrs.push(flagArr.concat());
            while (!isEnd) {
                leftCnt = 0;
                for (i = 0; i < m - 1; i++) {
                    if (flagArr[i] == 1 && flagArr[i + 1] == 0) {
                        for (j = 0; j < i; j++) {
                            flagArr[j] = j < leftCnt ? 1 : 0;
                        }
                        flagArr[i] = 0;
                        flagArr[i + 1] = 1;
                        var aTmp = flagArr.concat();
                        resultArrs.push(aTmp);
                        if (aTmp.slice(-n).join("").indexOf("0") == -1) {
                            isEnd = true;
                        }
                        break;
                    }
                    flagArr[i] == 1 && leftCnt++;
                }
            }
            return resultArrs;
        },
        //把组合的key放入结果集SKUResult
        add2SKUResult: function(combArrItem, sku) {
            var self = this;
            var key = combArrItem.join(self.splitStr);
            if (self.SKUResult[key]) {
                //SKU信息key属性·
                self.SKUResult[key].count += sku.count;
                self.SKUResult[key].prices.push(sku.price);
                self.SKUResult[key].images.push(sku.imgs);
                self.SKUResult[key].code.push(sku.skuCode);
                self.SKUResult[key].stock.push(sku.stock);
            } else {
                self.SKUResult[key] = {
                    count: sku.count,
                    code: [ sku.skuCode ],
                    stock: [ sku.stock ],
                    prices: [ sku.price ],
                    images: [ sku.imgs ]
                };
            }
        }
    };
    module.exports = SkuOption;
});
