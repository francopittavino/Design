<%@ include file="_encabezado.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="ventana">
    <div class="pasos" aria-label="Pasos">
        <div class="paso ${paso > 1 ? 'hecho' : 'actual'}"><div class="circ">${paso > 1 ? '&#10003;' : '1'}</div>Candidatos</div>
        <div class="guion"></div>
        <div class="paso ${paso > 2 ? 'hecho' : (paso == 2 ? 'actual' : '')}"><div class="circ">${paso > 2 ? '&#10003;' : '2'}</div>Función</div>
        <div class="guion"></div>
        <div class="paso ${paso >= 3 ? 'actual' : ''}"><div class="circ">3</div>Confirmación</div>
    </div>

    <c:choose>
    <%-- ================= PASO 1: selección de candidatos ================= --%>
    <c:when test="${paso == 1}">
        <h1>EVALUAR CANDIDATOS - PASO 1 DE 3: SELECCIÓN DE CANDIDATOS</h1>
        <form method="get" action="${ctx}/evaluar">
            <input type="hidden" name="accion" value="buscar"><input type="hidden" name="paso" value="1">
            <div class="seccion">Buscar candidatos</div>
            <div class="filtros">
                <div class="campo"><label for="apellido">Apellido</label>
                    <input type="text" id="apellido" name="apellido" value="${fn:escapeXml(filtros.apellido)}" autofocus></div>
                <div class="campo"><label for="nombre">Nombre</label>
                    <input type="text" id="nombre" name="nombre" value="${fn:escapeXml(filtros.nombre)}"></div>
                <div class="campo"><label for="nroCandidato">Nro. candidato</label>
                    <input type="text" id="nroCandidato" name="nroCandidato" value="${fn:escapeXml(filtros.nroCandidato)}"></div>
                <div><button type="submit" class="primario">&#128269; Buscar</button></div>
            </div>
        </form>

        <form method="post" action="${ctx}/evaluar">
            <c:forEach var="f" items="${filtros}"><input type="hidden" name="${f.key}" value="${fn:escapeXml(f.value)}"></c:forEach>
            <div class="seccion">Candidatos encontrados</div>
            <div class="scroll" style="max-height:200px">
                <table class="grilla">
                    <thead><tr><th style="width:36px"></th><th>Apellido</th><th>Nombre</th><th>Tipo doc.</th><th>Nro. doc.</th><th>Nro. candidato</th></tr></thead>
                    <tbody>
                    <c:forEach var="c" items="${candidatos}">
                        <tr class="seleccionable" onclick="if (event.target.type !== 'checkbox') { var x = this.querySelector('input'); x.checked = !x.checked; }">
                            <td class="centro"><input type="checkbox" name="nro" value="${fn:escapeXml(c.nroCandidato)}" aria-label="Seleccionar ${fn:escapeXml(c.apellido)}"></td>
                            <td>${fn:escapeXml(c.apellido)}</td><td>${fn:escapeXml(c.nombre)}</td>
                            <td>${c.tipoDoc}</td><td>${fn:escapeXml(c.nroDoc)}</td><td>${fn:escapeXml(c.nroCandidato)}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty candidatos}"><tr class="vacio"><td colspan="6">Complete los filtros y presione Buscar (sin filtros trae todos).</td></tr></c:if>
                    </tbody>
                </table>
            </div>
            <div class="botonera"><button type="submit" name="accion" value="agregar">Agregar seleccionados &darr;</button></div>
        </form>

        <form method="post" action="${ctx}/evaluar" style="display:contents">
            <c:forEach var="f" items="${filtros}"><input type="hidden" name="${f.key}" value="${fn:escapeXml(f.value)}"></c:forEach>
            <div class="seccion">Candidatos a evaluar (${fn:length(ev.candidatosAEvaluar)})</div>
            <div class="scroll" style="max-height:200px">
                <table class="grilla">
                    <thead><tr><th style="width:36px"></th><th>Apellido</th><th>Nombre</th><th>Tipo doc.</th><th>Nro. doc.</th><th>Nro. candidato</th></tr></thead>
                    <tbody>
                    <c:forEach var="c" items="${ev.candidatosAEvaluar}">
                        <tr class="seleccionable" onclick="if (event.target.type !== 'checkbox') { var x = this.querySelector('input'); x.checked = !x.checked; }">
                            <td class="centro"><input type="checkbox" name="quitar" value="${fn:escapeXml(c.nroCandidato)}" aria-label="Quitar ${fn:escapeXml(c.apellido)}"></td>
                            <td>${fn:escapeXml(c.apellido)}</td><td>${fn:escapeXml(c.nombre)}</td>
                            <td>${c.tipoDoc}</td><td>${fn:escapeXml(c.nroDoc)}</td><td>${fn:escapeXml(c.nroCandidato)}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty ev.candidatosAEvaluar}"><tr class="vacio"><td colspan="6">Todavía no se agregaron candidatos.</td></tr></c:if>
                    </tbody>
                </table>
            </div>
            <div class="botonera"><button type="submit" name="accion" value="quitar">&larr; Quitar seleccionados</button></div>

            <div class="acciones">
                <a class="boton" href="${ctx}/menu">Cancelar</a>
                <button type="submit" name="accion" value="siguiente1" class="primario">Siguiente &rarr;</button>
            </div>
        </form>
    </c:when>

    <%-- ================= PASO 2: selección de función / puesto ================= --%>
    <c:when test="${paso == 2}">
        <h1>EVALUAR CANDIDATOS - PASO 2 DE 3: SELECCIÓN DE FUNCIÓN</h1>
        <div class="seccion">Funciones disponibles</div>
        <div class="scroll" style="max-height:220px">
            <table class="grilla">
                <thead><tr><th>Código</th><th>Función</th><th>Empresa</th></tr></thead>
                <tbody>
                <c:forEach var="p" items="${puestos}">
                    <tr class="seleccionable ${ev.puesto.id == p.id ? 'seleccionada' : ''}"
                        onclick="document.getElementById('idPuesto').value='${p.id}'; document.getElementById('form-puesto').submit()">
                        <td>${fn:escapeXml(p.codigo)}</td><td>${fn:escapeXml(p.nombre)}</td><td>${fn:escapeXml(p.empresa)}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <form method="post" action="${ctx}/evaluar" id="form-puesto">
            <input type="hidden" name="accion" value="elegirPuesto"><input type="hidden" name="idPuesto" id="idPuesto">
            <noscript><p class="ayuda">Requiere JavaScript para seleccionar la función.</p></noscript>
        </form>

        <c:if test="${not empty ev.puesto}">
            <div class="seccion">Competencias requeridas - ${fn:escapeXml(ev.puesto.nombre)} / ${fn:escapeXml(ev.puesto.empresa)}</div>
            <table class="grilla">
                <thead><tr><th>Competencia</th><th class="num">Ponderación mínima</th></tr></thead>
                <tbody>
                <c:forEach var="cp" items="${ev.puesto.caracteristicas}">
                    <tr><td>${fn:escapeXml(cp.competencia)}</td><td class="num">${cp.ponderacionMinima}</td></tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty ev.puesto}"><p class="ayuda">Seleccione una función de la grilla para ver sus competencias.</p></c:if>

        <form method="post" action="${ctx}/evaluar" class="acciones">
            <a class="boton izq" href="${ctx}/evaluar?paso=1">&larr; Anterior</a>
            <a class="boton" href="${ctx}/evaluar?accion=nueva">Cancelar</a>
            <button type="submit" name="accion" value="siguiente2" class="primario" autofocus>Siguiente &rarr;</button>
        </form>
    </c:when>

    <%-- ================= PASO 3: confirmación ================= --%>
    <c:when test="${paso == 3}">
        <h1>EVALUAR CANDIDATOS - PASO 3 DE 3: CONFIRMACIÓN
            <small>${fn:escapeXml(ev.puesto.nombre)} / ${fn:escapeXml(ev.puesto.empresa)}</small></h1>
        <div class="seccion">Candidatos a evaluar</div>
        <p class="ayuda">* Clave de ingreso generada automáticamente (8 caracteres alfanuméricos).</p>
        <table class="grilla">
            <thead><tr><th>Apellido</th><th>Nombre</th><th>Tipo doc.</th><th>Nro. documento</th><th>Clave de ingreso</th></tr></thead>
            <tbody>
            <c:forEach var="c" items="${ev.claves}">
                <tr><td>${fn:escapeXml(c.apellido)}</td><td>${fn:escapeXml(c.nombre)}</td><td>${c.tipoDoc}</td>
                    <td>${fn:escapeXml(c.nroDoc)}</td><td><b>${c.token}</b></td></tr>
            </c:forEach>
            </tbody>
        </table>
        <c:choose>
            <c:when test="${empty ev.evaluacion}">
                <form method="post" action="${ctx}/evaluar" class="acciones">
                    <a class="boton izq" href="${ctx}/evaluar?paso=2">&larr; Anterior</a>
                    <a class="boton" href="${ctx}/evaluar?accion=nueva">Cancelar</a>
                    <button type="submit" name="accion" value="finalizar" class="primario" autofocus>Finalizar</button>
                </form>
            </c:when>
            <c:otherwise>
                <div class="aviso ok">Evaluación N° ${ev.evaluacion.id} guardada: ${ev.evaluacion.cantCandidatos} cuestionario(s) en estado "Activo".</div>
                <div class="acciones"><a class="boton primario" href="${ctx}/menu">Volver al menú principal</a></div>
            </c:otherwise>
        </c:choose>
    </c:when>

    <%-- ================= Pasos 15-17: guardar el archivo Excel ================= --%>
    <c:when test="${paso == 4}">
        <h1>EXPORTAR CANDIDATOS Y CLAVES</h1>
        <div class="seccion">Guardar archivo</div>
        <p>Se va a generar el archivo <b>${fn:escapeXml(nombreArchivo)}</b> con la lista de candidatos y sus claves de ingreso.</p>
        <p class="ayuda">Al presionar "Guardar" el navegador le pide la carpeta donde guardarlo (o lo deja en su carpeta de descargas,
            según la configuración del navegador).</p>
        <div class="aviso info" id="guardado" hidden>Archivo generado. Puede volver al menú principal.</div>
        <form method="post" action="${ctx}/evaluar" class="acciones">
            <button type="submit" name="accion" value="preguntarExportar">Cancelar</button>
            <a class="boton primario" href="${ctx}/evaluar/claves" download="${fn:escapeXml(nombreArchivo)}"
               onclick="document.getElementById('guardado').hidden = false; document.getElementById('volver').hidden = false">&#128190; Guardar</a>
            <a class="boton" id="volver" href="${ctx}/menu" hidden>Volver al menú principal</a>
        </form>
    </c:when>
    </c:choose>
</div>
<%@ include file="_pie.jspf" %>
