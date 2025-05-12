IF ((SELECT COUNT(*) FROM ::fn_listextendedproperty('MS_Description',
'SCHEMA', N'dbo',
'TABLE', N'flow_task',
'COLUMN', N'flow_status')) > 0)
  EXEC sp_updateextendedproperty
'MS_Description', N'流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
'SCHEMA', N'dbo',
'TABLE', N'flow_task',
'COLUMN', N'flow_status'
ELSE
  EXEC sp_addextendedproperty
'MS_Description', N'流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
'SCHEMA', N'dbo',
'TABLE', N'flow_task',
'COLUMN', N'flow_status'
GO

IF ((SELECT COUNT(*) FROM ::fn_listextendedproperty('MS_Description',
'SCHEMA', N'dbo',
'TABLE', N'flow_instance',
'COLUMN', N'flow_status')) > 0)
  EXEC sp_updateextendedproperty
'MS_Description', N'流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
'SCHEMA', N'dbo',
'TABLE', N'flow_instance',
'COLUMN', N'flow_status'
ELSE
  EXEC sp_addextendedproperty
'MS_Description', N'流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
'SCHEMA', N'dbo',
'TABLE', N'flow_instance',
'COLUMN', N'flow_status'
GO

IF ((SELECT COUNT(*) FROM ::fn_listextendedproperty('MS_Description',
'SCHEMA', N'dbo',
'TABLE', N'flow_his_task',
'COLUMN', N'flow_status')) > 0)
  EXEC sp_updateextendedproperty
'MS_Description', N'流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
'SCHEMA', N'dbo',
'TABLE', N'flow_his_task',
'COLUMN', N'flow_status'
ELSE
  EXEC sp_addextendedproperty
'MS_Description', N'流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
'SCHEMA', N'dbo',
'TABLE', N'flow_his_task',
'COLUMN', N'flow_status'
GO

-- sys_social 表修改列
ALTER TABLE sys_social ALTER COLUMN access_token VARCHAR(2000) NOT NULL
GO
ALTER TABLE sys_social ALTER COLUMN refresh_token VARCHAR(2000) NULL
GO
