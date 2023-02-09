import request from '@/utils/request'

//获取资产配置人员
export function listAssets(query) {
  return request({
    url: '/asset/list',
    method: 'get',
    params: query
  })
}


export function exportData(query) {
  return request({
    url: '/asset/export',
    method: 'get',
    params: query
  })
}
