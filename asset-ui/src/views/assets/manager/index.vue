<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产类型" prop="assetType">
        <el-select v-model="queryParams.assetType" placeholder="请选择资产类型" clearable size="small">
          <el-option
            v-for="dict in dict.type.asset_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产分类" prop="assetCategory">
        <el-select v-model="queryParams.assetCategory" placeholder="请选择资产分类" clearable size="small">
          <el-option
            v-for="dict in dict.type.asset_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属公司" prop="company">
        <el-select v-model="queryParams.company" placeholder="请选择所属公司" clearable size="small">
          <el-option
            v-for="dict in dict.type.asset_company"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="地区" prop="location">
        <el-select v-model="queryParams.location" placeholder="请选择地区" clearable size="small">
          <el-option
            v-for="dict in dict.type.asset_location"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产管理部门" prop="manageDept" label-width="100px">
        <el-select v-model="queryParams.manageDept" placeholder="请选择资产管理部门" clearable size="small">
          <el-option
            v-for="dict in dict.type.asset_location"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['assets:manager:add']"
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
          v-hasPermi="['assets:manager:delete']"
        >删除
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="managerList" @selection-change="handleSelectionChange" tooltip-effect="light">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" />
<!--
      资产管理部门
      -->
      <el-table-column label="资产类型" align="center" prop="priority">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_type" :value="scope.row.assetType"/>
        </template>
      </el-table-column>
      <el-table-column label="资产分类" align="center" prop="assetCategory">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_category" :value="scope.row.assetCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="所属公司" align="center" prop="company">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_company" :value="scope.row.company"/>
        </template>
      </el-table-column>
      <el-table-column label="地区" align="center" prop="location">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_location" :value="scope.row.location.split(',')"/>
        </template>
      </el-table-column>
      <el-table-column label="资产管理员" align="center" prop="assetManager" :show-overflow-tooltip="true" />
      <el-table-column label="账务管理员" align="center" prop="financialManager" :show-overflow-tooltip="true" />
      <el-table-column label="创建日期" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新日期" align="center" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['assets:manager:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['assets:manager:delete']"
          >删除</el-button>
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

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="资产类型" prop="assetType">
          <el-select v-model="form.assetType" placeholder="请选择资产类型" clearable size="small" style="width: 80%">
            <el-option v-for="dict in dict.type.asset_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产分类" prop="assetCategory">
          <el-select v-model="form.assetCategory" placeholder="请选择资产分类" clearable size="small" style="width: 80%">
            <el-option v-for="dict in dict.type.asset_category" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属公司" prop="company">
          <el-select v-model="form.company" placeholder="请选择所属公司" clearable size="small" style="width: 80%">
            <el-option v-for="dict in dict.type.asset_company" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="地区" prop="location">
          <el-select v-model="form.location" placeholder="请选择地区" clearable multiple size="small" style="width: 80%">
            <el-option v-for="dict in dict.type.asset_location" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产管理员" prop="assetManager">
          <el-select popper-class="long_select" v-model="form.assetManager" placeholder="请选择资产管理员" multiple filterable style="width: 80%;" :multiple-limit="10">
            <el-option v-for="item in common_users" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="财务管理员" prop="financialManager">
          <el-select popper-class="long_select" v-model="form.financialManager" placeholder="请选择财务管理员" multiple filterable style="width: 80%;" :multiple-limit="10" >
            <el-option v-for="item in common_users" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue"/>
          </el-select>
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
  import {deleteAssetManager, getAssetManager, listAssetManager, addAssetManger, editAssetManger} from "@/api/assets/manager";
  import {getDicts} from "@/api/system/dict/data";

  export default {
  name: "assets_manager",
  dicts: ['asset_type', 'asset_category', 'asset_company', 'asset_location'],
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
      managerList: [],
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assetCategory: null,
        assetType: null,
        company: null,
        location: null,
        manageDept: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        assetType: [
          { required: true, message: "该项必填", trigger: "blur" }
        ],
        assetCategory: [
          { required: true, message: "该项必填", trigger: "blur" }
        ],
        company: [
          { required: true, message: "该项必填", trigger: "blur" }
        ],
        location: [
          { required: true, message: "该项必填", trigger: "blur" }
        ],
        manageDept: [
          { required: true, message: "该项必填", trigger: "blur" }
        ],
        assetManager: [
          { required: true, message: "该项必填", trigger: "blur" }
        ],
        financialManager: [
          { required: true, message: "该项必填", trigger: "blur" }
        ]
      },
      asset_dept: [],
      common_users: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询成熟度流程列表 */
    getList() {
      this.loading = true;
      listAssetManager(this.queryParams).then(response => {
        this.managerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getCommonUsers(){
      const userList = this.common_users
      if (userList.length == 0) {
        getDicts("common_users").then(res => {
          this.common_users = res.data
        })
      }
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
        assetCategory: null,
        assetType: null,
        company: null,
        location: null,
        manageDept: null,
        assetManager: null,
        financialManager: null
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getCommonUsers();
      this.open = true;
      this.title = "添加资产管理人员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getCommonUsers();
      getAssetManager(row.id).then(response => {
        this.form = response.data;
        this.form.location = this.form.location.split(',')
        this.form.assetManager = this.form.assetManager.split(',')
        this.form.financialManager = this.form.financialManager.split(',')
        this.open = true;
        this.title = "修改资产管理人员";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm("提示", "确认","取消", '是否确认删除选中的数据项？').then(function() {
        return deleteAssetManager(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    submitForm(){
      this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.location = this.form.location.join()
            this.form.assetManager = this.form.assetManager.join()
            this.form.financialManager = this.form.financialManager.join()
            if (this.form.id != undefined) {
              editAssetManger(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addAssetManger(this.form).then(response => {
                this.$modal.msgSuccess("添加成功");
                this.open = false;
                this.getList();
              });
            }
          }
      });
    }
  }
};
</script>
<style>
  .long_select {
    max-width: 100px;
  }
  .long_select .el-select-dropdown__item {
    overflow: visible;
    display: block;
  }
</style>

