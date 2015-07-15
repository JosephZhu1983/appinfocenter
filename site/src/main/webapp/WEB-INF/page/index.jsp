<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>应用程序信息中心</title>
    <link rel="shortcut icon" href="${appcfg.websiteStaticFileBaseUrl}favicon.ico">

    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.4 -->
    <link href="${appcfg.websiteStaticFileBaseUrl}bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <!-- Font Awesome Icons -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Ionicons -->
    <link href="http://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="${appcfg.websiteStaticFileBaseUrl}dist/css/AdminLTE.css" rel="stylesheet" type="text/css"/>

    <link href="${appcfg.websiteStaticFileBaseUrl}dist/css/skins/skin-blue.min.css" rel="stylesheet" type="text/css"/>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="http://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- jQuery 2.1.4 -->
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/jQuery/jQuery-2.1.4.min.js" type="text/javascript"></script>
    <!-- Bootstrap 3.3.2 JS -->
    <script src="${appcfg.websiteStaticFileBaseUrl}bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- AdminLTE App -->
    <script src="${appcfg.websiteStaticFileBaseUrl}dist/js/app.min.js" type="text/javascript"></script>

</head>

<body class="skin-blue sidebar-mini">
<div class="wrapper">

    <%@include file="../share/header.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <%@include file="../share/side.jsp" %>
        <!-- Main content -->
        <section class="content">

            <div class="callout callout-info">
                <h4>状态</h4>
                <p>查看应用程序所运行的服务器集群的状态</p>
            </div>
            <div class="callout callout-info">
                <h4>日志</h4>
                <p>查看应用程序产生的日志</p>
            </div>
            <div class="callout callout-info">
                <h4>异常</h4>
                <p>查看应用程序产生的已处理或未处理异常</p>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%@include file="../share/footer.jsp" %>
</body>
</html>
