import request from "@/utils/request";

// 查询盘点统计
export function inventoryCount(query) {
  return request({
    url: "/asset/counting/inventoryCount",
    method: "get",
    params: query,
  });
}

//盘点统计列表
export function inventoryCountList(query) {
  return request({
    url: "/asset/counting/inventoryCountList",
    method: "get",
    params: query,
  });
}
