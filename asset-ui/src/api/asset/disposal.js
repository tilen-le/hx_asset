import request from '@/utils/request'

// 查询资产处置流程列表
export function listDisposal(query) {
  return request({
    url: '/asset/disposal/list',
    method: 'get',
    params: query
  })
}

// 查询资产处置流程详细
export function getDisposal(id) {
  return request({
    url: '/asset/disposal/' + id,
    method: 'get'
  })
}

// 新增资产处置流程
export function addDisposal(data) {
  return request({
    url: '/asset/disposal',
    method: 'post',
    data: data
  })
}

// 修改资产处置流程
export function updateDisposal(data) {
  return request({
    url: '/asset/disposal',
    method: 'put',
    data: data
  })
}

// 删除资产处置流程
export function delDisposal(id) {
  return request({
    url: '/asset/disposal/' + id,
    method: 'delete'
  })
}

// 导出资产处置流程
export function exportDisposal(query) {
  return request({
    url: '/asset/disposal/export',
    method: 'get',
    params: query
  })
}