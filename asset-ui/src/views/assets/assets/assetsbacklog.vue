<template>
  <div style="width: 100%;height: 100%">
    <el-table v-loading="loading" :data="assetList" height="250px" style="height: 400px" tooltip-effect="light">
      <el-table-column align="center" label="资产编码" prop="assetCode" width="150px">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="goDetail(scope.row)">{{ scope.row.assetCode }}</el-link>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" align="center" label="资产大类" prop="assetType"/>
      <el-table-column align="center" label="资产中类" prop="assetCategory"/>
      <el-table-column align="center" label="资产小类" prop="assetSubCategory"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="资产名称" prop="assetName"/>
      <el-table-column align="center" label="提交人" prop="responsiblePersonName"/>
      <el-table-column align="center" label="提交时间" prop="capitalizationDate">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.capitalizationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
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
import {listAssets} from "@/api/assets/assets";
import {backloglistAssets} from "../../../api/assets/assets";

export default {
  name: "assetsbacklog",
  dicts: ['asset_status', 'sap_card_asset_category', 'asset_company', 'asset_fixed', 'asset_management_category'],
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
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listAssets(this.addDateRange(this.queryParams, this.queryParams.capitalizationDateRange, "customize", "capitalizationStartDate", "capitalizationEndDate"))
        .then(response => {
          this.assetList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
    },
    goDetail(row) {
      // this.$router.push('/asset/assetInfo/' + row.assetCode)
      this.$router.push({
        name: 'assetInfo',
        params: {assetCode: row.assetCode, flag: true}
      })
    },
  },
};
</script>

<style scoped>

</style>
