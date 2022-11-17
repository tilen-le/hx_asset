<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实例ID" prop="instanceId">
        <el-input v-model="queryParams.instanceId" placeholder="请输入实例ID" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="发起人工号" prop="userCode">
        <el-input v-model="queryParams.userCode" placeholder="请输入发起人工号" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="平台资产编码" prop="assetCode">
        <el-input v-model="queryParams.assetCode" placeholder="请输入平台资产编码" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option v-for="dict in dict.type.asset_process_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['asset:transform:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['asset:transform:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['asset:transform:remove']"
        >删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading"
          @click="handleExport" v-hasPermi="['asset:transform:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transformList" :height="tableHeight" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实例ID" align="center" prop="instanceId" />
      <el-table-column label="发起人工号" align="center" prop="userCode" />
      <el-table-column label="发起人名称" align="center" prop="userName" />
      <el-table-column label="平台资产编码" align="center" prop="assetCode" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_process_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="附件1" align="center" prop="fileInfo">
        <template slot-scope="scope">
          <el-button v-if="scope.row.fileInfo" size="mini" icon="el-icon-view"
            @click="openDownload(scope.row.fileInfo)">查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="附件2" align="center" prop="fileInfoAdd">
        <template slot-scope="scope">
          <el-button v-if="scope.row.fileInfoAdd" size="mini" icon="el-icon-view"
            @click="openDownload(scope.row.fileInfoAdd)">查看</el-button>
        </template>
      </el-table-column>
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['asset:transform:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['asset:transform:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改资产改造流程对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流程总表id" prop="processId">
          <el-input v-model="form.processId" placeholder="请输入流程总表id" />
        </el-form-item>
        <el-form-item label="实例ID" prop="instanceId">
          <el-input v-model="form.instanceId" placeholder="请输入实例ID" />
        </el-form-item>
        <el-form-item label="发起人工号" prop="userCode">
          <el-input v-model="form.userCode" placeholder="请输入发起人工号" />
        </el-form-item>
        <el-form-item label="平台资产编码" prop="assetCode">
          <el-input v-model="form.assetCode" placeholder="请输入平台资产编码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="附件" :visible.sync="fileDialog" append-to-body>
      <el-row v-for="file in fileList" :key="file.url" style="margin-top:8px">
        {{ file.name }}<el-button size="mini" style="float:right" @click="download(file)">下载</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { listTransform, getTransform, delTransform, addTransform, updateTransform, exportTransform } from "@/api/asset/transform";

export default {
  name: "Transform",
  dicts: ['asset_process_status'],
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
      // 资产改造流程表格数据
      transformList: [],
      tableHeight: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        instanceId: null,
        userCode: null,
        assetCode: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      fileDialog: false,
      fileList: []
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = document.body.offsetHeight - 310;
    })
    var _this = this
    window.onresize = function () {
      _this.tableHeight = document.body.offsetHeight - 310;
    }
  },
  methods: {
    /** 查询资产改造流程列表 */
    getList() {
      this.loading = true;
      listTransform(this.queryParams).then(response => {
        this.transformList = response.rows;
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
        id: null,
        processId: null,
        instanceId: null,
        userCode: null,
        assetCode: null,
        createTime: null,
        updateTime: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加资产改造流程";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTransform(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改资产改造流程";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTransform(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTransform(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm("提示", "确认", "取消", '是否确认删除资产改造流程编号为"' + ids + '"的数据项？').then(function () {
        return delTransform(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm("提示", "确认", "取消", '是否确认导出所有资产改造流程数据项？').then(() => {
        this.exportLoading = true;
        return exportTransform(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => { });
    },
    /** 下载弹窗 */
    openDownload(fileInfo) {
      this.fileList = JSON.parse(fileInfo)
      this.fileDialog = true;
    },
    /** 下载附件 */
    download(file) {
      this.$download.resource(file.url, file.name)
    }
  }
};
</script>
