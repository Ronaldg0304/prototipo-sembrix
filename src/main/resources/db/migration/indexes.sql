-- ============================================================================
-- SEMBRIX DATABASE INDEXING SCRIPT
-- Database Optimization for High-Traffic Queries
-- Generated: 2026-01-22
-- Database: PostgreSQL
-- ============================================================================

-- =============================================================================
-- 1. HARVEST TABLE INDEXES
-- Purpose: Optimize queries filtering by profile_producer_id, product_id, status
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_harvest_profile_producer_id
  ON harvest(profile_producer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_harvest_product_id
  ON harvest(product_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_harvest_status
  ON harvest(status);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_harvest_start_date
  ON harvest(start_date DESC);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_harvest_profile_status
  ON harvest(profile_producer_id, status);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_harvest_created_at
  ON harvest(created_at DESC);

-- =============================================================================
-- 2. PRODUCTION_EXPENSE_ITEM TABLE INDEXES
-- Purpose: Optimize queries filtering by harvest_id and expense_date
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_production_expense_item_harvest_id
  ON production_expense_item(harvest_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_production_expense_item_expense_date
  ON production_expense_item(expense_date DESC);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_production_expense_item_category
  ON production_expense_item(category);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_production_expense_item_harvest_date
  ON production_expense_item(harvest_id, expense_date DESC);

-- =============================================================================
-- 3. INVENTORY TABLE INDEXES
-- Purpose: Optimize queries filtering by product_id and profile_producer_id
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_product_id
  ON inventory(product_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_profile_producer_id
  ON inventory(profile_producer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_product_producer
  ON inventory(product_id, profile_producer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_profile_producer
  ON inventory(profile_producer_id, product_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_current_stock
  ON inventory(current_stock)
  WHERE current_stock < alert_threshold;

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_updated_at
  ON inventory(last_updated DESC);

-- =============================================================================
-- 4. SALE TABLE INDEXES
-- Purpose: Optimize queries filtering by sale_date and customer_id
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sale_sale_date
  ON sale(sale_date DESC);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sale_customer_id
  ON sale(customer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sale_profile_producer_id
  ON sale(profile_producer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sale_invoice_id
  ON sale(invoice_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sale_date_producer
  ON sale(sale_date DESC, profile_producer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sale_created_at
  ON sale(created_at DESC);

-- =============================================================================
-- 5. INVOICE TABLE INDEXES
-- Purpose: Optimize queries on invoice listing and filtering
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_customer_id
  ON invoice(customer_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_date
  ON invoice(date DESC);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_status
  ON invoice(status);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_customer_date
  ON invoice(customer_id, date DESC);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_created_at
  ON invoice(created_at DESC);

-- =============================================================================
-- 6. INVOICE_ITEM TABLE INDEXES
-- Purpose: Optimize queries filtering by invoice_id and inventory_id
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_item_invoice_id
  ON invoice_item(invoice_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_item_inventory_id
  ON invoice_item(inventory_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_invoice_item_invoice_inventory
  ON invoice_item(invoice_id, inventory_id);

-- =============================================================================
-- 7. PRODUCT TABLE INDEXES
-- Purpose: Optimize product lookups
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_product_name
  ON product(name);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_product_category
  ON product(category);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_product_created_at
  ON product(created_at DESC);

-- =============================================================================
-- 8. CUSTOMER TABLE INDEXES
-- Purpose: Optimize customer queries
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_customer_email
  ON customer(email);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_customer_phone
  ON customer(phone);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_customer_created_at
  ON customer(created_at DESC);

-- =============================================================================
-- 9. USER_ENTITY TABLE INDEXES
-- Purpose: Optimize user lookups and authentication
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_email
  ON user_entity(email);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_role
  ON user_entity(role);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_status
  ON user_entity(status);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_created_at
  ON user_entity(created_at DESC);

-- =============================================================================
-- 10. MARKET_PRICE TABLE INDEXES
-- Purpose: Optimize market intelligence queries
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_price_product_id
  ON market_price(product_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_price_region
  ON market_price(region);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_price_product_region
  ON market_price(product_id, region);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_price_created_at
  ON market_price(created_at DESC);

-- =============================================================================
-- 11. MARKET_DATA TABLE INDEXES
-- Purpose: Optimize external market data queries
-- =============================================================================

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_data_capture_date
  ON market_data(capture_date DESC);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_data_external_product_id
  ON market_data(external_product_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_data_source_api
  ON market_data(source_api_name);

-- =============================================================================
-- 12. VERIFY INDEX CREATION
-- Query to verify all indexes were created successfully
-- =============================================================================

-- Run this query to verify index creation:
-- SELECT
--   schemaname,
--   tablename,
--   indexname,
--   indexdef
-- FROM pg_indexes
-- WHERE schemaname = 'public'
-- ORDER BY tablename, indexname;

-- =============================================================================
-- MAINTENANCE RECOMMENDATIONS
-- =============================================================================
-- 1. Run ANALYZE on tables after index creation to update statistics
-- 2. Monitor index usage with:
--    SELECT * FROM pg_stat_user_indexes WHERE idx_scan = 0;
-- 3. Rebuild fragmented indexes periodically:
--    REINDEX INDEX index_name;
-- 4. Consider partitioning large tables (harvest, sale) by date for better performance
-- =============================================================================
