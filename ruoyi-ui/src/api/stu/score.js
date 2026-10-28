import request from '@/utils/request'

// 查询积分流水列表
export function listScore(query) {
  return request({
    url: '/stu/score/list',
    method: 'get',
    params: query
  })
}

// 删除积分流水
export function delScore(logIds) {
  return request({
    url: '/stu/score/' + logIds,
    method: 'delete'
  })
}
