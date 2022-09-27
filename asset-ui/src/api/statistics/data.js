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
