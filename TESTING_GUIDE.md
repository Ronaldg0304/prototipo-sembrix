# Guía de Testing - Endpoint POST /api/v1/invoices

## Prerequisitos

1. Base de datos MySQL activa
2. Servidor Spring Boot ejecutándose en http://localhost:8080
3. Al menos un Customer y Inventory registrados en la BD

## Preparación de Datos

Antes de testear, asegúrate de crear datos de prueba:

### 1. Crear un Productor (ProfileProducer)
```sql
INSERT INTO profile_producer (farm_name, region, municipality, farm_size, created_at, updated_at)
VALUES ('Granja Test', 'Región Test', 'Municipio Test', 5.0, NOW(), NOW());
-- Guardar el ID de ProfileProducer (ej: 1)
```

### 2. Crear un Usuario (opcional, si está vinculado)
```sql
INSERT INTO users (first_name, last_name, email, password, role, status, created_at, updated_at)
VALUES ('Test', 'User', 'test@example.com', 'hashed_password', 'PRODUCER', 'ACTIVE', NOW(), NOW());
```

### 3. Crear un Cliente (Customer)
```sql
INSERT INTO customers (first_name, last_name, phone, email, address, profile_producer_id, created_at, updated_at)
VALUES ('Juan', 'Pérez', '3001234567', 'juan@example.com', 'Calle 1 #1', 1, NOW(), NOW());
-- Guardar el ID (ej: 1)
```

### 4. Crear Productos (Product)
```sql
INSERT INTO products (name, description, category, unit_of_measure, created_at, updated_at)
VALUES 
('Tomates', 'Tomates frescos', 'Verdura', 'KG', NOW(), NOW()),
('Lechuga', 'Lechuga verde', 'Verdura', 'UND', NOW(), NOW());
-- Guardar los IDs (ej: 1, 2)
```

### 5. Crear Inventario (Inventory)
```sql
INSERT INTO inventory (current_stock, unit_price, alert_threshold, last_updated, product_id, profile_producer_id, created_at, updated_at)
VALUES 
(100.0, 4500.00, 10.0, NOW(), 1, 1, NOW(), NOW()),
(50.0, 10000.00, 5.0, NOW(), 2, 1, NOW(), NOW());
-- Guardar los IDs (ej: 1, 2)
```

## Test Cases

### Test 1: Crear Factura Exitosa ✅

**Descripción**: Crear una factura con dos items válidos

**Endpoint**: `POST http://localhost:8080/api/v1/invoices`

**Headers**:
```
Content-Type: application/json
Authorization: [Tu token si está configurado]
```

**Body**:
```json
{
  "customerId": 1,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 10.0
    },
    {
      "inventoryId": 2,
      "quantity": 5.0
    }
  ]
}
```

**Respuesta Esperada** (201 Created):
```json
{
  "success": true,
  "message": "Created",
  "data": {
    "id": 1,
    "customerId": 1,
    "totalAmount": 95000.0,
    "date": "2026-01-13T16:20:00",
    "status": "COMPLETED",
    "items": [
      {
        "id": 1,
        "inventoryId": 1,
        "productName": "Tomates",
        "quantity": 10.0,
        "unitPrice": 4500.0,
        "subtotal": 45000.0
      },
      {
        "id": 2,
        "inventoryId": 2,
        "productName": "Lechuga",
        "quantity": 5.0,
        "unitPrice": 10000.0,
        "subtotal": 50000.0
      }
    ]
  }
}
```

**Verificación en BD**:
```sql
-- Factura creada
SELECT * FROM invoices WHERE id = 1;

-- Items creados
SELECT * FROM invoice_items WHERE invoice_id = 1;

-- Venta creada
SELECT * FROM sales WHERE invoice_id = 1;

-- Stock actualizado
SELECT current_stock FROM inventory WHERE id IN (1, 2);
-- Tomates: 90.0 (100 - 10)
-- Lechuga: 45.0 (50 - 5)
```

---

### Test 2: Stock Insuficiente ❌

**Descripción**: Intentar crear factura con cantidad mayor al stock disponible

**Endpoint**: `POST http://localhost:8080/api/v1/invoices`

**Body**:
```json
{
  "customerId": 1,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 200.0
    }
  ]
}
```

**Respuesta Esperada** (400 Bad Request):
```json
{
  "status": "BAD_REQUEST",
  "message": "Insufficient stock for product: Tomates. Available: 90, Requested: 200",
  "errors": []
}
```

