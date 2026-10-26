# Calzados: administracion y catalogo

## Recorrido administrativo

1. Registrar una marca y un producto en las pantallas existentes.
2. Entrar a `/admin/zapatos` y vincular un producto activo que aun no sea calzado.
3. Abrir **Variantes y materiales** del calzado.
4. Registrar combinaciones de talla europea entera y color, con existencias por par.
5. Registrar materiales en `/admin/materiales` y asignarlos al calzado desde su detalle.
6. Editar stock o desactivar/reactivar variantes. Las combinaciones inactivas tambien
   participan en la validacion de duplicados.
7. Desactivar materiales conserva su composicion en zapatos existentes y evita
   nuevas asignaciones. Retirar una asociacion no elimina el material.

La talla, el color y el calzado de una variante existente no se cambian para
preservar las referencias de compras anteriores. Los formularios de variantes
verifican que su identificador pertenezca al calzado indicado en la ruta.

## Recorrido del cliente

- `/catalogo`: busqueda por nombre y paginacion de calzados y productos activos.
- `/catalogo/{idProducto}` (tambien `/producto/{idProducto}`): precio, composicion,
  variantes activas y existencias. Las agotadas se muestran sin permitir seleccionarlas.
- `POST /carrito/agregar`: recibe idVariante y cantidad; el precio se obtiene del backend.
- `/carrito`: cantidades y precios actuales, almacenando solo identificadores y
  cantidades en la sesion de cada navegador.
- El carrito permite actualizar cantidades y retirar articulos. No reserva ni descuenta stock.
- Al consultar el carrito se vuelve a verificar producto, zapato y variante. Un articulo
  retirado de la venta se identifica para que el usuario lo quite del carrito.

## Limites de esta integracion

El checkout utiliza pago simulado y puntos de recojo activos. Las tarjetas se consultan,
registran y eliminan con el usuario de la sesion, sin recurrir a la primera cuenta de la BD.
Las rutas administrativas conservan la configuracion de seguridad actual del curso
(permitAll). Los permisos por rol corresponden a la integracion posterior de Spring Security.

El script `schema-h2.sql` es para una base vacia, no una migracion. Se actualizaron
los estados de activacion y la restriccion unica de variante. No se ejecuta contra
la base persistente durante las pruebas. No se agregaron datos ficticios a esa base.

## Verificacion

`CalzadoWebIntegrationTest` comprueba las vistas Thymeleaf, formularios,
asociaciones, bajas logicas, precios calculados desde BD, existencias y aislamiento
de carritos por sesion, con H2 en memoria y open-in-view desactivado.

En este entorno se utiliza JBR 25 y la opcion Maven `-Dlombok.version=1.18.46`
para verificar sin modificar la version declarada en el pom del equipo.

## RN04: marcas inactivas

En `/admin/marcas` se pueden desactivar y reactivar marcas sin eliminarlas.
Las marcas nuevas y las existentes anteriores a RN04 quedan activas por defecto.
El formulario de productos ofrece marcas activas y, al editar, permite conservar
su marca actual aunque este inactiva. El servicio rechaza una nueva asignacion
inactiva, incluso si se envia directamente desde un formulario manipulado.
Desactivar una marca no desactiva sus productos ni impide sus ventas.
Editar el nombre o descripcion de una marca no cambia su estado.

La columna `esta_activo` de marcas se agrega mediante Hibernate con `ddl-auto=update`
y DEFAULT TRUE, conservando los registros existentes. El script `schema-h2.sql`
tambien incluye esa columna para bases vacias. `Rn04IntegrationTest` y
`Rn01SchemaUpdateTest` verifican el flujo y la compatibilidad del esquema anterior.

## Confirmacion de inventario

`CompraService.cambiarEstado(id, "pagado")` descuenta el stock de las variantes de
la compra. Crear una compra ya pagada mediante `guardar` aplica las mismas reglas.
Una compra pendiente no reserva ni descuenta existencias. Los avances posteriores
a enviado o entregado no descuentan de nuevo; repetir pagado tampoco.

La compra y sus variantes se bloquean durante la confirmacion. Las variantes se
bloquean en orden de identificador para reducir conflictos entre compras simultaneas.
Si alguna esta inactiva, no existe o no tiene stock suficiente, se revierte toda
la transaccion: ninguna linea pierde stock y la compra conserva su estado anterior.
Las variantes y cantidades de compras pagadas no se pueden editar. El ajuste de
stock administrativo utiliza el mismo bloqueo de variante.

El checkout crea los detalles y utiliza este servicio al aprobar el pago simulado.
No se realizan cobros reales. Las cancelaciones no reponen stock automaticamente.

## FN30: compra con pago simulado

1. En `/carrito`, agregar variantes y guardar una tarjeta de prueba con la cuenta conectada.
2. Ir a `/checkout`, elegir una tarjeta propia, un punto de recojo activo y el resultado
   del pago simulado: aprobado o rechazado.
3. `CheckoutService.confirmar` valida cliente, carrito, propiedad y vencimiento de la
   tarjeta y disponibilidad del punto. Calcula importes desde la BD y conserva nombres,
   precios y descuentos en los detalles de la compra.
4. Si se aprueba, registra la compra pagada, descuenta stock mediante `CompraService`,
   genera la boleta y crea la entrega pendiente dentro de una misma transaccion.

Si se rechaza el pago o falla una validacion, no se registra la venta ni se descuenta stock.
El carrito se vacia solo cuando finaliza correctamente la transaccion. El controlador
sincroniza la confirmacion por sesion para impedir dos confirmaciones simultaneas del
mismo carrito. Los importes y el propietario no se reciben desde el formulario.

La boleta se consulta en `/compras/{idCompra}/boleta`, limitada al propietario, y tambien
desde Mis compras. Es un comprobante academico: el RUC predeterminado es `00000000000`
y se puede configurar con `tienda.ruc`. No se genera un documento fiscal ni se integra
una pasarela externa. Los datos de entrega consisten en el punto de recojo seleccionado;
no se agregan domicilios particulares ni nuevas tablas.

`CheckoutIntegrationTest` comprueba el flujo aprobado, el rechazado, importes reales,
propiedad de tarjetas y boletas, puntos inactivos, vencimiento, cliente desactivado,
reenvio del formulario y reversion completa ante un error al emitir la boleta.

## Selector del administrador

Al iniciar sesion, una cuenta ADMINISTRADOR llega a `/seleccionar-modo`.
Puede elegir CLIENTE para ir a `/catalogo` o ADMINISTRADOR para ir a `/admin`.
La eleccion se conserva en `modoAcceso` dentro de la sesion; el rol en la base de datos
permanece igual. El enlace Cambiar modo aparece en ambas cabeceras para administradores.
Una cuenta COMUN inicia directamente en el catalogo.

El servidor comprueba que la cuenta siga activa, no baneada y tenga rol administrador
antes de aceptar la seleccion. Este selector organiza la navegacion; la proteccion
general de rutas administrativas mediante Spring Security sigue pendiente.

`InventarioYModoIntegrationTest` verifica descuento unico, reversion completa por
falta de stock, compras concurrentes por la ultima unidad, restricciones sobre compras
pagadas, redireccion del login y acceso al selector segun cuenta y rol. Usa H2 en memoria.
