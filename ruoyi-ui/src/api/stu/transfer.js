import request from '@/utils/request'

// 查询调班记录列表
export function listTransfer(query) {
  return request({
    url: '/stu/transfer/list',
    method: 'get',
    params: query
  })
}

// 删除调班记录
export function delTransfer(transferIds) {
  return request({
    url: '/stu/transfer/' + transferIds,
    method: 'delete'
  })
}
