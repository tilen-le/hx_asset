import request from '@/utils/request'

// 查询流程总列表
export function listProcess(query) {
  return request({
    url: '/asset/process/list',
    method: 'get',
    params: query
  })
}

// 查询流程总详细
export function getProcess(id) {
  return request({
    url: '/asset/process/' + id,
    method: 'get'
  })
}

// 新增流程总
export function addProcess(data) {
  return request({
    url: '/asset/process',
    method: 'post',
    data: data
  })
}

// 修改流程总
export function updateProcess(data) {
  return request({
    url: '/asset/process',
    method: 'put',
    data: data
  })
}

// 删除流程总
export function delProcess(id) {
  return request({
    url: '/asset/process/' + id,
    method: 'delete'
  })
}

// 导出流程总
export function exportProcess(query) {
  return request({
    url: '/asset/process/export',
    method: 'get',
    params: query
  })
}