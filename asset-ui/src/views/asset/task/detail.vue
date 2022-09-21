<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产编码" prop="assetCode">
        <el-input
          v-model="queryParams.assetCode"
          placeholder="请输入资产编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="盘点人" prop="userNickName">
        <el-input
          v-model="queryParams.userNickName"
          placeholder="请输入盘点人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading" @click="handleExport" v-hasPermi="['asset:counting:export']">导出</el-button>
      </el-form-item>
    </el-form>
    <div style="display:flex">
        <el-card class="taskAmountCard" @click.native="statusSearch('')">
          固定资产总数
          <div class="card_text">{{countResult.total}}</div>
        </el-card>
        <el-card class="taskAmountCard" @click.native="statusSearch('1')">
          已盘点资产总数
          <div class="card_text">{{countResult.counted}}</div>
        </el-card>
        <el-card class="taskAmountCard" @click.native="statusSearch('0')">
          待盘点资产总数
          <div class="card_text">{{countResult.notCounted}}</div>
        </el-card>
        <el-card class="taskAmountCard" @click.native="statusSearch('2')">
          盘点异常设备数目
          <div class="card_text">{{countResult.abnormal}}</div>
        </el-card>
    </div>
    <el-table v-loading="loading" :data="taskList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资产编码" align="center" prop="assetCode" width="180"/>
      <el-table-column label="资产名称" align="center" prop="assetName" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="存放地点" align="center" prop="location" />
      <el-table-column label="管理部门" align="center" prop="manageDept" />
      <el-table-column label="保管人" align="center" prop="responsiblePersonName" />
      <el-table-column label="保管部门" align="center" prop="responsiblePersonDept" />
      <el-table-column label="使用场景" align="center" prop="usageScenario" />
      <el-table-column label="规格型号" align="center" prop="standard" />
      <el-table-column label="盘点人" align="center" prop="userNickName" />
      <el-table-column label="盘点时间" align="center" prop="countingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常信息" align="center" prop="comment" />
      <el-table-column label="盘点状态" align="center" prop="countingStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_counting_status" :value="scope.row.countingStatus"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

  </div>
</template>

<script>

  import {countRecord, listRecord, exportRecord } from "@/api/task/record";

  export default {
  name: "taskDetail",
  dicts: ['asset_counting_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 盘点任务表格数据
      taskList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskCode: null,
        assetCode: null,
        userNickName: null,
        countingStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      countResult: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    statusSearch(status) {
      this.queryParams.countingStatus = status;
      this.getList();
    },
    /** 查询盘点任务列表 */
    getList() {
      this.queryParams.taskCode = this.$route.params && this.$route.params.id
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.taskList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      const taskCode = this.queryParams.taskCode
      countRecord({taskCode: taskCode}).then(response => {
        this.countResult = response.data;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        taskId: null,
        taskCode: null,
        userCode: null,
        inventoryRange: null,
        assetCounted: null,
        assetNotCounted: null,
        assetAbnormal: null,
        startDate: null,
        endDate: null,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.countingStatus = null;
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm("提示", "确认","取消", "是否确认导出所有符合条件数据项？").then(() => {
        this.exportLoading = true;
        return exportRecord(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => { });
    }
  }
};
</script>
<style scoped>
.taskAmountCard {
  width: 180px;
  height: 100px;
  margin: 10px;
  text-align: center;
  cursor: pointer;
  font-weight: bold;
  background-color: rgb(236, 245, 255);
}
.card_text {
  color: blue;
  margin-top: 15px;
  font-size: 15pt;
}
.selectedCard {
  background: rgb(198, 226, 255);
}
</style>
