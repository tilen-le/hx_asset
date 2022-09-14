<template>
  <div class="app-container">
    <div style="display:flex">
      <el-card selected="" class="taskAmountCard selectedCard" @click="setSearchParam()">
        固定资产总数
      </el-card>
      <el-card selected="" class="taskAmountCard" @click="setSearchParam()">
        已盘点固定资产总数
      </el-card>
      <el-card selected="" class="taskAmountCard" @click="setSearchParam()">
        待盘点固定资产总数
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

    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange"
      @row-click="showTaskDetail">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="盘点任务id" align="center" prop="taskId" /> -->
      <el-table-column label="盘点任务编码" align="center" prop="taskCode" />
      <el-table-column label="发起人" align="center" prop="userCode" />
      <el-table-column label="盘点范围" align="center" prop="inventoryRange" />
      <!-- <el-table-column label="已盘点资产数" align="center" prop="assetCounted" /> -->
      <!-- <el-table-column label="待盘点资产数" align="center" prop="assetNotCounted" /> -->
      <!-- <el-table-column label="异常资产数目" align="center" prop="assetAbnormal" /> -->
      <el-table-column label="盘点开始时间" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="盘点结束时间" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="盘点状态" align="center" prop="status" />
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mature:task:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mature:task:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.taskId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 跳转资产详情页面
    showTaskDetail(row, column, event) {

    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加盘点任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getTask(taskId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskId != null) {
            updateTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTask(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const taskIds = row.taskId || this.ids;
      this.$modal.confirm('是否确认删除盘点任务编号为"' + taskIds + '"的数据项？').then(function () {
        return delTask(taskIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
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

.selectedCard {
  background: rgb(198, 226, 255);
}
</style>
