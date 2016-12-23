/**
 * 注册用户统计
 * douzy 2015-08-29
 */
define(function(require,exports,module){
    var $=require("$"),State=require("state");
    var Util=require("util");
    var util=new  Util();
    var state=new State();
    state.init(_contentPath + '/system/config/get/order_detail_state.json');

    require("bootstrap");
    util.include(_contentPath+"/resources/assets/sea-modules/gallery/hightChart/4.1.5/highcharts.js")
    util.include(_contentPath+"/resources/assets/sea-modules/gallery/dateRange/moment.js")
    util.include(_contentPath+"/resources/assets/sea-modules/gallery/dateRange/daterange.js")
    util.include(_contentPath+"/resources/assets/src/style/common/dateRange/daterange.css")


    var BJJChart= require("bjjChart");
    var bjjChart=new BJJChart();

    function ScheduleChart() {
        this.dimension = "day" //默认以"日"为搜索维度
        this.startTime = sTime
        this.endTime = eTime
        this.pieUrl = _contentPath + "/statistics/order_schedule/top/teacher/getData.json";
        this.stateUrl = _contentPath + "/statistics/order_schedule/state/getData.json";
        this.timeUrl = _contentPath + "/statistics/order_schedule/time/getData.json";
        this.cloumnUrl=_contentPath + "/statistics/order_schedule/cloumn/getData.json";
        this.ytxt = ""
    }
    ScheduleChart.prototype.init=function(){
        var self=this;

        self.config();
        //self.timeDimensions();
        util.ajaxGlobalSetting();
        self.chartRender();
        self.chartDayBtn();
    }
    ScheduleChart.prototype.config=function() {
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
    ScheduleChart.prototype.chartOptionCallBack=function(data) {
        return {
            type: "pie",
            render:"schduleContainer",
            title:"老师日程TOP10",
            height:450,
            seriesFull:function(){
                var dt=[];
                $.each(data.OrderScheduleStatisticss, function (n, v) {
                    var mediaDate=[];
                    mediaDate.push(v.teacherName);
                    mediaDate.push(v.count);
                    if( parseInt(v.count)>0)
                        dt.push(mediaDate);
                });
                return {series: [{type:'pie',name:'占比',data:dt}],categories:null};
            },
            tooltip: {
                name: "日程数"
            }, chart: {}
        }
    }
    ScheduleChart.prototype.chartStateOptionCallBack=function(data) {
        return {
            type: "pie",
            render:"schduleStateContainer",
            title:"各状态统计",
            height:450,
            seriesFull:function(){
                var dt=[];
                $.each(data.OrderScheduleStates, function (n, v) {
                    var mediaDate=[];
                    mediaDate.push(state.get(state.OrderProcessState(),v.state));
                    mediaDate.push(v.count);
                    if( parseInt(v.count)>0)
                        dt.push(mediaDate);
                });
                return {series: [{type:'pie',name:'占比',data:dt}],categories:null};
            },
            tooltip: {
                name: "日程数"
            }, chart: {}
        }
    }
    ScheduleChart.prototype.timeLineChartOptionCallBack=function(data){
        return {
            type: "zhe",
            render:"timeContainer",
            title:"每日日程走势",
            //height:450,
            seriesFull:function(){
                var dt=[];
                if(!!data) {
                    var obj=data.OrderScheduletimes;
                    var viewArr=[]
                        ,dateTArr=[];
                    for(var key in obj) {
                        viewArr.push(!!obj[key] ? parseInt(obj[key]) : 0);
                        dateTArr.push(!!key ? key : 0);
                    }
                }
                return {series:[{name:"日程数",data:viewArr}],categories:dateTArr};
            },
            tooltip: {
                name: "日程数"
            }, chart: {}
        }
    }
    ScheduleChart.prototype.columnChartOptionCallBack=function(data){
        return {
            type:'column',
            title:"每日日程状态统计",
            render:"cloumnContainer",
            seriesFull:function(){
                var dt=[],dateTArr=[];
                var stateObj=state.OrderProcessState();
                console.log(stateObj);
                for(var key in stateObj)
                {
                    var dataArr=[];
                    $.each(data.OrderSchedules, function (n, v) {
                        if(v.state==key)
                            dataArr.push(v.count);
                        dateTArr.push(v.timeLine);
                    });
                    dt.push({name:stateObj[key],data:dataArr});
                }
                return {series:dt,categories:dateTArr};
            },
            tooltip:{
                name:""
            },chart: {}
        }
    }
    /**
     * chart
     */
    ScheduleChart.prototype.chartRender=function(){
        var self=this;
        var year=new Date(self.startTime).getUTCFullYear();
        var option={"year":year};
        //chart
        var $bjjChart=new BJJChart(self.pieUrl);
        $bjjChart.setParam(option,self.chartOptionCallBack);
        $bjjChart.render();
        var stateOption={"year":year};
        //chart
        var $bjjChartStateLine=new BJJChart(self.stateUrl);
        $bjjChartStateLine.setParam(stateOption,self.chartStateOptionCallBack);
        $bjjChartStateLine.render();

        var timeOption={"sTime":self.startTime,"eTime":self.endTime};
        var $bjjChartTimeLine=new BJJChart(self.timeUrl);
        $bjjChartTimeLine.setParam(timeOption,self.timeLineChartOptionCallBack);
        $bjjChartTimeLine.render();
        //
        var columnOption={"sTime":self.startTime,"eTime":self.endTime};
        //chart
        var $bjjChartColumn=new BJJChart(self.cloumnUrl);
        $bjjChartColumn.setParam(columnOption,self.columnChartOptionCallBack);
        $bjjChartColumn.render();
    };
    /**
     * 维度事件绑定`
     */
    ScheduleChart.prototype.timeDimensions=function(){
        var self=this;
    }
    ScheduleChart.prototype.chartDayBtn=function(){
        var self=this;
    }
    /**
     * 维度切换  日  、 小时
     * 事件绑定
     */
    ScheduleChart.prototype.getTimeDimensions=function(){
        return this.dimension;
    }
    $(function(){
        new ScheduleChart().init();
    });
    module.exports=ScheduleChart;
});