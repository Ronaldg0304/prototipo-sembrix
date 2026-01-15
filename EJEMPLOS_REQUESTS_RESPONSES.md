# Ejemplos de Requests y Responses - POST /api/v1/invoices

## Base URL
```
http://localhost:8080/api/v1/invoices
```

## Headers Requeridos
```
Content-Type: application/json
Authorization: [Tu token si está configurado]
```

---

## Ejemplo 1: Éxito - Factura Simple

### Request
```json
{
  "customerId": 1,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 5.0
    }
  ]
}
```

### Response (201 Created)
```json
{
  "success": true,
  "message": "Created",
  "data": {
    "id": 1,
    "customerId": 1,
    "totalAmount": 22500.00,
    "date": "2026-01-13T16:30:45",
    "status": "COMPLETED",
    "items": [
      {
        "id": 1,
        "inventoryId": 1,
        "productName": "Tomates",
        "quantity": 5.0,
        "unitPrice": 4500.00,
        "subtotal": 22500.00
      }
    ]
  }
}
```

### Verificación en BD
```sql
SELECT * FROM invoices WHERE id = 1;
-- id: 1
-- customer_id: 1
-- total_amount: 22500.00
-- date: 2026-01-13 16:30:45
-- status: COMPLETED

SELECT * FROM invoice_items WHERE invoice_id = 1;
-- id: 1, invoice_id: 1, inventory_id: 1, quantity: 5, unit_price: 4500, subtotal: 22500

SELECT * FROM sales WHERE invoice_id = 1;
-- id: 1, invoice_id: 1, sale_price: 22500, profit: 15750 (estimado 70% de ganancia)

SELECT current_stock FROM inventory WHERE id = 1;
-- current_stock: 95 (fue 100, se restó 5)
```

---

## Ejemplo 2: Éxito - Múltiples Items

### Request
```json
{
  "customerId": 2,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 10.0
    },
    {
      "inventoryId": 2,
      "quantity": 3.0
    },
    {
      "inventoryId": 3,
      "quantity": 15.0
    }
  ]
}
```

### Response (201 Created)
```json
{
  "success": true,
  "message": "Created",
  "data": {
    "id": 2,
    "customerId": 2,
    "totalAmount": 195000.00,
    "date": "2026-01-13T16:35:20",
    "status": "COMPLETED",
    "items": [
      {
        "id": 2,
        "inventoryId": 1,
        "productName": "Tomates",
        "quantity": 10.0,
        "unitPrice": 4500.00,
        "subtotal": 45000.00
      },
      {
        "id": 3,
        "inventoryId": 2,
        "productName": "Lechuga",
        "quantity": 3.0,
        "unitPrice": 10000.00,
        "subtotal": 30000.00
      },
      {
        "id": 4,
        "inventoryId": 3,
        "productName": "Cebolla",
        "quantity": 15.0,
        "unitPrice": 6800.00,
        "subtotal": 102000.00
      }
    ]
  }
}
```

### Cálculo Manual
```
Item 1: 4500 × 10 = 45,000
Item 2: 10,000 × 3 = 30,000
Item 3: 6,800 × 15 = 102,000
────────────────────
Total:               195,000 ✓
```

---

## Ejemplo 3: Error - Stock Insuficiente

### Request
```json
{
  "customerId": 1,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 500.0
    }
  ]
}
```

**Supuesto**: Inventory 1 tiene current_stock = 95

### Response (400 Bad Request)
```json
{
  "status": "BAD_REQUEST",
  "message": "Insufficient stock for product: Tomates. Available: 95, Requested: 500",
  "errors": []
}
```

### Verificación en BD
```sql
-- NO se crea factura
SELECT COUNT(*) FROM invoices WHERE date = NOW();
-- 0 (no se agregó nada)

-- NO se crea sale
SELECT COUNT(*) FROM sales WHERE date = NOW();
-- 0

-- Stock NO cambia
SELECT current_stock FROM inventory WHERE id = 1;
-- current_stock: 95 (sin cambios)
```

---

## Ejemplo 4: Error - InventoryId No Existe

### Request
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

### Response (404 Not Found)
```json
{
  "status": "NOT_FOUND",
  "message": "Inventory not found with id: 9999",
  "errors": []
}
```

---

## Ejemplo 5: Error - Sin Items

### Request
```json
{
  "customerId": 1,
  "items": []
}
```

### Response (400 Bad Request)
```json
{
  "status": "BAD_REQUEST",
  "message": "Invoice must contain at least one item",
  "errors": []
}
```

---

## Ejemplo 6: Error - Customer Sin Producer

**Nota**: Este error ocurre si el Customer no tiene profileProducerId

### Request
```json
{
  "customerId": 99,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 5.0
    }
  ]
}
```

### Response (400 Bad Request)
```json
{
  "status": "BAD_REQUEST",
  "message": "Customer must be associated with a producer",
  "errors": []
}
```

---

## Ejemplo 7: GET - Obtener Factura Creada

### Request
```
GET http://localhost:8080/api/v1/invoices/1
```

