<%@ page isErrorPage="true" %>
<%@ include file="_encabezado.jspf" %>
<div class="ventana angosta">
    <h1>ERROR</h1>
    <div class="aviso error">No se pudo completar la operación. Intente nuevamente; si el problema sigue, avise al administrador.</div>
    <p class="ayuda">${fn:escapeXml(pageContext.exception.message)}</p>
    <div class="acciones"><a class="boton primario" href="${pageContext.request.contextPath}/login">Volver al inicio</a></div>
</div>
<%@ include file="_pie.jspf" %>
