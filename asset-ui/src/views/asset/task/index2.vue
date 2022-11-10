<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item v-for="item, index in queryProperty" :key="item.fieldKey" :label="item.fieldLabel"
        :prop="'domains.' + index + '.value'">
        <template v-if="!item.timeFormat">
          <el-input v-model="queryParams.domains[index].value" :placeholder="'请输入' + item.fieldLabel" clearable
            size="small" @keyup.enter.native="handleQuery" />
        </template>
        <template v-if="item.timeFormat">
          <el-date-picker clearable size="small" v-model="queryParams.domains[index].value" type="date"
            :value-format="item.timeFormat">
          </el-date-picker>
        </template>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['asset:task:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['asset:task:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading"
          @click="handleExport" v-hasPermi="['asset:task:export']">导出</el-button>
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
      <el-table-column v-for="item in tableProperty" :key="item.fieldKey" :label="item.fieldLabel" :prop="item.fieldKey"
        :width="item.width" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <!--<i class="el-icon-time"></i>-->
          <slot :name="item.fieldKey" :data="scope.row[item.fieldKey]" :row="scope.row" :index="scope.$index">
            <template v-if="!item.timeFormat && !item.dictType">
              <span>{{ scope.row[item.fieldKey] }}</span>
            </template>
            <template v-if="item.timeFormat">
              <span>{{ formatDate(scope.row[item.fieldKey], item.timeFormat) }}</span>
            </template>
            <template v-if="item.dictType">
              <dict-tag :options="dict.type[item.dictType]" :value="scope.row[item.fieldKey]" />
            </template>
          </slot>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <!-- 添加或修改盘点任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item v-for="item, index in form.domains" :key="index" :label="item.label"
          :prop="'domains.' + index + '.value'">
          <template v-if="true">
            <el-input v-model="form.domains[index].value" :placeholder="'请输入' + item.label" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </template>
          <template v-if="false && item.timeFormat">
            <el-date-picker clearable size="small" v-model="form.domains[index].value" type="date"
              :value-format="item.timeFormat">
            </el-date-picker>
          </template>
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
import { formatDate } from 'element-ui/src/utils/date-util';
import { listData, getData, delData, addData, exportData } from "@/api/process/variable";
import { listField } from "@/api/process/field";

import { treeselect } from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { allUser } from '@/api/system/user'

export default {
  name: "Task",
  dicts: [],
  components: { Treeselect },
  data() {
    return {
      property: [],
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
        domains: [],
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {
        domains: []
      },
      // 表单校验
      rules: {
        // taskName: [
        //   { required: true, message: '此项必填', trigger: "blur" }
        // ]
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
    listField({ processType: 100 }).then(response => {
      this.property = response.rows.sort((a, b) => a.orderNum - b.orderNum)
      let dictOptions = [];
      response.rows.forEach(row => {
        if (row.dictType)
          dictOptions.push(row.dictType)
        if (row.queryable == 1) {
          this.queryParams.domains.push({
            prop: row.fieldKey,
            value: ""
          })
        }
        if (row.editable == 1) {
          this.form.domains.push({
            label: row.fieldLabel,
            prop: row.fieldKey,
            value: ""
          })
        }
      })
      this.dict.init(dictOptions)
    });
    this.getList();
  },
  computed: {
    queryProperty() {
      return this.property.filter(item => item.queryable == 1)
    },
    tableProperty() {
      return this.property.filter(item => item.visible == 1)
    }
  },
  methods: {
    formatDate: formatDate,
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
    getQueryParams() {
      var queryParams = {};
      this.queryParams.domains.forEach(item => {
        queryParams[item.prop] = item.value
      })
      queryParams.pageNum = this.queryParams.pageNum
      queryParams.pageSize = this.queryParams.pageSize
      return queryParams;
    },
    /** 查询盘点任务列表 */
    getList() {
      var queryParams = this.getQueryParams()
      this.loading = true;
      listData(queryParams).then(response => {
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
      this.form.domains.forEach(item => { item.value = "" })
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
      this.single = selection.length !== 1
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
    /** 修改按钮操作 */   //todo
    // handleUpdate(row) {
    //   this.reset();
    //   const taskCode = row.taskCode || this.ids
    //   getData(taskCode).then(response => {
    //     // this.form = response.data;
    //     this.open = true;
    //     this.title = "修改盘点任务";
    //   });
    // },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          var data = {};
          this.form.domains.forEach(item => {
            data[item.prop] = item.value;
          })
          addData(data).then(response => {
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
      this.$modal.confirm("提示", "确认", "取消", '是否确认删除盘点任务编号为"' + taskCodes + '"的数据项？').then(function () {
        return delData(taskCodes);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.getQueryParams();
      this.$modal.confirm("提示", "确认", "取消", "是否确认导出所有符合条件数据项？").then(() => {
        this.exportLoading = true;
        return exportData(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => { });
    }
  }
};
</script>
