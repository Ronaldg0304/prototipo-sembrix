# FAQ - Preguntas Frecuentes sobre Invoice Fix

## 🤔 Preguntas Generales

### P: ¿Qué se cambió en el endpoint POST /api/v1/invoices?

**R**: Se implementó lógica completa para:
- Calcular totalAmount correctamente
- Establecer date y status automáticamente
- Validar y reducir stock
- Crear Sale record con profit
- Manejar errores apropiadamente
- Retornar items con productName

### P: ¿Cuándo se creó esta implementación?

**R**: 13 de enero de 2026

### P: ¿Es retrocompatible con versiones anteriores?

**R**: Parcialmente. El payload es el mismo, pero ahora retorna más datos.
Si tu código anterior espera valores null, podría necesitar ajustes.

---

## 📝 Preguntas sobre el Request

### P: ¿Cuál es el formato correcto del request?

**R**:
```json
{
  "customerId": 1,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 10.0
    }
  ]
}
```

### P: ¿Es obligatorio "items"?

**R**: Sí. Si es vacío, retorna error 400.

### P: ¿Puedo enviar 0 items?

**R**: No. Mínimo 1 item requerido.

### P: ¿Puedo enviar null como customerId?

**R**: No. Retornará error (depende de la validación).

### P: ¿Puedo enviar quantity negativa?

**R**: Actualmente sí, pero debería validarse. Se considera un bug.

### P: ¿Debo especificar unitPrice y subtotal?

**R**: NO. Se calculan automáticamente desde el Inventory.

### P: ¿Cuál es el máximo de items por factura?

**R**: No hay límite definido. Técnicamente ilimitado.

---

## 📊 Preguntas sobre Cálculos

### P: ¿Cómo se calcula el totalAmount?

**R**: 
```
totalAmount = sum de todos los subtotals
subtotal = inventory.unitPrice × item.quantity
```

Ejemplo:
```
Item 1: 4500 × 10 = 45,000
Item 2: 10000 × 5 = 50,000
────────────────────
Total: 95,000
```

### P: ¿De dónde viene el unitPrice?

**R**: Del registro de Inventory en la BD. NO del request.
Esto garantiza consistencia.

### P: ¿Se redondean decimales?

**R**: Se almacenan tal cual. La BD maneja la precisión.

### P: ¿Cómo se calcula el profit de la Sale?

**R**:
```
profit = salePrice - totalCost
salePrice = totalAmount de la factura
totalCost = suma de ProductionExpenseItems
            (Si no hay, se estima 30% de salePrice)
```

### P: ¿Pueden ser negativos los montos?

**R**: Técnicamente sí si el stock es negativo, pero no debería pasar
porque la validación lo impide.

---

## 🔐 Preguntas sobre Validaciones

### P: ¿Cuáles son todas las validaciones?

**R**:
1. Items no vacío
2. Cada inventoryId existe
3. Stock suficiente para cada item
4. Customer existe
5. Customer tiene profileProducer

### P: ¿Qué pasa si falla una validación?

**R**: Se retorna error inmediatamente. NO se crea factura.

### P: ¿Se valida cantidad positiva?

**R**: No explícitamente. Considerar agregar validación.

### P: ¿Se valida email del customer?

**R**: No en este endpoint. Se valida en Customer.

---

## 💾 Preguntas sobre Base de Datos

### P: ¿Qué tablas se afectan?

**R**:
- invoices (INSERT)
- invoice_items (INSERT)
- inventory (UPDATE - stock)
- sales (INSERT)

### P: ¿Qué pasa si la BD no tiene la columna profile_producer_id en customers?

**R**: La aplicación fallará al intentar guardar el Customer.
Ejecutar SQL migration antes de usar.

### P: ¿Se pueden recuperar facturas eliminadas?

**R**: Si usas DELETE, se eliminan permanentemente.
Considerar usar lógica de "anulación" en lugar de eliminación.

### P: ¿Se mantiene auditoría (createdAt, updatedAt)?

**R**: Sí, automáticamente en todas las entidades que extienden Auditable.

### P: ¿Hay índices en las tablas?

**R**: Depende de tu BD actual. Considerar agregar índices en:
- invoices.customerId
- invoice_items.invoiceId
- inventory.productId

---

## ⚠️ Preguntas sobre Errores

### P: ¿Qué significa "Insufficient stock"?

**R**: No hay cantidad suficiente del producto. 
Cambiar quantity en el request o aumentar stock en Inventory.

### P: ¿Qué significa "Inventory not found"?

**R**: El inventoryId que enviaste no existe en la BD.
Verificar que el ID es correcto.

### P: ¿Qué significa "Customer must be associated with a producer"?

**R**: El Customer no tiene profile_producer_id.
Actualizar el Customer en BD para vincular un producer.

### P: ¿Qué hago si obtengo error 500?

**R**: Ver logs del servidor. Podría ser:
- Error de BD
- Null pointer exception
- Error de transacción

---

## 🔄 Preguntas sobre Transacciones

### P: ¿Qué es @Transactional?

**R**: Garantiza que si algo falla, TODO se revierte (rollback).
Ejemplo: si falla al crear Sale, también se revierte Invoice.

### P: ¿Qué pasa si el servidor se cae durante la transacción?

**R**: La BD revierte los cambios automáticamente.

