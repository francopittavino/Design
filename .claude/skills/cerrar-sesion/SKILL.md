---
name: cerrar-sesion
description: Ritual de cierre de sesión de trabajo en el TP de Diseño de Sistemas (Capit@l Humano). Muestra el resumen de cambios, revisa el diff en busca de secretos o archivos que no deberían commitearse, redacta bitácora/decisión/LEEME de la entrega para aprobación, commitea en commits lógicos con Conventional Commits en español, y pushea tras rebasar contra el remoto. Usar siempre al terminar de trabajar, es el único momento en que se commitea y pushea.
---

# cerrar-sesion

Ritual de cierre de sesión para el grupo del TP (4 integrantes: Fumis,
Paletto, Pittavino, Valente). Es el único momento del flujo en que se
commitea y se pushea. Ejecutar los pasos en orden; ninguno se salta ni se
adelanta sin aprobación explícita del usuario en los puntos marcados como tal.

## Convenciones del proyecto que este skill asume

- **Bitácora**: `docs/bitacora/YYYY-MM-DD-HHmm-slug.md`.
- **Decisiones**: `docs/decisiones/NNNN-slug.md`, numeración secuencial.
  Formato sugerido: Título, Fecha, Estado (Propuesta/Aceptada/Rechazada/
  Reemplazada), Contexto, Decisión, Consecuencias (incluir qué diagramas o
  entregas afecta).
- **LEEME de la entrega**: `Entrega N - Título/LEEME - Notas de la Entrega N.md`
  hace las veces de changelog: contenido de la entrega, correcciones de la
  entrega anterior, notas de los diagramas/implementación y cómo regenerar o
  ejecutar. Se actualiza cuando la sesión cambió algo de lo que se entrega.
- **Nota de sesión**: la que abrió `abrir-sesion` en
  `.agents/session-notes/` (no versionada). Es el insumo principal de la
  entrada de bitácora de este cierre. Una vez que la bitácora quedó escrita y
  aprobada, la nota puede borrarse.
- **Co-authored-by**: no hay un roster versionado de emails. Si en la sesión
  participó más de una persona, preguntar en este paso nombre y email de
  cada co-autor para armar los trailers.

## Pasos

### 1. Mostrar el resumen de lo trabajado

- `git status`.
- `git diff --stat` (y `git diff --stat --cached` si ya hay algo en stage).
- Mostrar el contenido de la nota de sesión de `.agents/session-notes/`.

### 2. Revisar el diff en busca de cosas que no deberían commitearse

Recorré el diff completo (no solo el `--stat`) buscando, como mínimo:

- Credenciales/claves: contraseñas de la base de datos en
  `application.properties`/`persistence.xml`/`configuracion.properties`,
  API keys (por ejemplo del servicio de IA del CU033), tokens,
  `-----BEGIN PRIVATE KEY-----`, connection strings con usuario/password
  reales (los de desarrollo local tipo `postgres/postgres` están bien, pero
  avisá).
- Archivos de entorno: `.env`, `.env.*`, `*.pem`, `*.key`,
  `credentials.json` o similares.
- Datos personales reales (legajos, DNI, emails o teléfonos de personas
  reales cargados como datos de prueba).
- Salidas de compilación y dependencias: `target/`, `build/`, `out/`,
  `*.class`, `node_modules/`, `.idea/`, `*.iml`, `.vscode/` (salvo config
  compartida acordada).
- Binarios pesados que no son entregables (dumps de base, videos, `plantuml.jar`).
  **Excepción**: los entregables de cada etapa (informe `.docx`/`.pdf`, zip
  de entrega, imágenes `.png`/`.svg` de los diagramas) sí se versionan.
- Archivos temporales/basura de editor u OS (`*.tmp`, `*.bak`, `~$*.docx`,
  `.DS_Store`, `Thumbs.db`).

Si encontrás algo de esto: **FRENÁ**, mostrale al usuario qué encontraste y
en qué archivo, y proponé la entrada de `.gitignore` correspondiente (o sacar
el archivo del stage) antes de seguir a redactar nada. No seas exhaustivo
como un scanner dedicado — esto es una revisión heurística, decilo si tenés
dudas en vez de asumir que algo es seguro.

### 3. Redactar y proponer para aprobación (todavía sin escribir nada)

Presentale al usuario, para que apruebe o corrija antes de que escribas
ningún archivo:

- **Entrada de bitácora** (`docs/bitacora/YYYY-MM-DD-HHmm-slug.md`): fecha,
  integrantes, objetivo, entrega, qué se hizo, decisiones tomadas, archivos
  tocados, pendientes. Basate en la nota de sesión, no la reescribas desde
  cero.
- **Decisión** (`docs/decisiones/`) si hubo alguna de peso durante la sesión
  (tecnología, cambio de diseño que afecta varias entregas, criterio
  acordado con el auxiliar). Si no hubo ninguna, decilo explícitamente y no
  inventes una.
- **Actualización del LEEME de la entrega en curso** si la sesión cambió algo
  de lo que se entrega (diagramas, código, informe, correcciones). Incluir las
  diferencias diseño ↔ implementación que hayan quedado anotadas.

Esperá confirmación explícita antes de pasar al punto 4. Si el usuario pide
cambios al texto, iterá antes de escribir.

### 4. Recién ahí, escribir los archivos y commitear

- Escribí bitácora / decisión / LEEME ya aprobados.
- Agrupá el resto de los cambios (código, diagramas, informes) junto con
  estos en **commits lógicos y coherentes** — nunca un commit único con todo
  mezclado. Un criterio razonable: un commit por unidad de cambio con sentido
  propio (ej. un CU implementado, la corrección de un diagrama, el informe de
  la entrega, la bitácora como commit aparte si no está atada 1:1 a otro
  cambio).
- Mensajes en formato Conventional Commits, en español (`feat:`, `fix:`,
  `docs:`, `refactor:`, `test:`, `chore:`, etc.).
- Si trabajó más de una persona en la sesión, preguntá los datos de
  co-autoría que falten y agregá al final del mensaje:

  ```
  Co-authored-by: Nombre Apellido <email@ejemplo.com>
  ```

- **Mostrale al usuario los mensajes de commit propuestos (agrupados) antes
  de ejecutar `git commit`.** Esperá confirmación.

### 5. Antes de pushear, verificar que el remoto no avanzó

- `git fetch`.
- Si el remoto avanzó desde que arrancó la sesión: `git pull --rebase`.
  - Si hay conflictos: **FRENÁ**, mostrá los archivos en conflicto y esperá
    indicaciones del usuario. No los resuelvas por tu cuenta. Ojo con los
    binarios (`.docx`, `.png`, `.zip`): no se pueden mergear, hay que elegir
    una versión con el usuario.

### 6. Push

- `git push`.
- Confirmá que salió bien y mostrale al usuario el hash de los commits
  pusheados (`git log --oneline -n <cantidad>`).

### Después de pushear

- Si la nota de sesión en `.agents/session-notes/` ya no hace falta (su
  contenido pasó a la bitácora), podés borrarla. No es obligatorio: al no
  estar versionada, no ensucia el repo si queda.
