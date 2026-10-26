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

El pago y la confirmacion del checkout siguen pendientes. Las operaciones existentes
de metodos de pago se conservan. La identificacion del propietario de esos metodos
continua siendo la provisional del proyecto; no se implementa autenticacion nueva.
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
