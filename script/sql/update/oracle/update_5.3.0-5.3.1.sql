ALTER TABLE flow_node DROP COLUMN skip_any_node;
ALTER TABLE flow_node ADD (ext VARCHAR2(500));
COMMENT ON COLUMN flow_node.ext IS '扩展属性';
