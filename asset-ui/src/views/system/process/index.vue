<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程类型编号" prop="processType">
        <el-select v-model="queryParams.processType" placeholder="请选择流程类型编号" clearable size="small">
          <el-option v-for="dict in dict.type.asset_process" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['mature:field:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['mature:field:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['mature:field:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fieldList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <el-table-column label="流程类型编号" align="center" prop="processType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_process" :value="scope.row.processType" />
        </template>
      </el-table-column>
      <el-table-column label="字段键名" align="center" prop="fieldKey" />
      <el-table-column label="字段标签" align="center" prop="fieldLabel" />
      <el-table-column label="字典" align="center" prop="dictType">
        <template slot-scope="scope">
          <router-link :to="'/system/dict-data/index/' + scope.row.dictType" class="link-type">
            <span>{{ scope.row.dictType }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="时间戳格式" align="center" prop="timeFormat" />
      <el-table-column label="排序" align="center" prop="orderNum" />
      <el-table-column label="列宽" align="center" prop="width" />
      <el-table-column label="显示/隐藏" align="center" prop="visible">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.visible" active-value="1" inactive-value="0"
            @change="handleStatusChange(scope.row, 'visible')"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否查询" align="center" prop="queryable">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.queryable" active-value="1" inactive-value="0"
            @change="handleStatusChange(scope.row, 'queryable')"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否可编辑" align="center" prop="editable">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.editable" active-value="1" inactive-value="0"
            @change="handleStatusChange(scope.row, 'editable')"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="1" inactive-value="0"
            @change="handleStatusChange(scope.row, 'status')"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['mature:field:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['mature:field:remove']">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改【流程字段配置】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" @open.once="getTypeList">
      <div class="dialog-body">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="流程类型编号" prop="processType">
            <el-select v-model="form.processType" placeholder="请选择流程类型编号">
              <el-option v-for="dict in dict.type.asset_process" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="字段键名" prop="fieldKey">
            <el-input v-model="form.fieldKey" placeholder="请输入字段键名" />
          </el-form-item>
          <el-form-item label="字段标签" prop="fieldLabel">
            <el-input v-model="form.fieldLabel" placeholder="请输入字段标签" />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="form.status" active-value="1" inactive-value="0" />
          </el-form-item>
          <el-form-item label="显示/隐藏" prop="visible">
            <el-switch v-model="form.visible" active-value="1" inactive-value="0" />
          </el-form-item>
          <el-form-item label="字典" prop="dictType">
            <el-select v-model="form.dictType" placeholder="请选择字典" clearable>
              <el-option v-for="item in typeOptions" :key="item.dictId" :label="item.dictName" :value="item.dictType" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间戳格式" prop="timeFormat">
            <el-input v-model="form.timeFormat" placeholder="请输入时间戳格式" clearable />
          </el-form-item>
          <el-form-item label="是否查询" prop="queryable">
            <el-switch v-model="form.queryable" active-value="1" inactive-value="0" />
          </el-form-item>
          <el-form-item label="是否可编辑" prop="editable">
            <el-switch v-model="form.editable" active-value="1" inactive-value="0" />
          </el-form-item>
          <el-form-item label="排序" prop="orderNum">
            <el-input-number v-model="form.orderNum" :step="1" step-strictly />
          </el-form-item>
          <el-form-item label="列宽" prop="width">
            <el-input v-model="form.width" placeholder="请输入列宽" type="number" />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listField, getField, delField, addField, updateField } from "@/api/process/field";
import { listType } from "@/api/system/dict/type";

export default {
  name: "Field",
  dicts: ["asset_process"],
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 【流程字段配置】表格数据
      fieldList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processType: null,
        fieldKey: null,
        fieldLabel: null,
        status: null,
        visible: null,
        dictType: null,
        timeFormat: null,
        queryable: null,
        editable: null,
        orderNum: null,
        width: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        processType: [
          { required: true, message: "流程类型编号不能为空", trigger: "change" }
        ],
        fieldKey: [
          { required: true, message: "字段键名不能为空", trigger: "change" }
        ]
      },
      // 类型数据字典
      typeOptions: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【流程字段配置】列表 */
    getList() {
      this.loading = true;
      listField(this.queryParams).then(response => {
        this.fieldList = response.rows;
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
        processType: null,
        fieldKey: null,
        fieldLabel: null,
        status: "1",
        remark: null,
        visible: "1",
        dictType: null,
        timeFormat: null,
        queryable: "0",
        editable: "0",
        orderNum: null,
        width: null
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
      this.title = "添加【流程字段配置】";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getField(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改【流程字段配置】";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateField(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addField(this.form).then(response => {
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
      this.$modal.confirm("删除", "确认", "取消", '确认删除？').then(function () {
        return delField(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    // 任务状态修改
    handleStatusChange(row, prop) {
      let text = row[prop] === "1" ? "启用" : "停用";
      updateField(row).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row[prop] = row.status === "0" ? "1" : "0";
      });
    },
    /** 查询字典类型列表 */
    getTypeList() {
      listType().then(response => {
        this.typeOptions = response.rows;
      });
    },
  }
};
</script>

<style scoped>
.dialog-body{
  height: calc(65vh);
  padding:0 20px;
  overflow: auto;
}
</style>
