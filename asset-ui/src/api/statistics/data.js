import request from "@/utils/request";

// 总资产统计
export function assetCount(data) {
  return request({
    url: "/asset/assetCount",
    method: "post",
    data: data,
  });
}

export function assetCountByCategory(data) {
  return request({
    url: "/asset/assetCountByCategory",
    method: "post",
    data: data,
  });
}

export function assetCountByDept(data) {
  return request({
    url: "/asset/assetCountByDept",
    method: "post",
    data: data,
  });
}

//  入库/报废/外卖/改造-数量和时间折线图统计
export function assetProcessTypeTimeNumCount(data) {
  return request({
    url: "/asset/assetProcessTypeTimeNumCount",
    method: "post",
    data: data,
  });
}

//  入库/报废/外卖/改造-数量和类别折线图统计
export function assetProcessTypeCategoryNumCount(data) {
  return request({
    url: "/asset/assetProcessTypeCategoryNumCount",
    method: "post",
    data: data,
  });
}
