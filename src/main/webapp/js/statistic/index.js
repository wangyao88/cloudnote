var count = 0;
$(document).ready(function () {
    char1();
    char1Interval();

    char2();
    char2Interval();

    char3();
    char3Interval();

    char4();
    char4Interval();

    getTopAndTableData();
    getTopAndTableDataInterval();

    getLogTableData();
    getLogTableDataInterval()
});

function getTopAndTableData() {
    $.ajax({
        url : basePath+"statistic/getTopAndTableData",
        type : "get",
        dataType: "json",
        success : function(result){
            //topData
            var topData = result.topData;
            $("#noteNum").html(topData.noteNum);
            $("#flagNum").html(topData.flagNum);
            $("#articleNum").html(topData.articleNum);
            $("#blogNum").html(topData.blogNum);
            $("#todayArticleNum").html(topData.todayArticleNum);
            $("#logNum").html(topData.logNum);
            //tableData
            var hitDatas = result.hitDatas;
            var hitDataHtml = "";
            $("#hitDataTable").html(hitDataHtml);
            $(hitDatas).each(function(index, hitData) {
                hitDataHtml += "<tr><td>"+hitData.index+"</td><td>"+hitData.title+"</td><td>"+hitData.hitNum+"</td></tr>";
            });
            $("#hitDataTable").html(hitDataHtml);


            var recentDatas = result.recentDatas;
            var recentDataHtml = "";
            $("#recentDataTable").html(recentDataHtml);
            $(recentDatas).each(function(index, recentData) {
                recentDataHtml += "<tr><td>"+recentData.index+"</td><td>"+recentData.title+"</td><td>"+recentData.createDate+"</td></tr>";
            });
            $("#recentDataTable").html(recentDataHtml);

            var flagDatas = result.flagDatas;
            var flagDataHtml = "";
            $("#flagDataTable").html(flagDataHtml);
            $(flagDatas).each(function(index, flagData) {
                flagDataHtml += "<tr><td>"+flagData.index+"</td><td>"+flagData.name+"</td><td>"+flagData.num+"</td></tr>";
            });
            $("#flagDataTable").html(flagDataHtml);


            var searchDatas = result.searchDatas;
            var searchDataHtml = "";
            $("#searchDataTable").html(searchDataHtml);
            $(searchDatas).each(function(index, searchData) {
                searchDataHtml += "<tr><td>"+searchData.index+"</td><td>"+searchData.key+"</td><td>"+searchData.num+"</td></tr>";
            });
            $("#searchDataTable").html(searchDataHtml);
        },
        error : function(){
            alert("getTopAndTableData执行失败，请稍候重试！");
        }
    });
}

//笔记本关联笔记统计
function char1() {
    $.ajax({
        url : basePath+"statistic/getPieData",
        type : "get",
        dataType: "json",
        success : function(result){
            var myChart = echarts.init($("#char1")[0]);

            option = {
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 'right',
                    textStyle : {
                        color : '#ffffff',

                    },
                    data: result.optionData
                },

                calculable : false,
                series : [
                    {
                        name:'笔记本',
                        type:'pie',
                        radius : ['40%', '70%'],
                        itemStyle : {
                            normal : {
                                label : {
                                    show : false
                                },
                                labelLine : {
                                    show : false
                                }
                            },
                            emphasis : {
                                label : {
                                    show : true,
                                    position : 'center',
                                    textStyle : {
                                        fontSize : '20',
                                        fontWeight : 'bold'
                                    }
                                }
                            }
                        },
                        data: result.seriesData
                    }
                ]
            };

            myChart.setOption(option);
            window.addEventListener('resize', function () {myChart.resize();})
        },
        error : function(){
            alert("getPieData执行失败，请稍候重试！");
        }
    });
}

