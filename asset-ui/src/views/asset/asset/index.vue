<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="固定资产名称" prop="assetName">
        <el-input v-model="queryParams.assetName" placeholder="请输入固定资产名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="平台资产编号" prop="assetCode">
        <el-input v-model="queryParams.assetCode" placeholder="请输入平台资产编号" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="存放地点" prop="location">
        <el-input v-model="queryParams.location" placeholder="请输入存放地点" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="成本中心" prop="costCenter">
        <el-input v-model="queryParams.costCenter" placeholder="请输入成本中心" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['asset:asset:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['asset:asset:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['asset:asset:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
          v-hasPermi="['asset:asset:import']">{{$t('common_field.import')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading"
          @click="handleExport" v-hasPermi="['asset:asset:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="assetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资产id" align="center" prop="assetId" />
      <el-table-column label="固定资产名称" align="center" prop="assetName" />
      <el-table-column label="平台资产编号" align="center" prop="assetCode" />
      <el-table-column label="财务资产编号" align="center" prop="financialAssetCode" />
      <el-table-column label="保管人工号" align="center" prop="responsiblePersonNo" />
      <el-table-column label="资产分类描述" align="center" prop="category" />
      <el-table-column label="资产状态描述" align="center" prop="assetStatus" />
      <el-table-column label="出厂编号" align="center" prop="factoryNo" />
      <el-table-column label="规格型号" align="center" prop="standard" />
      <el-table-column label="单位" align="center" prop="measure" />
      <el-table-column label="采购人" align="center" prop="buyer" />
      <el-table-column label="采购日期" align="center" prop="buyDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.buyDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="净值" align="center" prop="netWorth" />
      <el-table-column label="保修期" align="center" prop="warranty" />
      <el-table-column label="预计使用寿命" align="center" prop="canUseMonths" />
      <el-table-column label="预计使用寿命" align="center" prop="canUseYears" />
      <el-table-column label="资本化日期/资产价值录入日期" align="center" prop="capitalizationDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.capitalizationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资产原值币制" align="center" prop="monetaryUnit" />
      <el-table-column label="公司代码" align="center" prop="companyCode" />
      <el-table-column label="公司代码描述" align="center" prop="companyName" />
      <el-table-column label="存放地点" align="center" prop="location" />
      <el-table-column label="供应商" align="center" prop="provider" />
      <el-table-column label="数量" align="center" prop="amount" />
      <el-table-column label="品牌" align="center" prop="brand" />
      <el-table-column label="成本中心" align="center" prop="costCenter" />
      <el-table-column label="成本中心描述" align="center" prop="costCenterName" />
      <el-table-column label="管理部门描述" align="center" prop="manageDept" />
      <el-table-column label="合同单号" align="center" prop="contractNo" />
      <el-table-column label="申请人" align="center" prop="proposer" />
      <el-table-column label="资产使用场景" align="center" prop="usageScenario" />
      <el-table-column label="备注" align="center" prop="comment" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['asset:asset:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['asset:asset:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改资产表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="固定资产名称" prop="assetName">
          <el-input v-model="form.assetName" placeholder="请输入固定资产名称" />
        </el-form-item>
        <el-form-item label="平台资产编号" prop="assetCode">
          <el-input v-model="form.assetCode" placeholder="请输入平台资产编号" />
        </el-form-item>
        <el-form-item label="财务资产编号" prop="financialAssetCode">
          <el-input v-model="form.financialAssetCode" placeholder="请输入财务资产编号" />
        </el-form-item>
        <el-form-item label="保管人工号" prop="responsiblePersonNo">
          <el-input v-model="form.responsiblePersonNo" placeholder="请输入保管人工号" />
        </el-form-item>
        <el-form-item label="资产分类描述" prop="category">
          <el-input v-model="form.category" placeholder="请输入资产分类描述" />
        </el-form-item>
        <el-form-item label="资产状态描述">
          <el-radio-group v-model="form.assetStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出厂编号" prop="factoryNo">
          <el-input v-model="form.factoryNo" placeholder="请输入出厂编号" />
        </el-form-item>
        <el-form-item label="规格型号" prop="standard">
          <el-input v-model="form.standard" placeholder="请输入规格型号" />
        </el-form-item>
        <el-form-item label="单位" prop="measure">
          <el-input v-model="form.measure" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="采购人" prop="buyer">
          <el-input v-model="form.buyer" placeholder="请输入采购人" />
        </el-form-item>
        <el-form-item label="采购日期" prop="buyDate">
          <el-date-picker clearable size="small" v-model="form.buyDate" type="date" value-format="yyyy-MM-dd"
            placeholder="选择采购日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="净值" prop="netWorth">
          <el-input v-model="form.netWorth" placeholder="请输入净值" />
        </el-form-item>
        <el-form-item label="保修期" prop="warranty">
          <el-input v-model="form.warranty" placeholder="请输入保修期" />
        </el-form-item>
        <el-form-item label="预计使用寿命" prop="canUseMonths">
          <el-input v-model="form.canUseMonths" placeholder="请输入预计使用寿命" />
        </el-form-item>
        <el-form-item label="预计使用寿命" prop="canUseYears">
          <el-input v-model="form.canUseYears" placeholder="请输入预计使用寿命" />
        </el-form-item>
        <el-form-item label="资本化日期/资产价值录入日期" prop="capitalizationDate">
          <el-date-picker clearable size="small" v-model="form.capitalizationDate" type="date" value-format="yyyy-MM-dd"
            placeholder="选择资本化日期/资产价值录入日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="资产原值币制" prop="monetaryUnit">
          <el-input v-model="form.monetaryUnit" placeholder="请输入资产原值币制" />
        </el-form-item>
        <el-form-item label="公司代码" prop="companyCode">
          <el-input v-model="form.companyCode" placeholder="请输入公司代码" />
        </el-form-item>
        <el-form-item label="公司代码描述" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入公司代码描述" />
        </el-form-item>
        <el-form-item label="存放地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入存放地点" />
        </el-form-item>
        <el-form-item label="供应商" prop="provider">
          <el-input v-model="form.provider" placeholder="请输入供应商" />
        </el-form-item>
        <el-form-item label="数量" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="成本中心" prop="costCenter">
          <el-input v-model="form.costCenter" placeholder="请输入成本中心" />
        </el-form-item>
        <el-form-item label="成本中心描述" prop="costCenterName">
          <el-input v-model="form.costCenterName" placeholder="请输入成本中心描述" />
        </el-form-item>
        <el-form-item label="管理部门描述" prop="manageDept">
          <el-input v-model="form.manageDept" placeholder="请输入管理部门描述" />
        </el-form-item>
        <el-form-item label="合同单号" prop="contractNo">
          <el-input v-model="form.contractNo" placeholder="请输入合同单号" />
        </el-form-item>
        <el-form-item label="申请人" prop="proposer">
          <el-input v-model="form.proposer" placeholder="请输入申请人" />
        </el-form-item>
        <el-form-item label="资产使用场景" prop="usageScenario">
          <el-input v-model="form.usageScenario" placeholder="请输入资产使用场景" />
        </el-form-item>
        <el-form-item label="备注" prop="comment">
          <el-input v-model="form.comment" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text"><em>{{$t('common_field.upload_click')}}</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> {{$t('user.update_confirm')}}
          </div>
          <span>{{$t('common_field.upload_format_excel')}}</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">{{$t('common_field.download_template')}}</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">{{ $t('common_field.confirm') }}</el-button>
        <el-button @click="upload.open = false">{{ $t('common_field.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAsset, getAsset, delAsset, addAsset, updateAsset, exportAsset, importTemplate } from "@/api/asset/asset";
import { getToken } from '@/utils/auth'
export default {
  name: "Asset",
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
      // 资产表表格数据
      assetList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assetName: null,
        assetCode: null,
        location: null,
        costCenter: null
      },
      //显隐列
      columns: [
        { key: 0, label: '123', visible: true },
      ],
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/asset/importData"
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
    /** 查询资产表列表 */
    getList() {
      this.loading = true;
      listAsset(this.queryParams).then(response => {
        this.assetList = response.rows;
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
        assetId: null,
        assetName: null,
        assetCode: null,
        financialAssetCode: null,
        responsiblePersonNo: null,
        category: null,
        assetStatus: "0",
        factoryNo: null,
        standard: null,
        measure: null,
        buyer: null,
        buyDate: null,
        netWorth: null,
        warranty: null,
        canUseMonths: null,
        canUseYears: null,
        capitalizationDate: null,
        monetaryUnit: null,
        companyCode: null,
        companyName: null,
        location: null,
        provider: null,
        amount: null,
        brand: null,
        costCenter: null,
        costCenterName: null,
        manageDept: null,
        contractNo: null,
        proposer: null,
        usageScenario: null,
        comment: null,
        createBy: null,
        createTime: null,
        updateBy: null,
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
      this.ids = selection.map(item => item.assetId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加资产表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const assetId = row.assetId || this.ids
      getAsset(assetId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改资产表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.assetId != null) {
            updateAsset(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAsset(this.form).then(response => {
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
      const assetIds = row.assetId || this.ids;
      this.$modal.confirm('是否确认删除资产表编号为"' + assetIds + '"的数据项？').then(function () {
        return delAsset(assetIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = this.$t('user.import');
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then(response => {
        this.$download.name(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, this.$t('common_field.result'), { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有资产表数据项？').then(() => {
        this.exportLoading = true;
        return exportAsset(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => { });
    }
  }
};
</script>
