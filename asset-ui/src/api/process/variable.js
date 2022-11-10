import request from '@/utils/request'

// 查询列表
export function listData(query) {
  return request({
    url: '/asset/task/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getData(Id) {
  return request({
    url: '/asset/task/' + Id,
    method: 'get'
  })
}

// 新增
export function addData(data) {
  return request({
    url: '/asset/task',
    method: 'post',
    data: data
  })
}

// 修改
export function updateData(data) {
  return request({
    url: '/asset/task',
    method: 'put',
    data: data
  })
}

// 删除
export function delData(Id) {
  return request({
    url: '/asset/task/' + Id,
    method: 'delete'
  })
}

// 导出
export function exportData(query) {
  return request({
    url: '/asset/task/export',
    method: 'get',
    params: query
  })
}
