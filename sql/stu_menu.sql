-- ----------------------------
-- 学员管理系统 菜单与权限
-- 说明：新增"学员管理"目录及各页面菜单、按钮权限；隐藏若依自带用不到的菜单
-- ----------------------------

-- 清理旧数据（可重复执行）
delete from sys_menu where menu_id between 2000 and 2199;

-- 一级目录：学员管理
insert into sys_menu values(2000, '学员管理', 0, 0, 'stu', null, '', '', 1, 0, 'M', '0', '0', '', 'user', 'admin', sysdate(), '', null, '学员管理目录');

-- 二级菜单
insert into sys_menu values(2001, '学生档案', 2000, 1, 'student', 'stu/student/index', '', 'StuStudent', 1, 0, 'C', '0', '0', 'stu:student:list', 'user', 'admin', sysdate(), '', null, '学生档案菜单');
insert into sys_menu values(2002, '班级管理', 2000, 2, 'class',   'stu/class/index',   '', 'StuClass',   1, 0, 'C', '0', '0', 'stu:class:list',   'peoples', 'admin', sysdate(), '', null, '班级管理菜单');
insert into sys_menu values(2003, '积分流水', 2000, 3, 'score',   'stu/score/index',   '', 'StuScore',   1, 0, 'C', '0', '0', 'stu:score:list',   'star', 'admin', sysdate(), '', null, '积分流水菜单');
insert into sys_menu values(2004, '分班调班', 2000, 4, 'transfer','stu/transfer/index','', 'StuTransfer',1, 0, 'C', '0', '0', 'stu:transfer:list','swap', 'admin', sysdate(), '', null, '分班调班菜单');

-- 学生档案 按钮权限
insert into sys_menu values(2100, '学生查询', 2001, 1, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:query',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2101, '学生新增', 2001, 2, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:add',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2102, '学生修改', 2001, 3, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:edit',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2103, '学生删除', 2001, 4, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2104, '学生导出', 2001, 5, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2105, '学生导入', 2001, 6, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:import', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2106, '积分增减', 2001, 7, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:score',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2107, '学生调班', 2001, 8, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:student:transfer','#', 'admin', sysdate(), '', null, '');

-- 班级管理 按钮权限
insert into sys_menu values(2110, '班级查询', 2002, 1, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:class:query',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2111, '班级新增', 2002, 2, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:class:add',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2112, '班级修改', 2002, 3, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:class:edit',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2113, '班级删除', 2002, 4, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:class:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2114, '班级导出', 2002, 5, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:class:export', '#', 'admin', sysdate(), '', null, '');

-- 积分流水 按钮权限
insert into sys_menu values(2120, '流水删除', 2003, 1, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:score:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2121, '流水导出', 2003, 2, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:score:export', '#', 'admin', sysdate(), '', null, '');

-- 分班调班 按钮权限
insert into sys_menu values(2130, '记录删除', 2004, 1, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:transfer:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2131, '记录导出', 2004, 2, '', null, '', '', 1, 0, 'F', '0', '0', 'stu:transfer:export', '#', 'admin', sysdate(), '', null, '');

-- 隐藏若依自带、学员管理场景用不到的菜单
update sys_menu set visible = '1' where menu_id in (2, 3, 4);
update sys_menu set visible = '1' where menu_id in (102, 103, 104, 105, 106, 107, 108);
