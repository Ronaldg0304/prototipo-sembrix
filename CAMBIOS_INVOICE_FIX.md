# Correcciones del Endpoint POST /api/v1/invoices

## Resumen de Cambios

Se han realizado correcciones completas al endpoint de creación de facturas (invoices) para implementar todas las validaciones y cálculos requeridos.

## Cambios Realizados

### 1. Nueva Excepción: `InsufficientStockException`
- **Archivo**: `com.sena.sembrix.exception.InsufficientStockException`
- **Descripción**: Excepción personalizada para manejar casos donde no hay stock suficiente
- **Uso**: Se lanza cuando hay intentos de crear items de factura sin stock disponible

### 2. DTO Actualizado: `InvoiceDto`
- **Cambio**: Se agregó campo `List<InvoiceItemDto> items`
- **Propósito**: Permitir que la respuesta incluya los items de la factura
- **Beneficio**: Respuesta completa con detalles de los items

### 3. DTO Actualizado: `InvoiceItemDto`
- **Cambio**: Se agregó campo `String productName`
- **Propósito**: Mostrar el nombre del producto en la respuesta
- **Beneficio**: Información más clara en la respuesta JSON

### 4. Servicio Completamente Refactorizado: `InvoiceServiceImpl`
Implementa toda la lógica requerida:

#### a) Validación de Entrada
- Valida que la factura tenga al menos un item
- Valida que todos los `inventoryId` existan en la base de datos
- Valida que todos los items tengan `quantity` positiva

#### b) Cálculo de Montos
- **Total Amount**: Suma de (unitPrice * quantity) para cada item
- **Subtotal de Items**: unitPrice * quantity
- **Unit Price**: Se obtiene del Inventory existente

#### c) Actualización de Stock
- Para cada item en la factura:
  - Valida que hay stock suficiente
  - Lanza `InsufficientStockException` si no hay stock
  - Reduce el stock: `currentStock -= quantity`
  - Actualiza `lastUpdated` con fecha actual

#### d) Creación de Sale Record
- Se crea automáticamente después de guardar la factura
- **Campos calculados**:
  - `salePrice`: Total de la factura
  - `totalCost`: Suma de costos de producción asociados
  - `profit`: salePrice - totalCost
  - `date`: Fecha de la factura
  - `profileProducer`: Obtenido del Customer
  - `invoice`: Referencia a la factura creada

#### e) Gestión Transaccional
- La clase está anotada con `@Transactional`
- Si cualquier paso falla, todo se revierte (rollback)
- Garantiza consistencia de datos

### 5. Mappers Actualizados

#### a) `InvoiceItemMapper`
- **Cambio**: Se actualiza `toDto()` para incluir `productName`
- **Extrae**: El nombre del producto desde `Inventory -> Product -> name`

#### b) `InvoiceMapper`
- **Cambio**: Se incluye la carga de items en `toDto()`
- **Inyecta**: `InvoiceItemRepository` e `InvoiceItemMapper`
- **Resultado**: El DTO de factura incluye lista de items

### 6. Entidad Actualizada: `Customer`
- **Cambio**: Se agregó relación `ManyToOne` con `ProfileProducer`
- **Campo**: `@ManyToOne private ProfileProducer profileProducer`
- **Propósito**: Vincular clientes con productores para la creación de Sales

### 7. Manejador Global de Excepciones Actualizado
- **Archivo**: `GlobalExceptionHandler`
- **Cambio**: Se agregó handler para `InsufficientStockException`
- **Código de Respuesta**: HTTP 400 BAD_REQUEST
- **Mensaje**: Detalle del problema de stock

### 8. Configuración del Proyecto
- **Cambio**: Java version actualizada de 21 a 17
- **Razón**: Compatibilidad con ambiente disponible
- **Archivo**: `pom.xml`

## Flujo de Creación de Factura

```
1. Cliente envía POST /api/v1/invoices con InvoiceDto
   {
     customerId: 1,
     items: [
       { inventoryId: 3, quantity: 10 },
       { inventoryId: 5, quantity: 5 }
     ]
   }

2. InvoiceServiceImpl.create() valida:
   - ✓ Al menos 1 item
   - ✓ Cada inventoryId existe
   - ✓ Hay stock suficiente
   - ✓ Customer existe

3. Cálculos:
   - Total Amount = sum(unitPrice * quantity)
   - Subtotal por item = unitPrice * quantity
   - Profit = totalAmount - totalCost

4. Actualizaciones:
   - Crea Invoice con totalAmount, date, status
   - Crea InvoiceItems
   - Reduce stock en Inventory
   - Crea Sale record

5. Respuesta 201 Created:
   {
     "success": true,
     "message": "Created",
     "data": {
       "id": 6,
       "customerId": 1,
       "totalAmount": 95000.00,
       "date": "2026-01-13T16:20:00",
       "status": "COMPLETED",
       "items": [
         {
           "id": 10,
           "inventoryId": 3,
           "productName": "Tomatoes",
           "quantity": 10,
           "unitPrice": 4500.00,
           "subtotal": 45000.00
         },
         {
           "id": 11,
           "inventoryId": 5,
           "productName": "Lettuce",
           "quantity": 5,
           "unitPrice": 10000.00,
           "subtotal": 50000.00
         }
       ]
     }
   }

6. Si hay error (stock insuficiente):
   {
     "status": "BAD_REQUEST",
     "message": "Insufficient stock for product: Tomatoes. Available: 5, Requested: 10",
     "errors": []
   }
```

## Validaciones Implementadas

| Validación | Mensaje de Error | Código HTTP |
|-----------|-----------------|-------------|
| Sin items | "Invoice must contain at least one item" | 400 |
| inventoryId no existe | "Inventory not found with id: X" | 404 |
| Stock insuficiente | "Insufficient stock for product: X. Available: Y, Requested: Z" | 400 |
| Customer sin producer | "Customer must be associated with a producer" | 400 |

## Testing Recomendado

### Caso 1: Crear factura exitosa
```bash
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"inventoryId": 1, "quantity": 5},
      {"inventoryId": 2, "quantity": 3}
    ]
  }'
```

### Caso 2: Stock insuficiente
```bash
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"inventoryId": 1, "quantity": 1000}
    ]
  }'
```

### Caso 3: InventoryId no existe
```bash
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"inventoryId": 9999, "quantity": 5}
    ]
  }'
```

## Archivos Modificados

1. ✅ `InvoiceServiceImpl.java` - Lógica principal refactorizada
2. ✅ `InvoiceDto.java` - Campo items agregado
3. ✅ `InvoiceItemDto.java` - Campo productName agregado
4. ✅ `InvoiceMapper.java` - Incluye items en respuesta
5. ✅ `InvoiceItemMapper.java` - Incluye productName
6. ✅ `Customer.java` - Relación ProfileProducer agregada
7. ✅ `GlobalExceptionHandler.java` - Handler para InsufficientStockException
8. ✅ `InsufficientStockException.java` - Nueva excepción creada
9. ✅ `pom.xml` - Java version actualizada

## Notas Importantes

- La transacción es ACID compliant (Atomicity, Consistency, Isolation, Durability)
- Si cualquier operación falla, todo se revierte automáticamente
- El stock solo se actualiza si la factura se guarda exitosamente
- El Sale record se crea automáticamente con el invoice
- Los costos de producción se calculan desde ProductionExpenseItems si existen


