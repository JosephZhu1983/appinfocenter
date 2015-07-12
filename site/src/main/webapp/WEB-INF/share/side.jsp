<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

        <!-- Sidebar Menu -->
        <ul class="sidebar-menu">
            <li class="header">选择应用程序</li>


            <c:forEach var="app" items="${apps}">
                <c:choose>
                    <c:when test="${currentAppId!= null && currentAppId == app.id}">
                        <li class="treeview active">
                    </c:when>
                    <c:otherwise>
                        <li class="treeview">
                    </c:otherwise>
                </c:choose>
                <a href="#">
                <c:if test="${app.activeServers >0 && app.lastActiveTime != null}">
                    <i class="fa fa-circle" style="color:green"></i>
                </c:if>
                <c:if test="${app.activeServers ==0 && app.lastActiveTime != null}">
                    <i class="fa fa-circle" style="color:red"></i>
                </c:if>
                <c:if test="${app.lastActiveTime ==null}">
                    <i class="fa fa-circle" style="color:white"></i>
                </c:if>
               <span> ${app.name} (${app.version})</span>

                <c:if test="${app.activeServers >0}">
                    <span class="label label-primary pull-right">${app.activeServers}</span>
                </c:if>
                </a>
                <ul class="treeview-menu">
                    <li><a href="<%=request.getContextPath()%>/status/${app.id}" <c:if test='${section =="status"}'>style="color: white"</c:if>>状态</a></li>
                    <li><a href="<%=request.getContextPath()%>/log/${app.id}"  <c:if test='${section =="log"}'>style="color: white"</c:if>>日志</a></li>
                    <li><a href="<%=request.getContextPath()%>/detail/${app.id}"  <c:if test='${section =="exception"}'>style="color: white"</c:if>>异常</a></li>
                </ul>
                </li>
            </c:forEach>
            <!-- Optionally, you can add icons to the links -->
            <%--<li class="active"><a href="#"><i class="fa fa-link"></i> <span>Link</span></a></li>--%>


        </ul>
        <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
</aside>