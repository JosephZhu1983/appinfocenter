<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>应用程序信息中心</title>
    <link rel="shortcut icon" href="${appcfg.websiteStaticFileBaseUrl}favicon.ico">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link href="${appcfg.websiteStaticFileBaseUrl}bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <!-- Font Awesome Icons -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Ionicons -->
    <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <link href="${appcfg.websiteStaticFileBaseUrl}dist/css/AdminLTE.css" rel="stylesheet" type="text/css"/>
    <link href="${appcfg.websiteStaticFileBaseUrl}dist/css/skins/skin-blue.min.css" rel="stylesheet" type="text/css"/>
    <link href="${appcfg.websiteStaticFileBaseUrl}plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet"
          type="text/css"/>
    <link href="${appcfg.websiteStaticFileBaseUrl}plugins/select2/select2.min.css" rel="stylesheet" type="text/css"/>

    <!--[if lt IE 9]>
    <script src="http://cdn.staticfile.org/html5shiv/r29/html5.js"></script>
    <script src="http://cdn.staticfile.org/respond.js/1.4.2/respond.js"></script>
    <![endif]-->

    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/jQuery/jQuery-2.1.4.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}dist/js/app.min.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/daterangepicker/daterangepicker.js"
            type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/select2/select2.full.min.js" type="text/javascript"></script>

</head>

<body class="skin-blue sidebar-mini">
<div class="wrapper">

    <%@include file="../share/header.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <%@include file="../share/side.jsp" %>
        <!-- Main content -->
        <section class="content">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">${appInfo.name} (${appInfo.version}) <button type="button" class="btn-xs bg-yellow deleteApp" data="${appInfo.id}">
                        删除
                    </button></h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <table class="table table-bordered">
                        <tr>
                            <th style="width: 20px"></th>
                            <th>服务器名</th>
                            <th>服务器IP</th>
                            <th>最后活跃时间</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach var="status" items="${appDetail}">
                            <tr>
                                <td>
                                    <c:if test="${status.idleMins <1}">
                                        <i class="fa fa-circle" style="color:green"></i>
                                    </c:if>
                                    <c:if test="${status.idleMins >=1}">
                                        <i class="fa fa-circle" style="color:red"></i>
                                    </c:if>

                                </td>
                                <td>${status.serverName}</td>
                                <td>${status.serverIp}</td>
                                <td><fmt:formatDate value="${status.lastActiveTime}"
                                                    pattern="yyyy/MM/dd HH:mm:ss"/></td>
                                <td>
                                    <button type="button" class="btn-xs bg-red deleteStatus" data="${status.statusId}">
                                        删除
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>

                    </table>

                    <br/>

                    <div id="exceptionchart" style="height:400px"> <div class="overlay" id="loadingexceptionchart">
                        <i class="fa fa-refresh fa-spin"></i>
                    </div></div>

                    <div id="logchart" style="height:400px"> <div class="overlay" id="loadinglogchart">
                        <i class="fa fa-refresh fa-spin"></i>
                    </div></div>


                    <script src="http://echarts.baidu.com/build/dist/echarts.js"></script>

                    <script type="text/javascript">

                        $(document).ready(function () {

                            $(".deleteStatus").click(function () {
                                var id = $(this).attr("data");
                                $.ajax({
                                    type: "delete",
                                    url: "<%=request.getContextPath()%>/ajax/deleteStatus/" + id,
                                    success: function (result) {
                                        if (result) {
                                            location.href = location.href;
                                        }

                                    },
                                    error: function () {
                                        alert("错误！");
                                    }

                                });
                            });
                            $(".deleteApp").click(function () {
                                var id = $(this).attr("data");
                                $.ajax({
                                    type: "delete",
                                    url: "<%=request.getContextPath()%>/ajax/deleteApp/" + id,
                                    success: function (result) {

                                        if (result) {
                                            location.href = '${appcfg.websiteBaseUrl}';
                                        }
                                        else {
                                            alert("删除失败！");
                                        }
                                    },
                                    error: function () {
                                        alert("错误！");
                                    }
                                });
                            });

                            // 路径配置
                            require.config({
                                paths: {
                                    echarts: 'http://echarts.baidu.com/build/dist'
                                }
                            });

                            // 使用
                            require(
                                    [
                                        'echarts',
                                        'echarts/chart/line', 'echarts/chart/bar'
                                    ],
                                    function (ec) {

                                        var logchart = ec.init(document.getElementById('logchart'));

                                        var exceptionchart = ec.init(document.getElementById('exceptionchart'));


                                        var logoption = {
                                            title: {
                                                text: '日志数据量',
                                                subtext: '（跨度：3天；粒度：30分钟）'
                                            },
                                            tooltip: {
                                                trigger: 'axis'
                                            },
                                            legend: {
                                                data: ['Debug', 'Info', 'Warning', 'Error']
                                            },
                                            toolbox: {
                                                show: true,
                                                feature: {
                                                    mark: {show: true},
                                                    dataView: {show: true, readOnly: false},
                                                    magicType: {show: true, type: ['line', 'bar']},
                                                    restore: {show: true},
                                                    saveAsImage: {show: true}
                                                }
                                            },
                                            calculable: true,
                                            xAxis: [
                                                {
                                                    type: 'category',
                                                    data: []
                                                }
                                            ],

                                            yAxis: [
                                                {
                                                    scale: true,
                                                    type: 'value',
                                                    axisLabel: {
                                                        formatter: '{value} 条'
                                                    }
                                                }
                                            ],

                                            series: []
                                        };
                                        var exceptionoption = {
                                            title: {
                                                text: '异常数据量',
                                                subtext: '（跨度：3天；粒度：30分钟）'
                                            },
                                            tooltip: {
                                                trigger: 'axis'
                                            },

                                            toolbox: {
                                                show: true,
                                                feature: {
                                                    mark: {show: true},
                                                    dataView: {show: true, readOnly: false},
                                                    magicType: {show: true, type: ['line', 'bar']},
                                                    restore: {show: true},
                                                    saveAsImage: {show: true}
                                                }
                                            },
                                            calculable: true,
                                            xAxis: [
                                                {
                                                    type: 'category',
                                                    data: []
                                                }
                                            ],

                                            yAxis: [
                                                {
                                                    scale: true,
                                                    type: 'value',
                                                    axisLabel: {
                                                        formatter: '{value} 条'
                                                    }
                                                }
                                            ],

                                            series: []
                                        };


                                        $.ajax({
                                            type: "get",

                                            url: "<%=request.getContextPath()%>/ajax/logcharts/${appInfo.id}",
                                            dataType: "json",
                                            success: function (result) {

                                                if (result) {
                                                    logoption.xAxis[0].data = result.x;
                                                    logoption.series = result.data;
                                                    logchart.setOption(logoption);
                                                    $("#loadinglogchart").hide();
                                                }
                                            },
                                            error: function () {
                                                alert("错误！");
                                            }
                                        });

                                        $.ajax({
                                            type: "get",

                                            url: "<%=request.getContextPath()%>/ajax/exceptioncharts/${appInfo.id}",
                                            dataType: "json",
                                            success: function (result) {

                                                if (result) {
                                                    exceptionoption.xAxis[0].data = result.x;
                                                    exceptionoption.series = result.data;
                                                    exceptionchart.setOption(exceptionoption);
                                                    $("#loadingexceptionchart").hide();
                                                }
                                            },
                                            error: function () {
                                                alert("错误！");
                                            }
                                        });

                                    }
                            );
                        });

                    </script>

                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%@include file="../share/footer.jsp" %>

</body>
</html>
