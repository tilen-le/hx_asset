<template>
  <div>
<!--    height="calc(38vh - 130px)"-->
    <el-table v-loading="loading" :data="operationLogList" tooltip-effect="light" height="230px">
      <el-table-column label="序号" type="index" align="center"/>

      <el-table-column label="操作人" align="center" prop="createBy" />
      <el-table-column label="操作类型" align="center" prop="processType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_process" :value="scope.row.processType" />
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="searchInfo(scope.row)"
          >查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>

</template>

<script>
  import {operationLogList, getLogDetail} from "@/api/assets/assets";

  export default {
    props: {
      assetCode: {
        type: String
      }
    },
    dicts: ['asset_process'],
    data() {
      return {
        // 遮罩层
        loading: false,
        total: 0,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assetCode: '',
        },
        operationLogList: []
      };
    },
/*    created() {
      this.loading = true
      this.queryParams.assetCode = this.assetCode
      this.getList();
    },*/
    methods: {
      getList() {
        this.loading = true;
        this.queryParams.assetCode = this.assetCode
        operationLogList(this.queryParams).then(response => {
          this.operationLogList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      searchInfo(row) {
        this.$modal.msgWarning(row.id + "功能尚未设计开发...");
      }
    }
  };
</script>
