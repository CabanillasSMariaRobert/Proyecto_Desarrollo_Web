-- Correccion para bases antiguas: la boleta debe usar la misma tabla que MetodoPagoEntity.
-- No elimina tarjetas, compras ni boletas. Respaldar la base antes de ejecutarlo.
-- Si existen boletas, comprobar antes que sus metodos de pago existan en "metodos_de_pago".
ALTER TABLE "boletas" DROP CONSTRAINT IF EXISTS FK_BOLETA_METODOPAGO;
ALTER TABLE "boletas" ADD CONSTRAINT FK_BOLETA_METODOPAGO
    FOREIGN KEY (id_metodo_de_pago) REFERENCES "metodos_de_pago" (id_metodo_de_pago);
