<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产编码" prop="taskCode">
        <el-input
          v-model="queryParams.taskCode"
          placeholder="请输入资产编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="盘点人" prop="taskCode">
        <el-input
          v-model="queryParams.taskCode"
          placeholder="请输入盘点人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
    </el-form>
    <div style="display:flex">
        <el-card selected="" class="taskAmountCard selectedCard" @click="setSearchParam()">
          固定资产总数
          <div class="card_text">9</div>
        </el-card>
        <el-card selected="" class="taskAmountCard" @click="setSearchParam()">
          已盘点资产总数
        </el-card>
        <el-card selected="" class="taskAmountCard" @click="setSearchParam()">
          待盘点资产总数
        </el-card>
        <el-card selected="" class="taskAmountCard" @click="setSearchParam()">
          盘点异常设备数目
        </el-card>
    </div>
    <el-row :gutter="10" class="mb8">
      <el-tooltip style="float:right" effect="dark" content="刷新" placement="top">
        <el-button size="mini" circle icon="el-icon-refresh" @click="getList" />
      </el-tooltip>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="盘点任务id" align="center" prop="taskId" /> -->
      <el-table-column label="资产编码" align="center" prop="taskCode" />
      <el-table-column label="资产的其他信息" align="center" prop="taskCode" />
      <el-table-column label="盘点人" align="center" prop="userCode" />

      <el-table-column label="盘点时间" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常信息" align="center" prop="userCode" />
      <el-table-column label="盘点状态" align="center" prop="status" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

  </div>
</template>

<script>
import { listTask, getTask, delTask, addTask, updateTask, exportTask } from "@/api/task/task";

export default {
  name: "Task",
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
        userCode: null,
        inventoryRange: null,
        assetCounted: null,
        assetNotCounted: null,
        assetAbnormal: null,
        startDate: null,
        endDate: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询盘点任务列表 */
    getList() {
      this.loading = true;
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows;
        this.total = response.total;
        this.loading = false;
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
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有盘点任务数据项？').then(() => {
        this.exportLoading = true;
        return exportTask(queryParams);
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
