<%@ include file="_encabezado.jspf" %>
<div class="ventana">
    <h1>ORDEN DE MÉRITO</h1>

    <form method="get" action="${pageContext.request.contextPath}/orden-merito">
        <input type="hidden" name="accion" value="buscar">
        <div class="seccion">Buscar puesto</div>
        <div class="filtros">
            <div class="campo"><label for="codigo">Código</label>
                <input type="text" id="codigo" name="codigo" maxlength="20" value="${fn:escapeXml(filtros.codigo)}" autofocus></div>
            <div class="campo"><label for="nombre">Nombre del puesto</label>
                <input type="text" id="nombre" name="nombre" maxlength="100" value="${fn:escapeXml(filtros.nombre)}"></div>
            <div class="campo"><label for="empresa">Empresa</label>
                <input type="text" id="empresa" name="empresa" maxlength="100" value="${fn:escapeXml(filtros.empresa)}"></div>
            <div><button type="submit" class="primario">&#128269; Buscar</button></div>
        </div>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/orden-merito" id="form-puesto" style="display:contents">
        <input type="hidden" name="codigo" value="${fn:escapeXml(filtros.codigo)}">
        <input type="hidden" name="nombre" value="${fn:escapeXml(filtros.nombre)}">
        <input type="hidden" name="empresa" value="${fn:escapeXml(filtros.empresa)}">

        <div class="seccion">Puestos</div>
        <div class="scroll">
            <table class="grilla">
                <thead><tr><th style="width:36px"></th><th>Código</th><th>Puesto</th><th>Empresa</th>
                    <th class="num">Candidatos</th><th class="num">Eval. completas</th></tr></thead>
                <tbody>
                <c:forEach var="p" items="${puestos}">
                    <tr class="seleccionable ${idPuesto == p.id ? 'seleccionada' : ''}" onclick="this.querySelector('input').checked = true; marcar(this)">
                        <td class="centro"><input type="radio" name="idPuesto" value="${p.id}" ${idPuesto == p.id ? 'checked' : ''}
                                                  aria-label="Seleccionar ${fn:escapeXml(p.nombre)}"></td>
                        <td>${fn:escapeXml(p.codigo)}</td>
                        <td>${fn:escapeXml(p.nombre)}</td>
                        <td>${fn:escapeXml(p.empresa)}</td>
                        <td class="num">${p.cantCandidatos}</td>
                        <td class="num">${p.cantEvaluacionesCompletas}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty puestos}"><tr class="vacio"><td colspan="6">Ingrese al menos un criterio y presione Buscar.</td></tr></c:if>
                </tbody>
            </table>
        </div>
        <p class="ayuda">Criterio de búsqueda: "contiene". Seleccione un puesto y presione "Emitir Orden de Mérito".</p>

        <div class="acciones">
            <a class="boton" href="${pageContext.request.contextPath}/menu">Cancelar</a>
            <button type="submit" class="primario" ${empty puestos ? 'disabled' : ''}>Emitir Orden de Mérito</button>
        </div>
    </form>
</div>

<%-- Paso 6: selección de la/s evaluación/es (o TODAS) --%>
<c:if test="${not empty evaluaciones}">
    <div class="modal-fondo" id="sel-evaluaciones">
        <form class="modal" method="post" action="${pageContext.request.contextPath}/orden-merito" style="min-width:480px">
            <input type="hidden" name="accion" value="emitir">
            <input type="hidden" name="idPuesto" value="${idPuesto}">
            <input type="hidden" name="codigo" value="${fn:escapeXml(filtros.codigo)}">
            <input type="hidden" name="nombre" value="${fn:escapeXml(filtros.nombre)}">
            <input type="hidden" name="empresa" value="${fn:escapeXml(filtros.empresa)}">
            <div class="titulo">Seleccionar Evaluación/es</div>
            <div class="cuerpo" style="text-align:left">
                <p class="ayuda">Seleccione la/s evaluación/es para emitir el orden de mérito:</p>
                <c:if test="${not empty errorSeleccion}"><div class="aviso error">${errorSeleccion}</div></c:if>
                <label><input type="checkbox" name="todas" value="1" id="todas" checked> <b>TODAS</b></label>
                <hr>
                <c:forEach var="e" items="${evaluaciones}" varStatus="st">
                    <label style="display:block;margin:6px 0">
                        <input type="checkbox" name="idEvaluacion" value="${e.id}" class="eval">
                        Evaluación N° ${e.id} · ${e.fechaCreacion.toLocalDate()} · ${e.cantCandidatos} candidato(s), ${e.cantCompletos} completo(s)
                    </label>
                </c:forEach>
            </div>
            <div class="botonera">
                <button type="button" onclick="document.getElementById('sel-evaluaciones').remove()">Cancelar</button>
                <button type="submit" class="primario" autofocus>Aceptar</button>
            </div>
        </form>
    </div>
</c:if>
<script>
    function marcar(tr) {
        document.querySelectorAll('tr.seleccionada').forEach(function (t) { t.classList.remove('seleccionada'); });
        tr.classList.add('seleccionada');
    }
    var todas = document.getElementById('todas');
    if (todas) {
        todas.addEventListener('change', function () {
            if (todas.checked) document.querySelectorAll('.eval').forEach(function (c) { c.checked = false; });
        });
        document.querySelectorAll('.eval').forEach(function (c) {
            c.addEventListener('change', function () { if (c.checked) todas.checked = false; });
        });
    }
</script>
<%@ include file="_pie.jspf" %>
