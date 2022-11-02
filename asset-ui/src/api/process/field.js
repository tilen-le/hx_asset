import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listField(query) {
  return request({
    url: '/asset/process/field/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getField(id) {
  return request({
    url: '/asset/process/field/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addField(data) {
  return request({
    url: '/asset/process/field',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateField(data) {
  return request({
    url: '/asset/process/field',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delField(id) {
  return request({
    url: '/asset/process/field/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
// export function exportField(query) {
//   return request({
//     url: '/mature/field/export',
//     method: 'get',
//     params: query
//   })
// }
