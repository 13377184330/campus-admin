import request from '@/utils/request'

// 查询班级列表
export function listClass(query) {
  return request({
    url: '/stu/class/list',
    method: 'get',
    params: query
  })
}

// 查询全部班级（下拉选择用）
export function optionClass(query) {
  return request({
    url: '/stu/class/optionselect',
    method: 'get',
    params: query
  })
}

// 查询班级详细
export function getClass(classId) {
  return request({
    url: '/stu/class/' + classId,
    method: 'get'
  })
}

// 新增班级
export function addClass(data) {
  return request({
    url: '/stu/class',
    method: 'post',
    data: data
  })
}

// 修改班级
export function updateClass(data) {
  return request({
    url: '/stu/class',
    method: 'put',
    data: data
  })
}

// 删除班级
export function delClass(classId) {
  return request({
    url: '/stu/class/' + classId,
    method: 'delete'
  })
}
