<%@ include file="_encabezado.jspf" %>
<div class="ventana menu-principal">
    <h1>MENÚ PRINCIPAL</h1>
    <div class="menu">
        <a href="${pageContext.request.contextPath}/preguntas"><b>Preguntas</b>
            <span>CU020 Gestionar Preguntas · CU021 Dar de alta Pregunta</span></a>
        <a href="${pageContext.request.contextPath}/evaluar"><b>Evaluar Candidatos</b>
            <span>CU025 Evaluar a Candidatos</span></a>
        <a href="${pageContext.request.contextPath}/orden-merito"><b>Orden de Méritos</b>
            <span>CU028 Emitir Orden de Mérito</span></a>
        <a class="deshabilitado"><b>Puestos</b><span>CU008 (fuera del alcance de las etapas 7 y 8)</span></a>
        <a class="deshabilitado"><b>Competencias</b><span>CU012 (fuera del alcance)</span></a>
        <a class="deshabilitado"><b>Factores</b><span>CU016 (fuera del alcance)</span></a>
        <a class="deshabilitado"><b>Opciones de Respuesta</b><span>CU024 (fuera del alcance)</span></a>
        <a class="deshabilitado"><b>Importar Candidatos</b><span>CU006 (fuera del alcance)</span></a>
        <a class="deshabilitado"><b>Reporte Comparativo</b><span>CU027 (fuera del alcance)</span></a>
    </div>
</div>
<%@ include file="_pie.jspf" %>
