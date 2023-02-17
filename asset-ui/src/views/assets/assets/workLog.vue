<template>
  <div>
<!--    height="calc(38vh - 130px)"-->
    <el-table v-loading="loading" :data="workLogList" tooltip-effect="light" height="230px">
      <el-table-column label="序号" type="index" align="center"/>
      <el-table-column label="工单号" align="center" prop="wokeCode"/>
      <el-table-column label="工单类型" align="center" prop="processType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_process" :value="scope.row.processType" />
        </template>
      </el-table-column>
      <el-table-column label="提交人" align="center" prop="createBy"/>
      <el-table-column label="备注" align="center" prop="comment"/>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
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
  import {workLogList} from "@/api/assets/assets";

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
        workLogList: []
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
        workLogList(this.queryParams).then(response => {
          this.workLogList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      }
    }
  };
</script>
