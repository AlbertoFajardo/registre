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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroEntrada.oficiosRemisionInterna"/></strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="registroEntrada.buscador.oficiosRemisionInterna"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="registroEntradaBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><span class="text-danger">*</span> <spring:message code="registroEntrada.libro"/></div>
                                <div class="col-xs-8">
                                    <form:select path="registroEntrada.libro.id" items="${librosRegistro}" itemValue="id" itemLabel="nombreCompleto" cssClass="chosen-select"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="registroEntrada.anyRegistro"/></div>
                                <div class="col-xs-8">
                                    <form:select path="anyo" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach items="${anys}" var="anyo">
                                            <form:option value="${anyo}">${anyo}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>
                    </form:form>

                            <c:if test="${oficiosRemisionOrganismos != null}">

                                <div class="row">
                                    <div class="col-xs-12">

                                        <c:if test="${empty oficiosRemisionOrganismos}">
                                            <div class="alert alert-warning alert-dismissable">
                                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroEntrada.registroEntrada"/></strong>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty oficiosRemisionOrganismos}">

                                            <c:forEach var="oficiosRemisionOrganismo" items="${oficiosRemisionOrganismos}" varStatus="status">

                                                <c:if test="${oficiosRemisionOrganismo.vigente}">
                                                    <div class="alert alert-warning center">
                                                        <strong>${oficiosRemisionOrganismo.organismo.denominacion}
                                                            (${oficiosRemisionOrganismo.organismo.estado.descripcionEstadoEntidad})</strong>
                                                    </div>
                                                </c:if>

                                                <c:if test="${oficiosRemisionOrganismo.vigente == false}">
                                                    <div class="alert alert-danger center">
                                                        <strong>${oficiosRemisionOrganismo.organismo.denominacion}
                                                            (${oficiosRemisionOrganismo.organismo.estado.descripcionEstadoEntidad})</strong>
                                                        <br> <br>
                                                        <strong><spring:message
                                                                code="oficioRemision.organismoDestino.extinguido"/></strong>
                                                    </div>
                                                </c:if>

                                                <c:if test="${oficiosRemisionOrganismo.oficinas == false}">
                                                    <div class="alert alert-danger center">
                                                        <strong><spring:message
                                                                code="oficioRemision.organismoDestino.oficinaRegistral"/></strong>
                                                    </div>
                                                </c:if>

                                                <div class="alert-grey">
                                                    <spring:message code="regweb.resultados"/> <strong>${fn:length(oficiosRemisionOrganismo.oficiosRemision)}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                </div>

                                                    <div class="table-responsive">
                                                        <form:form action="${pageContext.request.contextPath}/oficioRemision/new" id="oficio${status.count}" modelAttribute="registroEntradaListForm" method="post" cssClass="form-horizontal">

                                                            <input type="hidden" id="idOrganismo" name="idOrganismo"
                                                                   value="${oficiosRemisionOrganismo.organismo.id}"/>
                                                            <input type="hidden" id="idLibro" name="idLibro" value="${registroEntradaBusqueda.registroEntrada.libro.id}"/>

                                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                                <colgroup>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col width="50">
                                                                </colgroup>
                                                                <thead>
                                                                    <tr>
                                                                        <th style="text-align:center;cursor: pointer;" onclick="seleccionarTodo('oficio${status.count}','${fn:length(oficiosRemisionOrganismo.oficiosRemision)}');"><i class="fa fa-check-square"></i></th>
                                                                        <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                                                        <th><spring:message code="registroEntrada.oficina"/></th>
                                                                        <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                                        <th><spring:message code="registroEntrada.extracto"/></th>
                                                                        <th><spring:message code="registroEntrada.interesados"/></th>
                                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                                    </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="registroEntrada" items="${oficiosRemisionOrganismo.oficiosRemision}" varStatus="indice">
                                                                        <tr>
                                                                            <td><form:checkbox id="oficio${status.count}_id${indice.index}" path="registros[${indice.index}].id" value="${registroEntrada.id}"/></td>
                                                                            <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                            <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                            <td><label class="no-bold" rel="ayuda"
                                                                                       data-content="${registroEntrada.oficina.codigo}"
                                                                                       data-toggle="popover">${registroEntrada.oficina.denominacion}</label>
                                                                            </td>
                                                                            <td>${registroEntrada.destino.denominacion}</td>
                                                                            <td>${registroEntrada.registroDetalle.extracto}</td>
                                                                            <td class="center"><label class="no-bold representante" rel="ayuda"
                                                                                    data-content="${registroEntrada.registroDetalle.nombreInteresadosHtml}"
                                                                                    data-toggle="popover">${registroEntrada.registroDetalle.totalInteresados}</label>
                                                                            </td>
                                                                            <td class="center">
                                                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                            </td>
                                                                        </tr>
                                                                </c:forEach>

                                                                </tbody>
                                                            </table>
                                                        </form:form>

                                                            <div class="btn-group">
                                                                <c:if test="${oficiosRemisionOrganismo.vigente && oficiosRemisionOrganismo.oficinas}">
                                                                    <button type="button"
                                                                            onclick="doForm('#oficio${status.count}')"
                                                                            class="btn btn-sm btn-warning dropdown-toggle">
                                                                        <spring:message
                                                                                code="oficioRemision.boton.crear"/>
                                                                    </button>
                                                                </c:if>

                                                                <c:if test="${oficiosRemisionOrganismo.vigente == false || oficiosRemisionOrganismo.oficinas == false}">
                                                                    <button type="button"
                                                                            class="btn btn-sm btn-warning disabled">
                                                                        <spring:message
                                                                                code="oficioRemision.boton.crear"/>
                                                                    </button>
                                                                </c:if>

                                                            </div>

                                                    </div>
                                                <br>

                                            </c:forEach>

                                        </c:if>

                                    </div>
                                </div>

                            </c:if>


                        </div>
                </div>
            </div>
        </div>

        <!-- FIN BUSCADOR -->




    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    //Selecciona todos los oficios de remision de un organismo
    function seleccionarTodo(nomOficio,filas) {
        var len = parseInt(filas);
        var nombre = "#"+nomOficio+"_id0";
        //Selecciona todos los checks o los deselecciona todos a la vez
        if (len > 0) {
            if ($(nombre).prop('checked')) {
                for (var i = 0; i < len; i++) {
                    nombre = "#"+nomOficio+"_id" +i;
                    $(nombre).prop('checked', false);
                }
            } else {
                for (var i = 0; i < len; i++) {
                    nombre = "#"+nomOficio+"_id" +i;
                    $(nombre).prop('checked', true);
                }
            }
        }
    }
</script>

</body>
</html>