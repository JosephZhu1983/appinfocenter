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
                    <h3 class="box-title">${appInfo.name} (${appInfo.version})</h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <table class="table table-bordered">
                        <tr>
                            <th style="width: 20px"></th>
                            <th>服务器名</th>
                            <th>服务器IP</th>
                            <th>最后活跃时间</th>

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
                            </tr>
                        </c:forEach>

                    </table>
                </div>
                <!-- /.box-body -->

            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%@include file="../share/footer.jsp" %>

</body>
</html>
