<%@ include file="_encabezado.jspf" %>
<div class="ventana angosta" style="max-width:860px">
    <h1>DAR ALTA PREGUNTA</h1>

    <c:if test="${not empty errores}">
        <div class="aviso error" role="alert">
            <b>&#9888; No se pudo registrar la pregunta:</b>
            <ul><c:forEach var="e" items="${errores}"><li>${fn:escapeXml(e)}</li></c:forEach></ul>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/preguntas/nueva" id="form-alta">
        <%-- <Enter> en un campo = "Aceptar" (botón por defecto del formulario) --%>
        <button type="submit" name="accion" value="aceptar" tabindex="-1" aria-hidden="true" style="position:absolute;left:-9999px"></button>
        <div class="dos-columnas">
            <div class="campo"><label for="idCompetencia">Competencia <span class="obl">(*)</span></label>
                <select id="idCompetencia" name="idCompetencia" autofocus>
                    <option value="">-- SELECCIONE --</option>
                    <c:forEach var="co" items="${competencias}">
                        <option value="${co.id}" ${p.idCompetencia == co.id ? 'selected' : ''}>${fn:escapeXml(co.nombre)}</option>
                    </c:forEach>
                </select></div>
            <div class="campo"><label for="idFactor">Factor <span class="obl">(*)</span> <span class="nota">(depende de comp.)</span></label>
                <select id="idFactor" name="idFactor">
                    <option value="">-- SELECCIONE --</option>
                    <c:forEach var="f" items="${factores}">
                        <option value="${f.id}" ${p.idFactor == f.id ? 'selected' : ''}>${fn:escapeXml(f.nombre)}</option>
                    </c:forEach>
                </select></div>
        </div>
        <br>
        <div class="campo"><label for="nombre">Nombre de pregunta <span class="obl">(*)</span> <span class="nota">(máx. 200)</span></label>
            <input type="text" id="nombre" name="nombre" maxlength="200" value="${fn:escapeXml(p.nombre)}"></div>
        <br>
        <div class="campo"><label for="enunciado">Pregunta <span class="obl">(*)</span> <span class="nota">(máx. 500)</span></label>
            <textarea id="enunciado" name="enunciado" maxlength="500" rows="3">${fn:escapeXml(p.enunciado)}</textarea></div>
        <br>
        <div class="campo"><label for="descripcion">Descripción <span class="nota">(opcional, máx. 300)</span></label>
            <input type="text" id="descripcion" name="descripcion" maxlength="300" value="${fn:escapeXml(p.descripcion)}"></div>
        <br>
        <div class="campo"><label for="idOpcion">Opción de respuesta <span class="obl">(*)</span></label>
            <select id="idOpcion" name="idOpcion">
                <option value="">-- SELECCIONE --</option>
                <c:forEach var="o" items="${opciones}">
                    <option value="${o.id}" ${p.idOpcionRespuesta == o.id ? 'selected' : ''}>${fn:escapeXml(o.nombre)}</option>
                </c:forEach>
            </select></div>

        <div class="seccion">Opciones de respuesta</div>
        <p class="ayuda">* Las ponderaciones deben sumar exactamente 10 puntos y ser 0 o positivas.</p>
        <table class="grilla">
            <thead><tr><th style="width:80px">Orden</th><th>Descripción</th><th style="width:200px">Ponderación (editable)</th></tr></thead>
            <tbody id="items">
            <c:forEach var="i" items="${items}">
                <tr><td>${i.orden}</td><td>${fn:escapeXml(i.descripcion)}</td>
                    <td><input type="number" step="0.01" min="0" name="pond_${i.id}" class="pond" value="${fn:escapeXml(pond[i.id.toString()])}"></td></tr>
            </c:forEach>
            <c:if test="${empty items}"><tr class="vacio"><td colspan="3">Seleccione una opción de respuesta.</td></tr></c:if>
            </tbody>
            <tfoot><tr><td colspan="2" class="derecha"><b>Suma</b></td><td><b id="suma">-</b> / 10</td></tr></tfoot>
        </table>

        <div class="acciones">
            <button type="submit" name="accion" value="ia" class="izq">&#10024; Generar con IA</button>
            <button type="submit" formaction="${pageContext.request.contextPath}/preguntas" formnovalidate>Cancelar</button>
            <button type="submit" name="accion" value="aceptar" class="primario">Aceptar</button>
        </div>
    </form>
</div>
<script>
    var ctx = '${pageContext.request.contextPath}';
    function recalcular() {
        var total = 0, hay = false;
        document.querySelectorAll('.pond').forEach(function (el) {
            if (el.value !== '') { hay = true; total += parseFloat(el.value) || 0; }
        });
        document.getElementById('suma').textContent = hay ? total.toFixed(2) : '-';
    }
    document.getElementById('items').addEventListener('input', recalcular);
    recalcular();

    // Observación del CU021: al seleccionar una Competencia se cargan TODOS sus Factores
    document.getElementById('idCompetencia').addEventListener('change', function () {
        var sel = document.getElementById('idFactor');
        sel.innerHTML = '<option value="">-- SELECCIONE --</option>';
        if (!this.value) return;
        fetch(ctx + '/preguntas/nueva?accion=factores&idCompetencia=' + this.value)
            .then(function (r) { return r.json(); })
            .then(function (lista) {
                lista.forEach(function (f) {
                    var o = document.createElement('option');
                    o.value = f.id; o.textContent = f.nombre; sel.appendChild(o);
                });
            });
    });

    // Al seleccionar la Opción de Respuesta: grilla Orden / Descripción / Ponderación ordenada por orden
    document.getElementById('idOpcion').addEventListener('change', function () {
        var tbody = document.getElementById('items');
        if (!this.value) {
            tbody.innerHTML = '<tr class="vacio"><td colspan="3">Seleccione una opción de respuesta.</td></tr>';
            recalcular();
            return;
        }
        fetch(ctx + '/preguntas/nueva?accion=items&idOpcion=' + this.value)
            .then(function (r) { return r.json(); })
            .then(function (lista) {
                tbody.innerHTML = '';
                lista.forEach(function (i) {
                    var tr = document.createElement('tr');
                    var tdO = document.createElement('td'); tdO.textContent = i.orden;
                    var tdD = document.createElement('td'); tdD.textContent = i.descripcion;
                    var tdP = document.createElement('td');
                    var inp = document.createElement('input');
                    inp.type = 'number'; inp.step = '0.01'; inp.min = '0'; inp.name = 'pond_' + i.id; inp.className = 'pond';
                    tdP.appendChild(inp);
                    tr.appendChild(tdO); tr.appendChild(tdD); tr.appendChild(tdP);
                    tbody.appendChild(tr);
                });
                recalcular();
            });
    });
</script>
<%@ include file="_pie.jspf" %>
