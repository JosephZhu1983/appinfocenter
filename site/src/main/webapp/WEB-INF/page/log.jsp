<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="fn" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>应用程序信息中心</title>
    <link rel="shortcut icon" href="${appcfg.websiteStaticFileBaseUrl}favicon.ico">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link href="${appcfg.websiteStaticFileBaseUrl}bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="http://cdn.staticfile.org/font-awesome/4.2.0/css/font-awesome.css" rel="stylesheet" type="text/css"/>
    <link href="http://cdn.staticfile.org/ionicons/1.5.2/css/ionicons.css" rel="stylesheet" type="text/css"/>
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
    <script src="http://cdn.bootcss.com/URI.js/1.11.2/URI.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/daterangepicker/daterangepicker.js"
            type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/select2/select2.full.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}ZeroClipboard.min.js" type="text/javascript"></script>


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
                    <h3 class="box-title">搜索</h3>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>按级别：</label>
                                <select id="levels" class="form-control select2" multiple="multiple" data-placeholder="选择一个或多个级别">

                                    <option value="1" <c:if test="${levels == null || fn:contains(levels,1) }">selected</c:if>>DEBUG</option>
                                    <option value="2" <c:if test="${levels == null || fn:contains(levels,2) }">selected</c:if>>INFO</option>
                                    <option value="3" <c:if test="${levels == null || fn:contains(levels,3) }">selected</c:if>>WARNING</option>
                                    <option value="4" <c:if test="${levels == null || fn:contains(levels,4) }">selected</c:if>>ERROR</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>按时间段：</label>

                                <div class="input-group">
                                    <div class="input-group-addon">
                                        <i class="fa fa-clock-o"></i>
                                    </div>
                                    <input type="text" class="form-control pull-right " id="daterange"/>
                                </div>
                                <!-- /.input group -->
                            </div>

                        </div><!-- /.col -->
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>按服务器：</label>
                                <select id="serverIds" class="form-control select2" multiple="multiple" data-placeholder="选择一个或多个服务器">
                                    <c:forEach var="server" items="${servers}">
                                        <option value="${server.id}" <c:if test="${serverIds == null || fn:contains(serverIds,server.id) }">selected</c:if>>${server.ip}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>按上下文标示：</label>
                                <input id="contextId" type="text" class="form-control" placeholder="同一线程产生的数据拥有相同的上下文标示"/>

                            </div><!-- /.form-group -->

                        </div><!-- /.col -->
                    </div><!-- /.row -->

                    <button id="search" class="btn bg-blue btn-flat margin">搜索</button>
                    <button id="reset" class="btn bg-light-blue btn-flat margin">重置</button>
                </div><!-- /.box-body -->
                <div class="box-body">
                    <table class="table table-bordered" id="logs">
                        <tr>
                            <th>发生时间</th>
                            <th>服务器名</th>
                            <th>服务器IP</th>
                            <th>级别</th>
                            <th style="width: 50%">消息</th>
                            <th>上下文标识</th>
                        </tr>
                        <c:forEach var="log" items="${logs}">
                            <tr>
                                <td><fmt:formatDate value="${log.time}"
                                                    pattern="yyyy/MM/dd HH:mm:ss"/></td>
                                <td>${log.serverName}</td>
                                <td>${log.serverIp}</td>
                                <td>
                                    <c:if test="${log.level ==1}">
                                        <small class="label bg-green">DEBUG</small>
                                    </c:if>
                                    <c:if test="${log.level ==2}">
                                        <small class="label bg-blue">INFO</small>
                                    </c:if>
                                    <c:if test="${log.level ==3}">
                                        <small class="label bg-orange">WARN</small>
                                    </c:if>
                                    <c:if test="${log.level ==4}">
                                        <small class="label bg-red">ERROR</small>
                                    </c:if>
                                </td>
                                <td>${log.message}</td>
                                <td><button id="${log.id}" class="btn-xs bg-blue copyContextId" data-clipboard-text="${log.contextId}">复制</button></td>

                            </tr>
                        </c:forEach>

                    </table>

                    <div class="box-footer clearfix">
                        <span class="pull-left">第 ${p}/${pageCount} 页</span>
                        <ul class="pagination pagination-sm no-margin pull-right">

                            <c:if test="${p != 1}">
                                <li><span><a href="javascript:void(0);" data-p="${1}">第一页</a></span></li>
                                <li><span><a href="javascript:void(0);" data-p="${p - 1}">前一页</a></span></li>
                            </c:if>
                            <c:forEach var="pageItem" items="${pageList}">
                                <li>
                                    <c:if test="${pageItem == p}">
                                        <span class="pager-selected">${pageItem}</span>
                                    </c:if>
                                    <c:if test="${pageItem != p}">
                                        <a href="javascript:void(0);" data-p="${pageItem}">${pageItem}</a>
                                    </c:if>
                                </li>
                            </c:forEach>

                            <c:if test="${p != pageCount}">
                                <li><span><a href="javascript:void(0);" data-p="${p + 1}">后一页</a></span></li>
                                <li><span><a href="javascript:void(0);" data-p="${pageCount}">最后一页</a></span></li>
                            </c:if>

                        </ul>
                    </div>


                </div>
            </div><!-- /.box -->


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%@include file="../share/footer.jsp" %>

    <script language="javascript">
        $(document).ready(function () {
//
//            // Javascript to enable link to tab
//            var hash = document.location.hash;
//            var prefix = "_";
//            if (hash) {
//                $('.nav-tabs a[href=' + hash.replace(prefix, "") + ']').tab('show');
//            }
//
//            // Change hash for page-reload
//            $('.nav-tabs a').on('shown.bs.tab', function (e) {
//                window.location.hash = e.target.hash.replace("#", "#" + prefix);
//            });

            $(".pagination a").click(function () {
                var page = $(this).attr("data-p");
                var levels = $("#levels").val();
                var serverIds = $("#serverIds").val();
                var uri = new URI();
                uri.setSearch("page", page);
                uri.setSearch("serverIds", serverIds);
                uri.setSearch("levels", levels);

                location.href = uri.toString();
            });
            $("#search").click(function () {

                var levels = $("#levels").val();
                var serverIds = $("#serverIds").val();

                var uri = new URI();
                uri.setSearch("page", 1);
                uri.setSearch("serverIds", serverIds);
                uri.setSearch("levels", levels);

                location.href = uri.toString();
            });
            $("#reset").click(function () {

                location.href = '<%=request.getContextPath()%>/log/${currentAppId}';
            });

            $('#daterange').daterangepicker({timePicker: true, timePickerIncrement: 1, format: 'YYYY-MM-DD HH:mm'});

            $(".copyContextId").each(function(i) {
                var client = new ZeroClipboard(document.getElementById($(this).attr("id")));

                client.on( "ready", function( readyEvent ) {
                    // alert( "ZeroClipboard SWF is ready!" );

                    client.on( "aftercopy", function( event ) {

                        //alert("已经复制了： " + event.data["text/plain"] );
                    } );
                } );
            });

        });

    </script>

</body>
</html>
