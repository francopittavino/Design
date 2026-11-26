<%@ include file="_encabezado.jspf" %>
<div class="ventana">
    <h1>GESTIONAR PREGUNTAS</h1>

    <form method="get" action="${pageContext.request.contextPath}/preguntas" id="form-busqueda">
        <input type="hidden" name="accion" value="buscar">
        <div class="filtros">
            <div class="campo"><label for="idCompetencia">Competencia</label>
                <select id="idCompetencia" name="idCompetencia">
                    <option value="">-- TODAS --</option>
                    <c:forEach var="co" items="${competencias}">
                        <option value="${co.id}" ${filtros.idCompetencia == co.id ? 'selected' : ''}>${fn:escapeXml(co.nombre)}</option>
                    </c:forEach>
                </select></div>
            <div class="campo"><label for="idFactor">Factor <span class="nota">(depende de competencia)</span></label>
                <select id="idFactor" name="idFactor">
                    <option value="">-- TODOS --</option>
                    <c:forEach var="f" items="${factores}">
                        <option value="${f.id}" ${filtros.idFactor == f.id ? 'selected' : ''}>${fn:escapeXml(f.nombre)}</option>
                    </c:forEach>
                </select></div>
            <div class="campo"><label for="nombre">Nombre de pregunta</label>
                <input type="text" id="nombre" name="nombre" maxlength="200" value="${fn:escapeXml(filtros.nombre)}"></div>
        </div>
        <div class="botonera">
            <button type="submit" class="primario">&#128269; Buscar</button>
            <a class="boton" href="${pageContext.request.contextPath}/preguntas/nueva">+ Nuevo</a>
        </div>
    </form>

    <div class="seccion">Preguntas</div>
    <div class="scroll" style="max-height:330px">
        <table class="grilla">
            <thead><tr><th style="width:22%">Competencia</th><th style="width:22%">Factor</th><th>Pregunta</th><th class="centro" style="width:150px">Acción</th></tr></thead>
            <tbody>
            <c:forEach var="p" items="${preguntas}">
                <tr>
                    <td>${fn:escapeXml(p.competencia)}</td>
                    <td>${fn:escapeXml(p.factor)}</td>
                    <td><b>${fn:escapeXml(p.nombre)}</b><br>${fn:escapeXml(p.enunciado)}</td>
                    <td class="centro">
                        <c:url var="urlMod" value="/preguntas"><c:param name="accion" value="noImplementado"/><c:param name="cu" value="modificar"/>
                            <c:param name="idCompetencia" value="${filtros.idCompetencia}"/><c:param name="idFactor" value="${filtros.idFactor}"/><c:param name="nombre" value="${filtros.nombre}"/></c:url>
                        <c:url var="urlBaja" value="/preguntas"><c:param name="accion" value="noImplementado"/><c:param name="cu" value="eliminar"/>
                            <c:param name="idCompetencia" value="${filtros.idCompetencia}"/><c:param name="idFactor" value="${filtros.idFactor}"/><c:param name="nombre" value="${filtros.nombre}"/></c:url>
                        <a class="boton chico" title="Modificar (CU022)" href="${urlMod}">&#9998; Modificar</a>
                        <a class="boton chico" title="Eliminar (CU023)" href="${urlBaja}">&#10005; Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty preguntas}">
                <tr class="vacio"><td colspan="4">Ingrese al menos un criterio y presione Buscar.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <p class="ayuda">El criterio de búsqueda es "contiene". Al seleccionar una Competencia se cargan sus Factores.</p>

    <div class="acciones">
        <a class="boton" href="${pageContext.request.contextPath}/menu">Salir</a>
    </div>
</div>
<script>
    // Al cambiar la competencia se recarga el combo de factores
    document.getElementById('idCompetencia').addEventListener('change', function () {
        var sel = document.getElementById('idFactor');
        sel.innerHTML = '<option value="">-- TODOS --</option>';
        if (!this.value) return;
        fetch('${pageContext.request.contextPath}/preguntas/nueva?accion=factores&idCompetencia=' + this.value)
            .then(function (r) { return r.json(); })
            .then(function (lista) {
                lista.forEach(function (f) {
                    var o = document.createElement('option');
                    o.value = f.id; o.textContent = f.nombre; sel.appendChild(o);
                });
            });
    });
</script>
<%@ include file="_pie.jspf" %>
