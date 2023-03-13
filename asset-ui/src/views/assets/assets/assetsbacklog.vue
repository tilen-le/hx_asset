<template>
  <div style="width: 100%;height: 100%">
    <el-table v-loading="loading" :data="assetList" height="250px" style="height: 400px" tooltip-effect="light">
      <el-table-column align="center" label="资产编码" prop="assetCode" width="150px">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="goDetail(scope.row)">{{ scope.row.assetCode }}</el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="资产大类" prop="assetType" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="资产中类" prop="assetCategory"/>
      <el-table-column align="center" label="资产小类" prop="assetSubCategory"/>
      <el-table-column align="center" label="资产名称" prop="assetName" :show-overflow-tooltip="true"/>
      <el-table-column align="center" label="提交人" prop="submitter"/>
      <el-table-column align="center" label="提交时间" prop="createTime"/>
    </el-table>
    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      style="margin-top: 0px;height: 50px"
      @pagination="getList"
    />
  </div>
</template>

<script>

import {backloglistAssets} from "@/api/assets/assets";

export default {
  name: "assetsbacklog",
  data() {
    return {
      // 遮罩层
      loading: true,
      assetList: [],
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assetCode: null,
        assetName: null,
        assetType: null,
        assetCategory: null,
        assetSubCategory: null,
        assetStatus: null,
        capitalizationDateRange: [],
        company: null,
        fixed: null,
        responsiblePersonDept: null
      },

    };
  },
  methods: {
    getList() {
      this.loading = true;
      backloglistAssets({type: "ZCZY"})
        .then(response => {
          this.assetList = response.data;
          // this.total = response.total;
          this.loading = false;
        });
    },
    goDetail(row) {
      // const flag = true;
      // this.$router.push('/asset/assetInfo/' + row.assetCode+flag)
      this.$router.push({
        name: 'assetInfo',
        query: {assetCode: row.assetCode, flag: 1}
      })
    },
  },
};
</script>

<style scoped>

</style>
