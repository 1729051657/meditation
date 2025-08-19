-- 删除付费相关字段的迁移脚本
-- Migration script to remove payment-related fields

-- 删除 mg_series 表的 is_free 字段
ALTER TABLE mg_series DROP COLUMN IF EXISTS is_free;

-- 删除 mg_track 表的 is_free 字段
ALTER TABLE mg_track DROP COLUMN IF EXISTS is_free;