import request from '@/utils/request'

//获取资产配置人员
export function listAssetManager(query) {
  return request({
    url: '/manage/config/list',
    method: 'get',
    params: query
  })
}

//获取配置人员详情/manage/config/{id}
export function getAssetManager(id) {
  return request({
    url: '/manage/config/' + id,
    method: 'get'
  })
}

//删除配置人员/manage/config/{ids}
export function deleteAssetManager(ids) {
  return request({
    url: '/manage/config/' + ids,
    method: 'delete'
  })
}

//添加配置人员/manage/config/add
export function addAssetManger(data) {
  return request({
    url: '/manage/config/add',
    method: 'post',
    data: data
  })
}

//修改配置人员/manage/config/edit
export function editAssetManger(data) {
  return request({
    url: '/manage/config/edit',
    method: 'put',
    data: data
  })
}
