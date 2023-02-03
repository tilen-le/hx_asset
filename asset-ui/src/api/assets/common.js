import request from '@/utils/request'


export function getAssetTypeTree() {
  return request({
    url: '/asset/getAssetCategoryTree',
    method: 'get'
  })
}