**Verificación**: 
- ❌ No se crea factura
- ❌ No se crea sale
- ❌ Stock NO cambia

---

### Test 3: InventoryId No Existe ❌

**Descripción**: Usar un inventoryId que no existe

**Endpoint**: `POST http://localhost:8080/api/v1/invoices`

**Body**:
```json
{
  "customerId": 1,
  "items": [
    {
      "inventoryId": 9999,
      "quantity": 5.0
    }
  ]
}
```

**Respuesta Esperada** (404 Not Found):
```json
{
  "status": "NOT_FOUND",
  "message": "Inventory not found with id: 9999",
  "errors": []
}
```

---

### Test 4: Sin Items en Factura ❌

**Descripción**: Crear factura sin items

**Endpoint**: `POST http://localhost:8080/api/v1/invoices`

**Body**:
```json
{
  "customerId": 1,
  "items": []
}
```

**Respuesta Esperada** (400 Bad Request):
```json
{
  "status": "BAD_REQUEST",
  "message": "Invoice must contain at least one item",
  "errors": []
}
```

---

### Test 5: Customer No Existe ❌

**Descripción**: Usar customerId que no existe

**Endpoint**: `POST http://localhost:8080/api/v1/invoices`

**Body**:
```json
{
  "customerId": 9999,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 5.0
    }
  ]
}
```

**Respuesta Esperada**: Dependerá de la configuración del mapper
(Probablemente Customer null en la factura)

---

### Test 6: Obtener Factura Creada ✅

**Descripción**: Obtener los detalles de una factura creada

**Endpoint**: `GET http://localhost:8080/api/v1/invoices/1`

**Respuesta Esperada** (200 OK):
```json
{
  "success": true,
  "message": "OK",
  "data": {
    "id": 1,
    "customerId": 1,
    "totalAmount": 95000.0,
    "date": "2026-01-13T16:20:00",
    "status": "COMPLETED",
    "items": [
      {
        "id": 1,
        "inventoryId": 1,
        "productName": "Tomates",
        "quantity": 10.0,
        "unitPrice": 4500.0,
        "subtotal": 45000.0
      },
      {
        "id": 2,
        "inventoryId": 2,
        "productName": "Lechuga",
        "quantity": 5.0,
        "unitPrice": 10000.0,
        "subtotal": 50000.0
      }
    ]
  }
}
```

---

### Test 7: Listar Facturas por Cliente ✅

**Descripción**: Obtener todas las facturas de un cliente

**Endpoint**: `GET http://localhost:8080/api/v1/invoices/customer/1`

**Respuesta Esperada** (200 OK):
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "id": 1,
      "customerId": 1,
      "totalAmount": 95000.0,
      "date": "2026-01-13T16:20:00",
      "status": "COMPLETED",
      "items": [...]
    }
  ]
}
```

---

## Script de Testing con cURL

```bash
#!/bin/bash

# Variables
BASE_URL="http://localhost:8080/api/v1"
CUSTOMER_ID=1
INVENTORY_1=1
INVENTORY_2=2

echo "=== Test 1: Crear Factura Exitosa ==="
curl -X POST $BASE_URL/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": '$CUSTOMER_ID',
    "items": [
      {"inventoryId": '$INVENTORY_1', "quantity": 10},
      {"inventoryId": '$INVENTORY_2', "quantity": 5}
    ]
  }' | jq .

echo -e "\n=== Test 2: Stock Insuficiente ==="
curl -X POST $BASE_URL/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": '$CUSTOMER_ID',
    "items": [
      {"inventoryId": '$INVENTORY_1', "quantity": 1000}
    ]
  }' | jq .

echo -e "\n=== Test 3: Obtener Factura ==="
curl -X GET $BASE_URL/invoices/1 | jq .

echo -e "\n=== Test 4: Listar por Cliente ==="
curl -X GET $BASE_URL/invoices/customer/$CUSTOMER_ID | jq .
```

## Checklist de Validación

- [ ] Factura se crea con totalAmount correcto
- [ ] Factura tiene status "COMPLETED"
- [ ] Factura tiene date con timestamp actual
- [ ] Items incluyen productName
- [ ] Items tienen subtotals calculados correctamente
- [ ] Stock se reduce en cada Inventory
- [ ] Sale se crea automáticamente
- [ ] Sale tiene profit calculado (salePrice - totalCost)
- [ ] Transacción se revierte si hay error
- [ ] Mensajes de error son claros y útiles


