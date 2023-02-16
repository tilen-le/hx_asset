<template>
  <div>
   <!-- height="calc(38vh - 130px)"-->
    <el-table v-loading="loading" :data="logList" tooltip-effect="light">
      <el-table-column label="序号" type="index" align="center"/>
      <el-table-column label="资产保管人" align="center" prop="responsiblePersonName" />
      <el-table-column label="资产保管部门" align="center" prop="responsiblePersonDeptName" />
      <el-table-column label="开始时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
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
  import {custodyLogList} from "@/api/assets/assets";

  export default {
    props: {
      assetCode: {
        type: String
      }
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        total: 0,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assetCode: '',
        },
        logList: []
      };
    },
    created() {
      this.loading = true
      this.queryParams.assetCode = this.assetCode
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        custodyLogList(this.queryParams).then(response => {
          this.logList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      }
    }
  };
</script>
