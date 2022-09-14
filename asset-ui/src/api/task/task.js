import request from '@/utils/request'

// 查询盘点任务列表
export function listTask(query) {
  return request({
    url: '/asset/task/list',
    method: 'get',
    params: query
  })
}

// 查询盘点任务详细
export function getTask(taskId) {
  return request({
    url: '/asset/task/' + taskId,
    method: 'get'
  })
}

// 新增盘点任务
export function addTask(data) {
  return request({
    url: '/asset/task',
    method: 'post',
    data: data
  })
}

// 修改盘点任务
export function updateTask(data) {
  return request({
    url: '/asset/task',
    method: 'put',
    data: data
  })
}

// 删除盘点任务
export function delTask(taskId) {
  return request({
    url: '/asset/task/' + taskId,
    method: 'delete'
  })
}

// 导出盘点任务
export function exportTask(query) {
  return request({
    url: '/asset/task/export',
    method: 'get',
    params: query
  })
}