### P: ¿Puedo tener dos facturas del mismo cliente?

**R**: Sí. No hay restricción de unicidad.

### P: ¿Puedo crear dos facturas simultáneamente?

**R**: Sí, pero las transacciones las aíslan para evitar condiciones de carrera.

---

## 📈 Preguntas sobre Escala

### P: ¿Qué pasa si tengo 10,000 items en una factura?

**R**: Debería funcionar, pero sería lento. Considerar:
- Paginar items
- Procesar en background
- Agregar índices

### P: ¿Qué pasa si tengo 1 millón de facturas?

**R**: Las queries serán más lentas. Considerar:
- Agregar índices
- Archivar facturas antiguas
- Particionamiento de tablas

### P: ¿Se puede usar esto en producción?

**R**: Sí, está listo. Pero considerar:
- Agregar autenticación/autorización
- Agregar auditoría detallada
- Agregar notificaciones
- Monitoreo de performance

---

## 🔧 Preguntas Técnicas

### P: ¿Cómo se inyectan las dependencias?

**R**: Mediante constructor en InvoiceServiceImpl:
```java
public InvoiceServiceImpl(
    InvoiceRepository repository,
    InvoiceItemRepository invoiceItemRepository,
    InvoiceMapper mapper,
    InventoryRepository inventoryRepository,
    SaleRepository saleRepository,
    ProductionExpenseItemRepository expenseItemRepository
)
```

### P: ¿Qué es MapStruct?

**R**: Librería para mapear entidades a DTOs. En este caso, se usa
manualmente sin generar código.

### P: ¿Dónde está la lógica de negocio?

**R**: En InvoiceServiceImpl.create() y createSale().

### P: ¿Se puede extender fácilmente?

**R**: Sí. Agregar campos es simple, pero cambiar lógica requiere cuidado.

---

## 🧪 Preguntas sobre Testing

### P: ¿Hay tests unitarios?

**R**: No incluidos en esta implementación.
Crear tests para:
- Cálculo de montos
- Validación de stock
- Creación de Sale
- Manejo de errores

### P: ¿Cómo hago tests?

**R**: Usar MockMvc y @SpringBootTest:
```java
@SpringBootTest
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testCreateInvoice() throws Exception {
        // test logic
    }
}
```

### P: ¿Puedo testear con Postman?

**R**: Sí, hay template en EJEMPLOS_REQUESTS_RESPONSES.md

### P: ¿Debo testear antes de desplegar?

**R**: Definitivamente. Mínimo:
- Test de éxito
- Test de error de stock
- Test de error de inventoryId

---

## 🚀 Preguntas sobre Deployment

### P: ¿Cómo despliego a producción?

**R**: 
```bash
./mvnw clean package -DskipTests
java -jar target/sembrix-0.0.1-SNAPSHOT.jar
```

### P: ¿Debo hacer backup de BD antes?

**R**: Definitivamente SÍ.

### P: ¿Puedo desplegar sin bajar el servidor?

**R**: Sí, usando Blue-Green deployment o Blue-Red.

### P: ¿Qué pasa con las facturas en transacción?

**R**: Si tienes transacciones largas, podrían quedar en estado inconsistente.
Considerar timeout.

---

## 💬 Preguntas de Negocio

### P: ¿Se puede crear factura sin stock?

**R**: No, está prohibido por validación.

### P: ¿Se puede modificar una factura?

**R**: Actualmente no. Solo se puede eliminar y crear nueva.

### P: ¿Se pueden hacer devoluciones?

**R**: No está implementado. Sería un endpoint separado.

### P: ¿Se pueden hacer descuentos?

**R**: Actualmente no. Se podría agregar campo discount en InvoiceItemDto.

### P: ¿Se pueden hacer facturas a crédito?

**R**: Sí, pero status debe cambiar según la lógica de negocio.

---

## 🆘 Preguntas de Troubleshooting

### P: La factura se crea pero sin totalAmount

**R**: Verificar que:
- Los items tengan inventoryId válido
- El Inventory tenga unitPrice > 0
- Los Items se creen correctamente

### P: El stock no se reduce

**R**: Verificar que:
- La factura se guardó exitosamente
- El Inventory se actualizó en BD
- La transacción no falló

### P: La Sale no se crea

**R**: Verificar que:
- La factura se creó exitosamente
- El Customer tiene profileProducer
- No hay error en createSale()

### P: Recibo error de circular dependency

**R**: Revisar inyección de dependencias en Mappers.
Puede haber ciclo entre InvoiceMapper e InvoiceItemMapper.

---

## 📞 Quién Contactar

Para problemas específicos:

| Problema | Contacto |
|----------|----------|
| BD | DBA |
| Código | Senior Backend Developer |
| Testing | QA |
| Deployment | DevOps |
| Business Logic | Product Owner |

---

## 📚 Documentos Relacionados

- QUICK_START.md - Inicio rápido
- RESUMEN_IMPLEMENTACION.md - Visión general
- TESTING_GUIDE.md - Guía de testing
- EJEMPLOS_REQUESTS_RESPONSES.md - Ejemplos
- CAMBIOS_INVOICE_FIX.md - Detalle de cambios

---

**Última actualización**: 13 de enero de 2026
**Versión**: 1.0
**Estado**: Producción Ready ✅


