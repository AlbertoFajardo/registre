<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="modeloRecibo.listado"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <a class="btn btn-success btn-xs pull-right margin-left10" href="<c:url value="/modeloRecibo/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="modeloRecibo.nuevo"/></a>
                        <a data-toggle="modal" href="#myModalModeloRecibo" class="btn btn-warning btn-xs pull-right"><spring:message code="regweb.ayuda"/></a>
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="modeloRecibo.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty listado}">
                            <div class="alert alert-warning alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="modeloRecibo.modeloRecibo"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty listado}">

                            <div class="alert-grey">
                                <c:if test="${paginacion.totalResults == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="modeloRecibo.modeloRecibo"/>
                                </c:if>
                                <c:if test="${paginacion.totalResults > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="modeloRecibo.modeloRecibos"/>
                                </c:if>

                                <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                            </div>

                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="regweb.nombre"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var="modeloRecibo" items="${listado}">
                                            <tr>
                                                <td>${modeloRecibo.nombre}</td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" href="<c:url value="/modeloRecibo/${modeloRecibo.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                    <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/modeloRecibo/${modeloRecibo.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacion.jsp">
                                    <c:param name="entidad" value="modeloRecibo"/>
                                </c:import>

                            </div>

                        </c:if>

                    </div>

                </div>
            </div>
        </div>


    </div>
</div> <!-- /container -->

<!-- ************* <spring:message code="regweb.inicio"/> Modal Ayuda Modelo Recibo ************************** -->
<div class="modal fade bs-example-modal-lg" id="myModalModeloRecibo">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title"><spring:message code="modeloRecibo.ayuda.modeloRecibo"/></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="panel panel-success">
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12 pull-left"><spring:message code="modeloRecibo.ayuda.contenido.modeloRecibo"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" data-dismiss="modal" class="btn btn-warning"><spring:message code="regweb.cerrar"/></a>
            </div>
        </div>
    </div>
</div>
<!-- *************Fi Modal Ayuda Modelo Recibo ************************** -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>