function char2() {
    $.ajax({
        url : basePath+"statistic/getBarPercentData",
        type : "get",
        dataType: "json",
        success : function(result){
            var myChart = echarts.init($("#char2")[0]);

            option = {
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {show:'true',borderWidth:'0'},
                legend: {
                    data:['INFO', 'WARN','ERROR','DEBUG'],
                    textStyle : {
                        color : '#ffffff',

                    }
                },

                calculable :false,
                xAxis : [
                    {
                        type : 'value',
                        axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            lineStyle:{
                                color:['#f2f2f2'],
                                width:0,
                                type:'solid'
                            }
                        }

                    }
                ],
                yAxis : [
                    {
                        type : 'category',
                        data : ['第一季度','第二季度','第三季度','第四季度'],
                        axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            lineStyle:{
                                width:0,
                                type:'solid'
                            }
                        }
                    }
                ],
                series : [
                    {
                        name:'INFO',
                        type:'bar',
                        stack: '总量',
                        itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                        data: result.infoDatas
                    },
                    {
                        name:'WARN',
                        type:'bar',
                        stack: '总量',
                        itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                        data: result.warnDatas
                    },
                    {
                        name:'ERROR',
                        type:'bar',
                        stack: '总量',
                        itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                        data: result.errorDatas
                    },
                    {
                        name:'DEBUG',
                        type:'bar',
                        stack: '总量',
                        itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                        data: result.debugDatas
                    }

                ]
            };

            myChart.setOption(option);
            window.addEventListener('resize', function () {myChart.resize();})
        },
        error : function(){
            alert("getBarPercentData执行失败，请稍候重试！");
        }
    });
}
function char3() {
    $.ajax({
        url : basePath+"statistic/getLineData",
        type : "get",
        dataType: "json",
        success : function(result){
            var myChart = echarts.init($("#char3")[0]);

            option = {
                legend: {
                    data:['todo完成数量'],
                    textStyle : {
                        color : '#ffffff',

                    }
                },
                grid: {show:'true',borderWidth:'0'},

                calculable : false,
                tooltip : {
                    trigger: 'axis',
                    formatter: "todo完成数量: <br/>{b}月 : {c}条"
                },
                xAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}',
                            textStyle: {
                                color: '#fff'
                            }
                        },

                        splitLine:{
                            lineStyle:{
                                width:0,
                                type:'solid'
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        type : 'category',
                        axisLine : {onZero: false},
                        axisLabel : {
                            formatter: '{value}月',
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            lineStyle:{
                                width:0,
                                type:'solid'
                            }
                        },
                        boundaryGap : false,
                        data : result.months
                    }
                ],
                series : [
                    {
                        name:'todo完成数量',
                        type:'line',
                        smooth:true,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    shadowColor : 'rgba(0,0,0,0.4)'
                                }
                            }
                        },
                        data: result.datas
                    }
                ]
            };

            myChart.setOption(option);
            window.addEventListener('resize', function () {myChart.resize();})
        },
        error : function(){
            alert("getLineData执行失败，请稍候重试！");
        }
    });
}

function char4() {
    $.ajax({
        url : basePath+"statistic/getBarData",
        type : "get",
        dataType: "json",
        success : function(result){
            var myChart = echarts.init($("#char4")[0]);

            option = {
                grid: {show:'true',borderWidth:'0'},
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },

                    formatter: function (params) {
                        var tar = params[0];
                        return tar.name + '月<br/>' + tar.seriesName + ' : ' + tar.value;
                    }
                },

                xAxis : [
                    {
                        type : 'category',
                        splitLine: {show:false},
                        data : result.months,
                        axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#fff'
                            }
                        }

                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        splitLine: {show:false},
                        axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#fff'
                            }
                        }
                    }
                ],
                series : [

                    {
                        name:'笔记数量',
                        type:'bar',
                        stack: '总量',
                        itemStyle : { normal: {label : {show: true, position: 'inside'}}},
                        data: result.datas
                    }
                ]
            };

            myChart.setOption(option);
            window.addEventListener('resize', function () {myChart.resize();})
        },
        error : function(){
            alert("getBarData执行失败，请稍候重试！");
        }
    });
}

function getLogTableData() {
    if(count >= 100) {
        count = 0;
    }
    count++;
    $.ajax({
        url : basePath+"statistic/getLogTableData",
        type : "get",
        data: {
            pageIndex: count
        },
        dataType: "json",
        success : function(result){
            //tableData
            var logDatas = result;
            var logDataHtml = "";
            $("#logDataTable").html(logDataHtml);
            $(logDatas).each(function(index, logData) {
                logDataHtml += "<tr><td>"+logData.index+"</td><td>"+logData.level+"</td><td>"+logData.message+"</td><td>"+logData.costTime+"</td><td>"+logData.ip+"</td><td>"+logData.date+"</td></tr>";
            });
            $("#logDataTable").html(logDataHtml);
        },
        error : function(){
            alert("getLogTableData执行失败，请稍候重试！");
        }
    });
}

function getTopAndTableDataInterval() {
    window.setInterval(getTopAndTableData, 1000*10);
}

function char1Interval() {
    window.setInterval(char1, 4000);
}

function char2Interval() {
    window.setInterval(char2, 4500);
}

function char3Interval() {
    window.setInterval(char3, 3500);
}

function char4Interval() {
    window.setInterval(char4, 5000);
}

function getLogTableDataInterval() {
    window.setInterval(getLogTableData, 1000*2);
}
