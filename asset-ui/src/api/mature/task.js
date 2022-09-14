import request from '@/utils/request'

// 查询盘点任务列表
export function listTask(query) {
  return request({
    url: '/mature/task/list',
    method: 'get',
    params: query
  })
}

// 查询盘点任务详细
export function getTask(taskId) {
  return request({
    url: '/mature/task/' + taskId,
    method: 'get'
  })
}

// 新增盘点任务
export function addTask(data) {
  return request({
    url: '/mature/task',
    method: 'post',
    data: data
  })
}

// 修改盘点任务
export function updateTask(data) {
  return request({
    url: '/mature/task',
    method: 'put',
    data: data
  })
}

// 删除盘点任务
export function delTask(taskId) {
  return request({
    url: '/mature/task/' + taskId,
    method: 'delete'
  })
}

// 导出盘点任务
export function exportTask(query) {
  return request({
    url: '/mature/task/export',
    method: 'get',
    params: query
  })
}