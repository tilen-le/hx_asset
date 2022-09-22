<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起人" prop="userCode">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入发起人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startDate">
        <el-date-picker clearable size="small"
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择盘点开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endDate">
        <el-date-picker clearable size="small"
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择盘点结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['asset:task:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['asset:task:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['asset:task:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange" tooltip-effect="light">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="任务编码" align="center" prop="taskCode" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="showTaskDetail(scope.row)">{{ scope.row.taskCode }}</el-link>
        </template>
      </el-table-column>-->
      <el-table-column label="任务名称" align="center" prop="taskName" width="220" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="showTaskDetail(scope.row)">{{ scope.row.taskName }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="盘点人" align="center" prop="inventoryUsersName" :show-overflow-tooltip="true" />
      <el-table-column label="盘点组织" align="center" prop="inventoryDeptName" width="180" :show-overflow-tooltip="true" />
       <el-table-column label="已盘点数" align="center" prop="assetCounted" width="80" />
       <el-table-column label="待盘点数" align="center" prop="assetNotCounted" width="80" />
       <el-table-column label="异常数" align="center" prop="assetAbnormal" width="80" />
      <el-table-column label="开始时间" align="center" prop="startDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发起人" align="center" prop="createByName" width="100" />
      <el-table-column label="盘点状态" align="center" prop="status" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inventory_task_status" :value="scope.row.status"/>
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

    <!-- 添加或修改盘点任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="任务名称" prop="taskName" >
          <el-input v-model="form.taskName" placeholder="请输入盘点任务名称" style="width: 80%;" />
        </el-form-item>
        <el-form-item label="盘点人" prop="inventoryUserList">
          <el-select v-model="form.inventoryUserList" placeholder="请选择盘点人" multiple filterable style="width: 80%;">
            <el-option v-for="item in userOptions" :key="item.userName" :label="item.showName" :value="item.userName"/>
          </el-select>
        </el-form-item>
        <el-form-item label="盘点组织" prop="inventoryDept">
          <treeselect v-model="form.inventoryDept" :options="deptOptions" :show-count="true" placeholder="请选择盘点组织" style="width: 80%;" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startDate">
          <el-date-picker clearable size="small"
            v-model="form.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择盘点开始时间" style="width: 80%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker clearable size="small"
            v-model="form.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择盘点结束时间" style="width: 80%;">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTask, getTask, delTask, addTask, exportTask } from "@/api/task/task";
import { treeselect } from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { allUser } from '@/api/system/user'

export default {
  name: "Task",
  components: { Treeselect },
  dicts: ['inventory_task_status'],
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
        taskName: null,
        createBy: null,
        startDate: null,
        endDate: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        taskName: [
          { required: true, message: '此项必填', trigger: "blur" }
        ],
        inventoryUserList: [
          { required: true, message: '此项必填', trigger: "blur" }
        ],
        inventoryDept: [
          { required: true, message: '此项必填', trigger: "blur" }
        ],
        startDate: [
          { required: true, message: '此项必填', trigger: "blur" }
        ],
        endDate: [
          { required: true, message: '此项必填', trigger: "blur" }
        ],
      },
      // 部门树选项
      deptOptions: [],
      userOptions: [],
      defaultProps: {
        children: "children",
        label: "label"
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询部门下拉树结构 */
    getTreeSelect() {
      if (this.deptOptions.length == 0) {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      }
    },
    getUsers() {
      if (this.userOptions.length == 0) {
        allUser().then(response => {
          this.userOptions = response.data;
        });
      }
    },
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
        inventoryDept: null,
        startDate: null,
        endDate: null,
        inventoryUserList: []
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
      this.ids = selection.map(item => item.taskCode)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 跳转资产详情页面
    showTaskDetail(row) {
        this.$router.push('/inventory/taskInfo/detail/' + row.taskCode)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeSelect();
      this.getUsers();
      this.open = true;
      this.title = "添加盘点任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const taskCode = row.taskCode || this.ids
      getTask(taskCode).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addTask(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const taskCodes = row.taskCode || this.ids;
      this.$modal.confirm("提示", "确认","取消", '是否确认删除盘点任务编号为"' + taskCodes + '"的数据项？').then(function() {
        return delTask(taskCodes);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm("提示", "确认","取消", "是否确认导出所有符合条件数据项？").then(() => {
        this.exportLoading = true;
        return exportTask(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
