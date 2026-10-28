-- ----------------------------
-- 专门学校学员管理系统 业务表
-- 说明：班级 / 学生档案 / 积分流水 / 调班记录
-- ----------------------------

-- ----------------------------
-- 1、班级信息表
-- ----------------------------
drop table if exists stu_class;
create table stu_class (
  class_id        bigint(20)      not null auto_increment    comment '班级ID',
  class_name      varchar(50)     not null                   comment '班级名称',
  class_code      varchar(30)     default ''                 comment '班级编号',
  head_teacher    varchar(50)     default ''                 comment '负责老师',
  capacity        int(11)         default 0                  comment '班级容量',
  order_num       int(4)          default 0                  comment '显示顺序',
  status          char(1)         default '0'                comment '状态（0正常 1停用）',
  remark          varchar(500)    default null               comment '备注',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  primary key (class_id)
) engine=innodb auto_increment=1 comment = '班级信息表';

-- ----------------------------
-- 2、学生档案表
-- ----------------------------
drop table if exists stu_student;
create table stu_student (
  student_id      bigint(20)      not null auto_increment    comment '学生ID',
  student_no      varchar(30)     not null                   comment '学生编号',
  name            varchar(50)     not null                   comment '姓名',
  gender          char(1)         default '0'                comment '性别（0男 1女）',
  birth_date      date            default null               comment '出生日期',
  enroll_date     date            default null               comment '入学日期',
  class_id        bigint(20)      default null               comment '所在班级',
  total_score     int(11)         default 0                  comment '当前积分',
  parent_name     varchar(50)     default ''                 comment '家长联系人',
  parent_relation varchar(20)     default ''                 comment '与学生关系',
  parent_phone    varchar(20)     default ''                 comment '家长手机号',
  status          char(1)         default '0'                comment '状态（0在校 1离校）',
  remark          varchar(500)    default null               comment '备注',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  primary key (student_id),
  key idx_student_class (class_id)
) engine=innodb auto_increment=1 comment = '学生档案表';

-- ----------------------------
-- 3、积分流水表
-- ----------------------------
drop table if exists stu_score_log;
create table stu_score_log (
  log_id          bigint(20)      not null auto_increment    comment '流水ID',
  student_id      bigint(20)      not null                   comment '学生ID',
  change_score    int(11)         not null                   comment '变动分值（正数加分 负数扣分）',
  score_after     int(11)         default 0                  comment '变动后积分',
  reason          varchar(100)    default ''                 comment '加/减分项目',
  remark          varchar(500)    default null               comment '备注',
  create_by       varchar(64)     default ''                 comment '操作老师',
  create_time     datetime                                   comment '操作时间',
  primary key (log_id),
  key idx_score_student (student_id)
) engine=innodb auto_increment=1 comment = '积分流水表';

-- ----------------------------
-- 4、调班记录表
-- ----------------------------
drop table if exists stu_transfer_log;
create table stu_transfer_log (
  transfer_id     bigint(20)      not null auto_increment    comment '调班记录ID',
  student_id      bigint(20)      not null                   comment '学生ID',
  from_class_id   bigint(20)      default null               comment '原班级',
  to_class_id     bigint(20)      default null               comment '新班级',
  reason          varchar(255)    default ''                 comment '调班原因',
  create_by       varchar(64)     default ''                 comment '操作人',
  create_time     datetime                                   comment '调班时间',
  primary key (transfer_id),
  key idx_transfer_student (student_id)
) engine=innodb auto_increment=1 comment = '调班记录表';

-- ----------------------------
-- 5、初始演示数据
-- ----------------------------
insert into stu_class (class_name, class_code, head_teacher, capacity, order_num, status, remark, create_by, create_time)
values ('向阳一班', 'XY-01', '李老师', 30, 1, '0', '基础养成班', 'admin', sysdate()),
       ('向阳二班', 'XY-02', '王老师', 30, 2, '0', '行为强化班', 'admin', sysdate()),
       ('启航三班', 'QH-03', '张老师', 25, 3, '0', '过渡适应班', 'admin', sysdate()),
       ('明德四班', 'MD-04', '赵老师', 25, 4, '0', '综合提升班', 'admin', sysdate());

insert into stu_student (student_no, name, gender, birth_date, enroll_date, class_id, total_score, parent_name, parent_relation, parent_phone, status, remark, create_by, create_time)
values ('XS2026001', '陈明', '0', '2010-03-12', '2026-02-20', 1, 85, '陈建国', '父亲', '13807710001', '0', '入校表现良好', 'admin', sysdate()),
       ('XS2026002', '刘洋', '0', '2009-11-05', '2026-02-20', 1, 62, '刘淑芬', '母亲', '13807710002', '0', '', 'admin', sysdate()),
       ('XS2026003', '黄小婷', '1', '2010-07-21', '2026-03-01', 2, 91, '黄志强', '父亲', '13807710003', '0', '乐于助人', 'admin', sysdate()),
       ('XS2026004', '韦强', '0', '2009-09-18', '2026-03-01', 2, 48, '韦丽娟', '母亲', '13807710004', '0', '需重点关注', 'admin', sysdate()),
       ('XS2026005', '周小雨', '1', '2010-01-30', '2026-03-15', 3, 76, '周海涛', '父亲', '13807710005', '0', '', 'admin', sysdate()),
       ('XS2026006', '覃浩', '0', '2010-05-09', '2026-03-15', 3, 55, '覃玉兰', '母亲', '13807710006', '0', '', 'admin', sysdate()),
       ('XS2026007', '吴倩', '1', '2009-12-25', '2026-04-01', 4, 88, '吴文昌', '父亲', '13807710007', '0', '', 'admin', sysdate()),
       ('XS2026008', '梁俊', '0', '2010-08-14', '2026-04-01', 4, 70, '梁美华', '母亲', '13807710008', '0', '', 'admin', sysdate());

insert into stu_score_log (student_id, change_score, score_after, reason, remark, create_by, create_time)
values (1, 5, 85, '课堂表现优秀', '主动帮助同学', 'admin', date_sub(sysdate(), interval 3 day)),
       (2, -3, 62, '违反作息纪律', '', 'admin', date_sub(sysdate(), interval 2 day)),
       (3, 6, 91, '积极参加活动', '', 'admin', date_sub(sysdate(), interval 2 day)),
       (4, -5, 48, '与他人发生争执', '已谈话教育', 'admin', date_sub(sysdate(), interval 1 day)),
       (5, 4, 76, '内务整理规范', '', 'admin', date_sub(sysdate(), interval 1 day));

insert into stu_transfer_log (student_id, from_class_id, to_class_id, reason, create_by, create_time)
values (3, 1, 2, '表现进步，调整至行为强化班', 'admin', date_sub(sysdate(), interval 5 day)),
       (7, 3, 4, '综合表现优异，调整至综合提升班', 'admin', date_sub(sysdate(), interval 4 day));
