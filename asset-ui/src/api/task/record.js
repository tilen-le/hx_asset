import request from '@/utils/request'

export function listRecord(query) {
  return request({
    url: '/asset/counting/list',
    method: 'get',
    params: query
  })
}

export function countRecord(query) {
  return request({
    url: '/asset/counting/countingStatusCount',
    method: 'get',
    params: query
  })
}
