import request from '@/utils/request'

//获取资产配置人员
export function listAssets(query) {
  return request({
    url: '/asset/list',
    method: 'get',
    params: query
  })
}

//获取待办列表
// export function backloglistAssets(query) {
//   return request({
//     url: '/asset/backloglist',
//     method: 'get',
//     params: query
//   })
// }
export function backloglistAssets(query) {
  return request({
    url: '/mature/backlog/list',
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

export function importTemplate() {
  return request({
    url: '/asset/importTemplate',
    method: 'get'
  })
}

export function getInfo(assetCode) {
  return request({
    url: '/asset/getInfo/' + assetCode,
    method: 'get'
  })
}

//保管记录
export function custodyLogList(query) {
  return request({
    url: '/asset/log/custodyLogList',
    method: 'get',
    params: query
  })
}

//查询工单记录
export function workLogList(query) {
  return request({
    url: '/asset/log/workLogList',
    method: 'get',
    params: query
  })
}

//查询操作记录
export function operationLogList(query) {
  return request({
    url: '/asset/log/operationLogList',
    method: 'get',
    params: query
  })
}

//查询操作日志详情
export function getLogDetail(id) {
  return request({
    url: '/asset/log/' + id,
    method: 'get'
  })
}
