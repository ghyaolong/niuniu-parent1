/**
 * 注册用户统计
 * douzy 2015-08-29
 */
define(function(require,exports,module){
       var $=require("$");
    var Util=require("util");
    var util=new  Util();

    require("bootstrap");
    util.include(_contentPath+"/resources/assets/sea-modules/gallery/hightChart/4.1.5/highcharts.js")
    util.include(_contentPath+"/resources/assets/sea-modules/gallery/dateRange/moment.js")
    util.include(_contentPath+"/resources/assets/sea-modules/gallery/dateRange/daterange.js")
    util.include(_contentPath+"/resources/assets/src/style/common/dateRange/daterange.css")


       var BJJChart= require("bjjChart");
       var bjjChart=new BJJChart();

    function newUser(){
        this.dimension="day" //默认以"日"为搜索维度
        this.startTime=sTime
        this.endTime=eTime
        this.charUrl=_contentPath+"/statistics/user/new/getData.json";
        this.ytxt="注册量"
    }
    newUser.prototype.init=function(){
        var self=this;

        self.config();
        //self.timeDimensions();
        util.ajaxGlobalSetting();
        self.chartRender();
        self.chartDayBtn();
    }
    newUser.prototype.config=function() {
        var self=this;
        var $reportrange = $("#reportrange2");
        //日期控件 初始
        $reportrange.daterangepicker({
            format: 'YYYY-MM-DD',
            opens: 'right',
            startDate: self.startDate,
            endDate: self.endDate,
            ranges: {
                '今天': [moment(), moment()],
                '昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '过去一周': [moment().subtract(6, 'days'), moment()],
                '过去30天': [moment().subtract(29, 'days'), moment()],
                '这个月': [moment().startOf('month'), moment().endOf('month')],
                '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            drops: 'down',
            locale: {
                applyLabel: '确认',
                cancelLabel: '取消',
                fromLabel: '从',
                toLabel: '到',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        },function(s,e,l){
            var starT=new Date(s._d);
            self.startTime=starT.toLocaleDateString().replace(/\//ig,"-");
            var endT=new Date(e._d);
            self.endTime=endT.toLocaleDateString().replace(/\//ig,"-");
            $('#reportrange2').html(self.startTime + ' - ' + self.endTime);
            self.chartRender();
        });
    }
    /**
     * 默认 x轴为时间 y为 会话总数
     * @param v
     * @returns {{x: Number, y: Number}}
     */
    newUser.prototype.chartOptionCallBack=function(data) {
        return {
            type: "zhe",
            render:"container",
            title:"",
            height:450,
            seriesFull:function(){
                var dt=[];
                if(!!data) {
                    var obj=data.newUserStatistics;
                    var viewArr=[]
                        ,dateTArr=[];
                    for(var key in obj)
                    {
                        viewArr.push(!!obj[key]?parseInt(obj[key]) : 0);
                        dateTArr.push( !!key ? key : 0);
                    }
                }
                return {series:[{name:"总量",data:viewArr}],categories:dateTArr};
            },
            tooltip: {
                name: "用户数"
            }, chart: {}
        }
    }
    /**
     * chart
     */
    newUser.prototype.chartRender=function(){
        var self=this;
        var option={"sTime":self.startTime,"eTime":self.endTime,"ytxt":self.ytxt};
        //chart
        var $bjjChart=new BJJChart(self.charUrl);
        $bjjChart.setParam(option,self.chartOptionCallBack);
        $bjjChart.render();
    };
    /**
     * 维度事件绑定`
     */
    newUser.prototype.timeDimensions=function(){
        var self=this;
        //$(".btn-sm").click(function(){
        //    $(this).addClass("active");
        //    $(this).siblings().removeClass("active");
        //    self.dimension=$(this).attr("data-stats");
        //
        //});
    }
    newUser.prototype.chartDayBtn=function(){
        var self=this;
        //$(".day-btn").click(function(){
        //    var stats=$(this).attr("data-stats");
        //    self.dimension=stats;
        //    self.chartRender();
        //});
    }
    /**
     * 维度切换  日  、 小时
     * 事件绑定
     */
    newUser.prototype.getTimeDimensions=function(){
        return this.dimension;
    }
    $(function(){
        new newUser().init();
    });
        module.exports=newUser;
});