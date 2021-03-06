<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="fn" %>
<%@ taglib prefix="jstlfn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
    <link href="${appcfg.websiteStaticFileBaseUrl}font-awesome-4.3.0/css/font-awesome.min.css" rel="stylesheet"
          type="text/css"/>
    <!-- Ionicons -->
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

    <script src="http://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/daterangepicker/daterangepicker.js"
            type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}plugins/select2/select2.full.min.js" type="text/javascript"></script>
    <script src="${appcfg.websiteStaticFileBaseUrl}ZeroClipboard.min.js" type="text/javascript"></script>

    <script type="text/javascript" src="${appcfg.websiteStaticFileBaseUrl}renderjson.js"></script>

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
                <div class="box-header with-border" id="onoffsearchsection">
                    <h3 class="box-title">搜索 (点击展开或收缩)</h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body" id="searchsection">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>按用户ID：</label>
                                <input id="userId" type="text" class="form-control" value="${userId}"/>

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

                        </div>
                        <!-- /.col -->
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>按服务器：
                                    <button id="selectAllServers" class="btn-xs bg-blue copyContextId">全选
                                    </button>
                                </label>
                                <select id="serverIds" class="form-control select2" multiple="multiple"
                                        data-placeholder="选择一个或多个服务器">
                                    <c:forEach var="server" items="${servers}">
                                        <option value="${server.id}"
                                                <c:if test="${serverIds == null || fn:intcontains(serverIds,server.id) }">selected</c:if>>${server.ip}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>按URL：</label>
                                <input id="url" type="text" class="form-control" value="${url}"/>
                            </div>
                            <!-- /.form-group -->

                        </div>
                        <!-- /.col -->
                    </div>
                    <!-- /.row -->

                    <button id="search" class="btn bg-blue btn-flat margin">搜索</button>
                    <button id="reset" class="btn bg-light-blue btn-flat margin">查询所有</button>
                </div>
            </div>
            <!-- /.box -->
            <div class="box box-info">
                <!-- /.box-body -->
                <div class="box-body">
                    <table class="table table-bordered" id="logs">
                        <tr>
                            <th>发生时间</th>
                            <th>服务器名</th>
                            <th>服务器IP</th>
                            <th>用户ID</th>
                            <th>URL</th>
                            <th>QueryString</th>
                            <th style="width: 60px">详细</th>
                        </tr>
                        <c:forEach var="log" items="${logs}">
                            <tr>
                                <td><fmt:formatDate value="${log.time}"
                                                    pattern="yyyy/MM/dd HH:mm:ss"/></td>
                                <td>${log.serverName}</td>
                                <td>${log.serverIp}</td>
                                <td>${log.userId}</td>
                                <td>${log.url}</td>
                                <td>
                                    <c:if test="${jstlfn:length(log.queryString)>40}">
                                        ${jstlfn:substring(log.queryString, 0, 40)}
                                    </c:if>
                                    <c:if test="${jstlfn:length(log.queryString)<=40}">
                                        ${log.queryString}
                                    </c:if>
                                </td>
                                <td>
                                    <!-- Button trigger modal -->
                                    <button type="button" class="btn-xs bg-blue" data-toggle="modal"
                                            data-target="#detail${log.id}">
                                        查看
                                    </button>

                                    <!-- Modal -->
                                    <div class="modal fade" id="detail${log.id}" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"
                                                            aria-label="Close"><span aria-hidden="true">&times;</span>
                                                    </button>
                                                    <h4 class="modal-title"
                                                        id="myModalLabel"> ${log.serverName}/${log.serverIp} @
                                                        <fmt:formatDate value="${log.time}"
                                                                        pattern="yyyy/MM/dd HH:mm:ss"/>
                                                        - ${log.url}?${log.queryString}</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <p>请求头：${log.requestHeader}</p>

                                                    <p>请求主体：
                                                        <c:choose>
                                                        <c:when test="${jstlfn:startsWith(log.requestBody, '{')}">
                                                    <div id="requestJson${log.id}"/>
                                                    <script> document.getElementById(requestJson${log.id}").appendChild(renderjson(${log.responseBody}));</script>
                                                    </c:when>
                                                    <c:when test="${jstlfn:startsWith(log.responseBody, '<')}">
                                                        ${jstlfn:escapeXml(log.responseBody)}</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${log.responseBody}
                                                    </c:otherwise>
                                                    </c:choose>
                                                    </p>

                                                    <p>响应头：${log.responseHeader}</p>

                                                    <p>响应主体：
                                                    <c:choose>
                                                        <c:when test="${jstlfn:startsWith(log.responseBody, '{')}">
                                                            <div id="responseJson${log.id}"/>
                                                            <script> document.getElementById(responseJson${log.id}").appendChild(renderjson(${log.responseBody}));</script>
                                                        </c:when>
                                                        <c:when test="${jstlfn:startsWith(log.responseBody, '<')}">
                                                            ${jstlfn:escapeXml(log.responseBody)}</p>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${log.responseBody}
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </p>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
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

            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%@include file="../share/footer.jsp" %>

    <script language="javascript">
        function setGetParameter(url, paramName, paramValue) {
            if (url.indexOf(paramName + "=") >= 0) {
                var prefix = url.substring(0, url.indexOf(paramName));
                var suffix = url.substring(url.indexOf(paramName));
                suffix = suffix.substring(suffix.indexOf("=") + 1);
                suffix = (suffix.indexOf("&") >= 0) ? suffix.substring(suffix.indexOf("&")) : "";
                url = prefix + paramName + "=" + paramValue + suffix;
            }
            else {
                if (url.indexOf("?") < 0)
                    url += "?" + paramName + "=" + paramValue;
                else
                    url += "&" + paramName + "=" + paramValue;
            }
            return url;
        }

        $(document).ready(function () {
            $("#searchsection").hide();
            $("#onoffsearchsection").click(function () {
                $("#searchsection").slideToggle();
            });

            var begin;
            var end;

            $('#daterange').daterangepicker({timePicker: true, timePickerIncrement: 10, format: 'YYYY-MM-DD HH:mm'});
            <c:if test="${begin != null}">
            $('#daterange').data('daterangepicker').setStartDate('${begin}');
            begin = '${begin}';
            </c:if>
            <c:if test="${end != null}">
            $('#daterange').data('daterangepicker').setEndDate('${end}');
            end = '${end}';
            </c:if>
            $('#daterange').on('apply.daterangepicker', function (ev, picker) {
                begin = picker.startDate.format('YYYY/MM/DD HH:mm');
                end = picker.endDate.format('YYYY/MM/DD HH:mm');
            });


            $(".pagination a").click(function () {
                var page = $(this).attr("data-p");

                var types = $("#types").val();
                var serverIds = $("#serverIds").val();

                var url = location.href;
                url = setGetParameter(url, "page", page);
                if (serverIds)
                    url = setGetParameter(url, "serverIds", serverIds);

                if (begin)
                    url = setGetParameter(url, "begin", begin);
                if (end)
                    url = setGetParameter(url, "end", end);

                location.href = url.toString();
            });
            $("#search").click(function () {
                var types = $("#types").val();
                var serverIds = $("#serverIds").val();
                var userId = $("#userId").val();
                var url2 = $("#url").val();
                var url = location.href;
                url = setGetParameter(url, "page", 1);
                if (serverIds)
                    url = setGetParameter(url, "serverIds", serverIds);

                if (begin)
                    url = setGetParameter(url, "begin", begin);
                if (end)
                    url = setGetParameter(url, "end", end);
                if (userId != "")
                    url = setGetParameter(url, "userId", userId);
                if (url2 != "")
                    url = setGetParameter(url, "url", url2);

                location.href = url.toString();
            });
            $("#reset").click(function () {

                location.href = '<%=request.getContextPath()%>/exception/${currentAppId}';
            });
            $("#selectAllServers").click(function () {

                $("#serverIds option").attr("selected", "seleted")
            });


        });

    </script>

</body>
</html>
