<%@ include file="_encabezado.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="q" value="${sessionScope.cuestionarioDTO}"/>
<div class="ventana angosta" style="max-width:860px">
    <c:choose>
    <%-- ================= Instrucciones ================= --%>
    <c:when test="${vista == 'instrucciones'}">
        <h1>CUESTIONARIO - INSTRUCCIONES<small>${fn:escapeXml(q.puesto)}</small></h1>
        <div class="instrucciones">${fn:escapeXml(instrucciones)}</div>
        <form method="post" action="${ctx}/cuestionario" class="acciones">
            <a class="boton" href="${ctx}/login?accion=salir">Cancelar</a>
            <button type="submit" name="accion" value="iniciar" class="primario" autofocus>Aceptar</button>
        </form>
    </c:when>

    <%-- ================= Bloque de preguntas (paso del wizard) ================= --%>
    <c:when test="${vista == 'bloque'}">
        <h1>CUESTIONARIO - BLOQUE ${q.bloqueActual} DE ${q.cantBloques}</h1>
        <div class="chico" style="text-align:center">Bloque ${q.bloqueActual} de ${q.cantBloques}</div>
        <div class="progreso" role="progressbar" aria-valuenow="${q.bloqueActual - 1}" aria-valuemax="${q.cantBloques}">
            <div style="width:${(q.bloqueActual - 1) * 100 / q.cantBloques}%"></div></div>
        <c:if test="${reingreso}">
            <div class="aviso info">&#8617; Reingreso detectado: se recuperó el cuestionario y continúa en el bloque ${q.bloqueActual},
                el primero que todavía no fue respondido.</div>
        </c:if>

        <form method="post" action="${ctx}/cuestionario" id="form-bloque">
            <input type="hidden" name="bloque" value="${q.bloqueActual}">
            <c:forEach var="p" items="${preguntas}">
                <c:set var="falta" value="${faltan[p.id.toString()]}"/>
                <fieldset class="pregunta ${falta ? 'sin-responder' : ''}" style="border:none;margin:0">
                    <legend class="nro">PREGUNTA ${p.nombre}</legend>
                    <div class="enunciado">${fn:escapeXml(p.enunciado)}</div>
                    <c:forEach var="o" items="${p.opciones}">
                        <label><input type="radio" name="resp_${p.id}" value="${o.id}"
                            ${marcadas[p.id.toString()] == o.id.toString() ? 'checked' : ''}> ${fn:escapeXml(o.nombre)}</label>
                    </c:forEach>
                </fieldset>
            </c:forEach>
            <div class="acciones">
                <a class="boton" href="${ctx}/login?accion=salir">Cancelar</a>
                <button type="submit" class="primario">${q.bloqueActual == q.cantBloques ? 'Finalizar' : 'Siguiente &rarr;'}</button>
            </div>
        </form>
    </c:when>

    <%-- ================= Fin del CU ================= --%>
    <c:otherwise>
        <h1>CUESTIONARIO</h1>
        <div class="aviso ${finOk ? 'ok' : 'error'}" style="font-size:15px;text-align:center;padding:24px">
            ${finOk ? '&#10004;' : '&#9888;'} ${fn:escapeXml(mensajeFin)}
        </div>
        <div class="acciones"><a class="boton primario" href="${ctx}/login">Salir</a></div>
    </c:otherwise>
    </c:choose>
</div>
<%@ include file="_pie.jspf" %>
