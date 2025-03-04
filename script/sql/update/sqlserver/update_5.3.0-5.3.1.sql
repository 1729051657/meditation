ALTER TABLE flow_node DROP COLUMN skip_any_node;
ALTER TABLE flow_node ADD ext nvarchar(500) NULL;

EXEC sp_addextendedproperty
'MS_Description', N'扩展属性',
'SCHEMA', N'dbo',
'TABLE', N'flow_node',
'COLUMN', N'ext'
GO
