<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!--
  Registro General CAIB - Registro de Entradas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficinaSalida");
String numeroSalida=request.getParameter("numeroSalida");
String numeroOficio=request.getParameter("oficio");
String ano=request.getParameter("ano");
%>
<%
RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada pregent = new ParametrosRegistroEntrada();
ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();

ValoresFacade valores = ValoresFacadeUtil.getHome().create();
OficioRemisionFacade ofi = OficioRemisionFacadeUtil.getHome().create();
ParametrosOficioRemision param = new ParametrosOficioRemision();
ParametrosOficioRemision oficio = new ParametrosOficioRemision();

Integer intSerie=(Integer)session.getAttribute("serie");
if (intSerie==null) {
    intSerie=new Integer(0);
    session.setAttribute("serie", intSerie);
}
int serie=intSerie.intValue();
int serieForm = Integer.parseInt(request.getParameter("serie"));

/*
if (serie>serieForm) {
    session.setAttribute("errorAtras","1");
%->
    <jsp:forward page="pedirdatos.jsp" />
       <-% }
 */
    
    serie++;
    // intSerie++;
    intSerie=Integer.valueOf(String.valueOf(serie));
    session.setAttribute("serie", intSerie);
    session.removeAttribute("errorAtras");
%>

<%
registro.fijaUsuario(usuario);
registro.setCorreo(request.getParameter("correo"));
registro.setdataentrada(request.getParameter("dataentrada"));
registro.sethora(request.getParameter("hora"));
registro.setoficina(request.getParameter("oficina"));
registro.setoficinafisica(request.getParameter("oficinafisica"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setentidad1(request.getParameter("entidad1"));
registro.setentidad2(request.getParameter("entidad2"));
registro.setaltres(request.getParameter("altres"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setsalida1(request.getParameter("salida1"));
registro.setsalida2(request.getParameter("salida2"));
registro.setdestinatari(request.getParameter("destinatari"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
if(request.getParameter("mun_060")!=null)
	registro.setMunicipi060(request.getParameter("mun_060"));

registro.setcomentario(request.getParameter("comentario"));
%>

<% 

param.setAnoOficio(ano);
param.setOficinaOficio(codOficina);
param.setNumeroOficio(numeroOficio);
oficio = ofi.leer(param);

boolean ok=regent.validar(registro);
if (!ok || oficio==null){
	if (oficio==null) {
		registro.getErrores().put("","Error inesperat, no s'ha pogut obtenir les dades de l'ofici");

	}
    request.setAttribute("registroEntrada",registro);
    
    request.setAttribute( "oficina", codOficina);
    request.setAttribute( "numeroSalida", numeroSalida);
    request.setAttribute( "oficio", numeroOficio);
    request.setAttribute( "ano", ano);


%>
        <jsp:forward page="RemiSalidaPaso.jsp" />
<% } else { 
    
    boolean grabado=regent.grabar(registro);
    if (!grabado) {
        request.setAttribute("registroEntrada",registro);


        request.setAttribute( "oficina", codOficina);
    	request.setAttribute( "numeroSalida", numeroSalida);
        request.setAttribute( "oficio", numeroOficio);
   		request.setAttribute( "ano", ano);

%>
                <jsp:forward page="RemiSalidaPaso.jsp" />

<%            } else {
			oficio.setDescartadoEntrada("N");
			oficio.setMotivosDescarteEntrada("");
			oficio.setUsuarioEntrada(usuario);
			oficio.setAnoEntrada(registro.getAnoEntrada());
			oficio.setOficinaEntrada(registro.getOficina());
			oficio.setNumeroEntrada(registro.getNumeroEntrada());
			oficio.setFechaEntrada(registro.getDataEntrada());
			ofi.actualizar(oficio);

	
		String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
        String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
        String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");
        
        if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
            valores.liberarDisquete(bloqueoOficina,bloqueoTipo,bloqueoAno,usuario);
            session.removeAttribute("bloqueoOficina");
            session.removeAttribute("bloqueoTipo");
            session.removeAttribute("bloqueoAno");
            session.removeAttribute("bloqueoUsuario");
            session.removeAttribute("bloqueoDisquete");
        }
%> 

<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache">
        <script src="jscripts/TAO.js"></script>
    </head>
    <body>
        
     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="pedirdatos.jsp"><fmt:message key='registre_entrades'/></a></li>
		<li><fmt:message key='registre_entrada_creat'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
        <table class="recuadroEntradas" width="400" align="center">
            <tr>
                <td style="border:0" >
                    &nbsp;<br><center><b><fmt:message key='ofici'/> <%=oficio.getNumeroOficio()%>/<%=oficio.getAnoOficio()%></b></center></p>
                </td>
            </tr>   
            <tr>   
                <td style="border:0" >
                    &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%> <fmt:message key='desat_correctament'/></b></center></p>
                </td>
            </tr>   
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p><center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p>
                    <center>
                    	<a style="text-decoration: none;" type="button" target="_blank" class="botonFormulario" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E">
                        &nbsp;<fmt:message key='imprimir_segell'/>&nbsp;</a>
                    	<a style="text-decoration: none;" type="button" class="botonFormulario" href="RemiSalidaLis.jsp">
                        &nbsp;<fmt:message key='tornar'/>&nbsp;</a>
                    </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
        </table>
        
        <%
}
}


        %>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
        
                 
    </body>
</html>