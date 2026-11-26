<%@ include file="_encabezado.jspf" %>
<div class="ventana angosta">
    <h1>INGRESO AL SISTEMA</h1>

    <c:choose>
        <c:when test="${tipo == 'consultor'}">
            <form method="post" action="${pageContext.request.contextPath}/login">
                <input type="hidden" name="tipo" value="consultor">
                <div class="seccion">Ingresar como usuario consultor</div>
                <div class="campo"><label for="usuario">Usuario</label>
                    <input type="text" id="usuario" name="usuario" class="sin-mayus" autofocus autocomplete="username"></div>
                <br>
                <div class="campo"><label for="password">Contraseña</label>
                    <input type="password" id="password" name="password" autocomplete="current-password"></div>
                <p class="ayuda">La contraseña se valida contra el LDAP de la organización.</p>
                <div class="acciones">
                    <a class="boton" href="${pageContext.request.contextPath}/login">Volver</a>
                    <button type="submit" class="primario">Ingresar</button>
                </div>
            </form>
        </c:when>
        <c:when test="${tipo == 'candidato'}">
            <form method="post" action="${pageContext.request.contextPath}/login">
                <input type="hidden" name="tipo" value="candidato">
                <div class="seccion">Realizar cuestionario</div>
                <div class="filtros">
                    <div class="campo" style="flex:0 0 120px"><label for="tipoDoc">Tipo</label>
                        <select id="tipoDoc" name="tipoDoc">
                            <c:forEach var="t" items="${tiposDoc}"><option>${t}</option></c:forEach>
                        </select></div>
                    <div class="campo"><label for="nroDoc">Número de documento</label>
                        <input type="text" id="nroDoc" name="nroDoc" autofocus></div>
                </div>
                <br>
                <div class="campo"><label for="clave">Clave</label>
                    <input type="password" id="clave" name="clave" maxlength="8"></div>
                <div class="acciones">
                    <a class="boton" href="${pageContext.request.contextPath}/login">Volver</a>
                    <button type="submit" class="primario">Ingresar</button>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <div class="seccion">Seleccione el tipo de acceso</div>
            <div class="menu">
                <a href="${pageContext.request.contextPath}/login?tipo=consultor"><b>Ingresar como usuario consultor</b>
                    <span>Gestión de preguntas, evaluaciones y reportes.</span></a>
                <a href="${pageContext.request.contextPath}/login?tipo=candidato"><b>Realizar cuestionario</b>
                    <span>Con su tipo y número de documento y la clave que le entregó la consultora.</span></a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%@ include file="_pie.jspf" %>
