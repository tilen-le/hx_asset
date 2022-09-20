import request from '@/utils/request'

// 查询资产改造流程列表
export function listTransform(query) {
  return request({
    url: '/asset/transform/list',
    method: 'get',
    params: query
  })
}

// 查询资产改造流程详细
export function getTransform(id) {
  return request({
    url: '/asset/transform/' + id,
    method: 'get'
  })
}

// 新增资产改造流程
export function addTransform(data) {
  return request({
    url: '/asset/transform',
    method: 'post',
    data: data
  })
}

// 修改资产改造流程
export function updateTransform(data) {
  return request({
    url: '/asset/transform',
    method: 'put',
    data: data
  })
}

// 删除资产改造流程
export function delTransform(id) {
  return request({
    url: '/asset/transform/' + id,
    method: 'delete'
  })
}

// 导出资产改造流程
export function exportTransform(query) {
  return request({
    url: '/asset/transform/export',
    method: 'get',
    params: query
  })
}