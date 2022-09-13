import request from '@/utils/request'

// 查询资产表列表
export function listAsset(query) {
  return request({
    url: '/asset/list',
    method: 'get',
    params: query
  })
}

// 查询资产表详细
export function getAsset(assetId) {
  return request({
    url: '/asset/getInfo?assetId=' + assetId,
    method: 'get'
  })
}

// 新增资产表
export function addAsset(data) {
  return request({
    url: '/asset',
    method: 'post',
    data: data
  })
}

// 修改资产表
export function updateAsset(data) {
  return request({
    url: '/asset/update',
    method: 'put',
    data: data
  })
}

// 删除资产表
export function delAsset(assetId) {
  return request({
    url: '/asset/' + assetId,
    method: 'delete'
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/asset/importTemplate',
    method: 'get'
  })
}

// 导出资产表
export function exportAsset(query) {
  return request({
    url: '/asset/export',
    method: 'get',
    params: query
  })
}
