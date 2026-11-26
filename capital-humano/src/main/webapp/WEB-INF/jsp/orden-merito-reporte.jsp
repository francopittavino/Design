<%@ include file="_encabezado.jspf" %>
<div class="ventana">
    <h1>ORDEN DE MÉRITO
        <small>${fn:escapeXml(om.puesto.codigo)} · ${fn:escapeXml(om.puesto.nombre)} / ${fn:escapeXml(om.puesto.empresa.nombre)}
            · Evaluaciones: ${fn:escapeXml(om.descripcionEvaluaciones)}</small></h1>

    <div class="seccion">A - Candidatos que alcanzaron las ponderaciones mínimas (ordenado por puntaje)</div>
    <table class="grilla">
        <thead><tr><th class="centro">Pos.</th><th>Tipo doc.</th><th>Nro. doc.</th><th>Apellido</th><th>Nombre</th>
            <th class="num">Puntaje</th><th>Datos adicionales</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${om.alcanzaron}">
            <tr>
                <td class="centro"><b>${i.posicion}°</b></td>
                <td>${i.candidato.tipoDoc}</td>
                <td>${fn:escapeXml(i.candidato.nroDoc)}</td>
                <td>${fn:escapeXml(i.candidato.apellido)}</td>
                <td>${fn:escapeXml(i.candidato.nombre)}</td>
                <td class="num"><b><fmt:formatNumber value="${i.puntajeObtenido}" minFractionDigits="2" maxFractionDigits="2"/></b></td>
                <td class="chico">${fn:escapeXml(i.datosAdicionales)}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty om.alcanzaron}"><tr class="vacio"><td colspan="7">Ningún candidato alcanzó las ponderaciones mínimas.</td></tr></c:if>
        </tbody>
    </table>

    <div class="seccion">B - Candidatos que no alcanzaron los mínimos o con cuestionario no completo</div>
    <p class="ayuda">* El puntaje solo se muestra para cuestionarios completos. El resto se agrupa por estado del cuestionario.</p>
    <table class="grilla">
        <thead><tr><th>Tipo doc.</th><th>Nro. doc.</th><th>Apellido</th><th>Nombre</th><th class="num">Puntaje</th>
            <th>Estado</th><th>Datos adicionales</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${om.noAlcanzaron}">
            <tr>
                <td>${i.candidato.tipoDoc}</td>
                <td>${fn:escapeXml(i.candidato.nroDoc)}</td>
                <td>${fn:escapeXml(i.candidato.apellido)}</td>
                <td>${fn:escapeXml(i.candidato.nombre)}</td>
                <td class="num"><fmt:formatNumber value="${i.puntajeObtenido}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>${i.estado}</td>
                <td class="chico">${fn:escapeXml(i.datosAdicionales)}</td>
            </tr>
        </c:forEach>
        <c:forEach var="grupo" items="${om.porEstado}">
            <tr><td colspan="7" style="background:#f1f1ee"><b>Estado: ${grupo.key}</b></td></tr>
            <c:forEach var="i" items="${grupo.value}">
                <tr>
                    <td>${i.candidato.tipoDoc}</td>
                    <td>${fn:escapeXml(i.candidato.nroDoc)}</td>
                    <td>${fn:escapeXml(i.candidato.apellido)}</td>
                    <td>${fn:escapeXml(i.candidato.nombre)}</td>
                    <td class="num">-</td>
                    <td>${i.estado}</td>
                    <td class="chico">${fn:escapeXml(i.datosAdicionales)}</td>
                </tr>
            </c:forEach>
        </c:forEach>
        <c:if test="${empty om.noAlcanzaron and empty om.porEstado}"><tr class="vacio"><td colspan="7">No hay candidatos en este grupo.</td></tr></c:if>
        </tbody>
    </table>

    <div class="acciones">
        <c:url var="volver" value="/orden-merito"><c:param name="accion" value="buscar"/>
            <c:param name="codigo" value="${filtros.codigo}"/><c:param name="nombre" value="${filtros.nombre}"/><c:param name="empresa" value="${filtros.empresa}"/></c:url>
        <a class="boton" href="${volver}">Cancelar</a>
        <a class="boton primario" href="${pageContext.request.contextPath}/orden-merito/imprimir" target="_blank" rel="noopener">&#128424; Imprimir</a>
    </div>
</div>
<%@ include file="_pie.jspf" %>
