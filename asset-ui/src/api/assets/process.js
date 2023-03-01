import request from '@/utils/request'

//派发
export function receiveAsset(data) {
  return request({
    url: '/asset/process/receiveAsset',
    method: 'put',
    data: data
  })
}

//转移
export function transferAsset(data) {
  return request({
    url: '/asset/process/transferAsset',
    method: 'put',
    data: data
  })
}

//退货
export function returnAsset(data) {
  return request({
    url: '/asset/process/returnAsset',
    method: 'put',
    data: data
  })
}

//转固
export function fixationAsset(data) {
  return request({
    url: '/asset/process/fixationAsset',
    method: 'put',
    data: data
  })
}

//维修
export function maintainAsset(data) {
  return request({
    url: '/asset/process/maintainAsset',
    method: 'put',
    data: data
  })
}

//闲置
export function unusedAsset(data) {
  return request({
    url: '/asset/process/unusedAsset',
    method: 'put',
    data: data
  })
}

//报废
export function scrapAsset(data) {
  return request({
    url: '/asset/process/scrapAsset',
    method: 'put',
    data: data
  })
}

//待外卖
export function waiteTakeOutAsset(data) {
  return request({
    url: '/asset/process/waiteTakeOutAsset',
    method: 'put',
    data: data
  })
}

//盘亏
export function inventoryLossAsset(data) {
  return request({
    url: '/asset/process/inventoryLossAsset',
    method: 'put',
    data: data
  })
}

//已维修
export function maintainedAsset(data) {
  return request({
    url: '/asset/process/maintainedAsset',
    method: 'put',
    data: data
  })
}

//已外卖
export function takeOutAsset(data) {
  return request({
    url: '/asset/process/takeOutAsset',
    method: 'put',
    data: data
  })
}

//已报废
export function scrapedAsset(data) {
  return request({
    url: '/asset/process/scrapedAsset',
    method: 'put',
    data: data
  })
}

//返修
export function repairAsset(data) {
  return request({
    url: '/asset/process/repairAsset',
    method: 'put',
    data: data
  })
}

//归还
export function backAsset(data) {
  return request({
    url: '/asset/process/backAsset',
    method: 'put',
    data: data
  })
}

//获取转移详情
export function getTransferInfo(assetCode) {
  return request({
    url: '/asset/process/getTransferInfo/' + assetCode,
    method: 'get'
  })
}

//账务转移
export function accountTransferAsset(data) {
  return request({
    url: '/asset/process/accountTransferAsset',
    method: 'put',
    data: data
  })
}

//编辑
export function editAsset(data) {
  return request({
    url: '/asset/process/editAsset',
    method: 'put',
    data: data
  })
}