### Response (200 OK)
```json
{
  "success": true,
  "message": "OK",
  "data": {
    "id": 1,
    "customerId": 1,
    "totalAmount": 22500.00,
    "date": "2026-01-13T16:30:45",
    "status": "COMPLETED",
    "items": [
      {
        "id": 1,
        "inventoryId": 1,
        "productName": "Tomates",
        "quantity": 5.0,
        "unitPrice": 4500.00,
        "subtotal": 22500.00
      }
    ]
  }
}
```

---

## Ejemplo 8: GET - Listar por Customer

### Request
```
GET http://localhost:8080/api/v1/invoices/customer/1
```

### Response (200 OK)
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "id": 1,
      "customerId": 1,
      "totalAmount": 22500.00,
      "date": "2026-01-13T16:30:45",
      "status": "COMPLETED",
      "items": [
        {
          "id": 1,
          "inventoryId": 1,
          "productName": "Tomates",
          "quantity": 5.0,
          "unitPrice": 4500.00,
          "subtotal": 22500.00
        }
      ]
    },
    {
      "id": 3,
      "customerId": 1,
      "totalAmount": 50000.00,
      "date": "2026-01-13T17:00:00",
      "status": "COMPLETED",
      "items": [
        {
          "id": 5,
          "inventoryId": 2,
          "productName": "Lechuga",
          "quantity": 5.0,
          "unitPrice": 10000.00,
          "subtotal": 50000.00
        }
      ]
    }
  ]
}
```

---

## Ejemplo 9: GET - Listar Todas las Facturas

### Request
```
GET http://localhost:8080/api/v1/invoices
```

### Response (200 OK)
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "id": 1,
      "customerId": 1,
      "totalAmount": 22500.00,
      "date": "2026-01-13T16:30:45",
      "status": "COMPLETED",
      "items": [...]
    },
    {
      "id": 2,
      "customerId": 2,
      "totalAmount": 195000.00,
      "date": "2026-01-13T16:35:20",
      "status": "COMPLETED",
      "items": [...]
    }
  ]
}
```

---

## Ejemplo 10: DELETE - Eliminar Factura

### Request
```
DELETE http://localhost:8080/api/v1/invoices/1
```

### Response (200 OK)
```json
{
  "success": true,
  "message": "OK",
  "data": null
}
```

**Nota**: Esto eliminará la factura pero NO revertirá los cambios de stock.
Considerar usar lógica de "anular" en lugar de eliminar físicamente.

---

## Payload Incompleto - Error

### Request (Falta inventoryId)
```json
{
  "customerId": 1,
  "items": [
    {
      "quantity": 5.0
    }
  ]
}
```

### Response (400 Bad Request)
```json
{
  "status": "BAD_REQUEST",
  "message": "Inventory not found with id: null",
  "errors": []
}
```

---

## Null Pointer Examples

### Request (customerId es null)
```json
{
  "customerId": null,
  "items": [
    {
      "inventoryId": 1,
      "quantity": 5.0
    }
  ]
}
```

**Resultado**: Dependerá de la validación del mapper
(Probablemente Customer será null pero la factura se crea)

---

## cURL Commands

```bash
#!/bin/bash

# Test 1: Éxito
echo "=== Test 1: Éxito ==="
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [{"inventoryId": 1, "quantity": 5}]
  }' | jq .

# Test 2: Stock insuficiente
echo -e "\n=== Test 2: Stock Insuficiente ==="
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [{"inventoryId": 1, "quantity": 500}]
  }' | jq .

# Test 3: GET por ID
echo -e "\n=== Test 3: GET por ID ==="
curl -X GET http://localhost:8080/api/v1/invoices/1 | jq .

# Test 4: GET por Customer
echo -e "\n=== Test 4: GET por Customer ==="
curl -X GET http://localhost:8080/api/v1/invoices/customer/1 | jq .

# Test 5: GET todas
echo -e "\n=== Test 5: GET Todas ==="
curl -X GET http://localhost:8080/api/v1/invoices | jq .
```

---

## Postman Collection Template

```json
{
  "info": {
    "name": "Sembrix - Invoices API",
    "version": "1.0"
  },
  "item": [
    {
      "name": "Create Invoice",
      "request": {
        "method": "POST",
        "url": {
          "raw": "http://localhost:8080/api/v1/invoices",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "invoices"]
        },
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"customerId\": 1, \"items\": [{\"inventoryId\": 1, \"quantity\": 5}]}"
        }
      }
    },
    {
      "name": "Get Invoice",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/invoices/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "invoices", "1"]
        }
      }
    },
    {
      "name": "List by Customer",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/invoices/customer/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "invoices", "customer", "1"]
        }
      }
    },
    {
      "name": "List All",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/invoices",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "invoices"]
        }
      }
    },
    {
      "name": "Delete Invoice",
      "request": {
        "method": "DELETE",
        "url": {
          "raw": "http://localhost:8080/api/v1/invoices/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "invoices", "1"]
        }
      }
    }
  ]
}
```


