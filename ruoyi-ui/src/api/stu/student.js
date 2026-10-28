import request from '@/utils/request'

// 查询学生档案列表
export function listStudent(query) {
  return request({
    url: '/stu/student/list',
    method: 'get',
    params: query
  })
}

// 查询全部学生（下拉选择用）
export function allStudent(query) {
  return request({
    url: '/stu/student/allList',
    method: 'get',
    params: query
  })
}

// 查询学生详细
export function getStudent(studentId) {
  return request({
    url: '/stu/student/' + studentId,
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/stu/student',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/stu/student',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStudent(studentId) {
  return request({
    url: '/stu/student/' + studentId,
    method: 'delete'
  })
}

// 学生加/减分
export function changeScore(data) {
  return request({
    url: '/stu/student/score',
    method: 'post',
    data: data
  })
}

// 学生调班
export function transferStudent(data) {
  return request({
    url: '/stu/student/transfer',
    method: 'post',
    data: data
  })
}

// 查询某学生的积分流水
export function getStudentScoreLog(studentId) {
  return request({
    url: '/stu/student/scoreLog/' + studentId,
    method: 'get'
  })
}

// 查询某学生的调班记录
export function getStudentTransferLog(studentId) {
  return request({
    url: '/stu/student/transferLog/' + studentId,
    method: 'get'
  })
}
