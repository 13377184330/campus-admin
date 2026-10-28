import request from '@/utils/request'

// 首页概览统计
export function getOverview() {
  return request({
    url: '/stu/dashboard/overview',
    method: 'get'
  })
}
