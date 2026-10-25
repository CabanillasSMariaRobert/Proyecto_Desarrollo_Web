# Esquema H2 del codigo actual

`schema-h2.sql` crea las 16 tablas, claves compuestas, restricciones de unicidad,
claves foraneas, indices y columnas calculadas definidas por las entidades JPA.
Se genero con Hibernate 6.5.3.Final, correspondiente al Spring Boot 3.3.5 del pom,
a partir de las anotaciones del codigo fuente actual, y se ejecuto con H2 2.2.224.

Las claves foraneas se declaran dentro de cada CREATE TABLE. Las tablas estan
ordenadas por dependencias para que las referencias existan al crearlas.

## Ejecucion

Ejecutar el contenido completo en una conexion H2 a una base vacia.
Es un script de estructura, sin datos de ejemplo ni instrucciones DROP.
No es una migracion y no debe ejecutarse sobre las tablas ya creadas por JPA.
Los nombres de tabla entre comillas se deben respetar, incluida su capitalizacion.
El script esta fuera de resources para evitar su ejecucion automatica al iniciar.
La aplicacion conserva su configuracion actual `ddl-auto=update`.

Los identificadores se generan automaticamente. Los importes calculados
`productos.precio_con_descuento` y los dos subtotales de `detalle_de_compra`
no se deben incluir en INSERT ni UPDATE.
El descuento de producto es una proporcion (0.20 representa 20 %), mientras
que `descuento_x_unidad` en el detalle es un importe monetario.
Al insertar directamente mediante SQL deben suministrarse los campos obligatorios,
incluidas las fechas: @CreationTimestamp, @UpdateTimestamp y @Builder.Default
son comportamientos de Java/Hibernate y no equivalen a DEFAULT SQL.

## Diferencias detectadas frente a la propuesta documental

- MetodoPagoEntity utiliza `@Table(name = "'Metodos_de_pago'")`: los apostrofos
  forman parte literal del nombre resultante `"'metodos_de_pago'"`.
  El script lo conserva para corresponder al codigo. Normalizar ese nombre
  requiere corregir la entidad y planificar el cambio de la base existente.
- ProductoEntity no tiene un atributo esta_activo; CategoriaEntity si lo tiene.
- TokenEntity declara tipo como String y token como el enum TipoToken.
  Esto limita token al valor RESTABLECIMIENTO_CLAVE; debe revisarse antes
  de implementar tokens aleatorios de recuperacion.
- El detalle ya referencia id_variante_de_zapato. No guarda talla/color historicos.
- No existe una tabla de historial de intentos de entrega.
- La combinacion zapato/talla/color aun no tiene restriccion UNIQUE en JPA.
- El estado de compra es String, con valor Java inicial "pendiente";
  no tiene un catalogo cerrado de estados a nivel SQL.
- EntregaEntity permite id_compra nulo, aunque si impone unicidad.
- El script conserva ON DELETE CASCADE en asociaciones de categorias y en
  el detalle de compra porque asi lo especifican las entidades actuales.

Estas observaciones no modifican las entidades ni implementan nuevas reglas
de negocio. El diccionario y el diagrama deben reflejar estas diferencias
o actualizarse junto con el codigo en un cambio posterior.
