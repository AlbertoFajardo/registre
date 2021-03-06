<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="modulos/imports.jsp"/>
</head>

<body>

<c:import url="modulos/menu.jsp"/>

    <div class="row-fluid container main">

        <div class="well well-white">

            <div class="row">

                <div class="col-xs-12">

                    <ol class="breadcrumb">
                        <c:import url="modulos/migadepan.jsp"/>
                    </ol>

                    <div class="alert alert-danger">
                        <strong><spring:message code="regweb.aviso"/>: </strong> ${aviso}
                    </div>
                    <c:remove var="aviso" scope="session"/>
                </div>

            </div><!-- /.row -->

        </div>
    </div>

    <c:import url="modulos/pie.jsp"/>

</body>
</html>


