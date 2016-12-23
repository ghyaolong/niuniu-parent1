/**
 * 广告图表
 * douzy    2015-07-20
 * @returns {bjjChart}
 */
define(function(require,exports,module) {
    var $=require("$");
    function bjjChart(charUrl) {
        this.url = charUrl
        this.param = null
        this.chartOptionApi = null
    }
    /**
     * 请求参数
     */
    bjjChart.prototype.setParam = function (option, chartOptionApi) {
        this.param = option;
        this.chartOptionApi = chartOptionApi;
    }
    /**
     * 初始配置
     */
    bjjChart.prototype.init = function () {
        var self = this;

        Highcharts.setOptions({
            global: {
                useUTC: false
            },
            lang: {
                numericSymbols: null
            },
            legend: {
                enabled: false
            },
            credits: {
                enabled: false
            },
            series: {
                showInLegend: false
            },
            title: {
                text: null
            },
            xAxis: {
                dateTimeLabelFormats: {
                    millisecond: '%H:%M:%S.%L',
                    second: '%H点%M分%S秒',
                    minute: '%H点%M分',
                    hour: '%H点',
                    day: '%b%e日',
                    week: '%e. %b',
                    month: '%b \'%y',
                    year: '%Y'
                }
            },
            yAxis: {
                min: 0
            },
            subtitle: {
                text: ''
            },
            lang: {
                months: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                weekdays: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
                shortMonths: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            }
        });

//    self.render();

    }
    /**
     * 图表基础设置 area
     */
    bjjChart.prototype.chartOption = function () {
        var self = this;
        var range = self.param.dim == "day" ? 86400000 : 3600000;
        var chartOptionApi = self.chartOptionApi(null);
        return {
            chart: {
                renderTo: chartOptionApi.render
            },
            title: {
                text: chartOptionApi.title
            },
            xAxis: {
                type: 'datetime',
                minRange: range
            },
            yAxis: {
                title: {
                    text: self.param.ytxt
                }
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },
            tooltip: {
                formatter: function () {
                    var dtFormat = self.param.dim == "day" ? "%Y年%m月%e日" : "%Y年%m月%e日 %H:00";

                    return Highcharts.dateFormat(dtFormat, this.x) +
                        "<br/>" + "<span style=\"fill:" + this.series.color + "\" x=\"8\" dy=\"16\">●</span> " +
                        "" + chartOptionApi.tooltip.name + ": <b>" + Highcharts.numberFormat(this.y, 0, ".", ",") + "<\/b> ";
                }
            }
        };
    }
    /**
     * 柱状图  配置
     */
    bjjChart.prototype.chartColumnOption = function () {
        var self = this;
        var chartOptionApi = self.chartOptionApi(null);
        return {
            chart: {
                renderTo: chartOptionApi.render,
                type: 'column'
            },
            title: {
                text: chartOptionApi.title
            },
            xAxis: {
                categories: [],
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: ''
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: []
        }
    }
    /**
     * 饼图  配置
     */
    bjjChart.prototype.chartPieOption = function () {
        var self = this;
        var chartOptionApi = self.chartOptionApi(null);
        return {
            chart: {
                renderTo: chartOptionApi.render,
                height: chartOptionApi.height,
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: chartOptionApi.title
            },
            tooltip: {
                pointFormat: '{series.name}: <b>≈{point.percentage:.2f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.2f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: []
        }
    }
    /**
     * 折线图
     */
    bjjChart.prototype.chartZheOption = function () {
        var self = this;
        var chartOptionApi = self.chartOptionApi(null);
        return {
            chart: {
                renderTo: chartOptionApi.render
            },
            title: {
                text: chartOptionApi.title,
                x: 0 //center
            },
            xAxis: {
                categories: []
            },
            yAxis: {
                title: {
                    text: ''
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: ''
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: []
        }
    }
    /**
     * chart option设置
     */
    bjjChart.prototype.getChartOption = function (data) {
        var self = this, option = null;

        var chartOptionApi = self.chartOptionApi(data);

        var seriesFull = chartOptionApi.seriesFull()
        switch (chartOptionApi.type) {
            case "area": //线性图
                option = self.chartOption();
                var range = self.param.dim == "day" ? 86400000 : 3600000;
                option.series = seriesFull.series;
                option.xAxis.minRange = range;
                option.yAxis.title.text = self.param.ytxt;
                break;
            case "column"://柱状图
                option = self.chartColumnOption();
                option.series = seriesFull.series;
                option.xAxis.categories = seriesFull.categories;
                option.yAxis.title.text = self.param.ytxt;
                break;
            case "pie": //饼图
                option = self.chartPieOption();
                option.series = seriesFull.series;
                break;
            case "zhe": //折线图
                option = self.chartZheOption();
                option.series = seriesFull.series;
                option.xAxis.categories = seriesFull.categories;
                option.yAxis.title.text = self.param.ytxt;
                break;
        }
        return option;
    }
    /**
     * 数据填充
     * @param url
     */
    bjjChart.prototype.render = function () {
        var self = this;
        $.ajax({
            url: self.url,
            method: 'GET',
            dataType: 'JSON',
            data: self.param,
            success: function (result) {
                var option = self.getChartOption(result);
                ecOverviewChart = new Highcharts.Chart(option);
            }
        });
    }

    $(function () {
        new bjjChart(null).init();
    });
    module.exports=bjjChart;
});