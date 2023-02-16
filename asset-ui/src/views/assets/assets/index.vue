<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产编码" prop="assetCode">
        <el-input v-model="queryParams.assetCode" placeholder="请输入资产编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="资产大类" prop="assetType">
        <el-select v-model="queryParams.assetType" placeholder="请选择资产大类" clearable size="small"  @change="typeChange">
          <el-option
            v-for="dict in asset_types"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产中类" prop="assetCategory">
        <el-select v-model="queryParams.assetCategory" placeholder="请选择资产中类" clearable size="small" @change="categoryChange">
          <el-option
            v-for="dict in asset_category_options"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产小类" prop="assetSubCategory">
        <el-select v-model="queryParams.assetSubCategory" placeholder="请选择资产小类" clearable multiple size="small">
          <el-option
            v-for="dict in asset_sub_category_options"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产名称" prop="assetName">
        <el-input v-model="queryParams.assetName" placeholder="请输入资产名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="资产状态" prop="assetStatus">
        <el-select v-model="queryParams.assetStatus" placeholder="请选择资产状态" clearable multiple size="small">
          <el-option
            v-for="dict in dict.type.asset_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属公司" prop="company">
        <el-select v-model="queryParams.company" placeholder="请选择所属公司" clearable multiple size="small">
          <el-option
            v-for="dict in dict.type.asset_company"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="转固状态" prop="fixed">
        <el-select v-model="queryParams.fixed" placeholder="请选择转固状态" clearable multiple size="small">
          <el-option
            v-for="dict in dict.type.asset_fixed"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产保管部门" prop="responsiblePersonDept" label-width="102px">
        <treeselect style="width:215px;" v-model="queryParams.responsiblePersonDept" :options="deptOptions" :normalizer="normalizer"
                    :show-count="true" placeholder="请选择资产保管部门"/>
      </el-form-item>
      <el-form-item label="资产化日期" prop="capitalizationDateRange" label-width="82px">
        <div class="block">
          <el-date-picker size="small" v-model="queryParams.capitalizationDateRange"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期"
                          type="daterange" value-format="yyyy-MM-dd" range-separator="-">
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading"
                           @click="handleExport" v-hasPermi="['asset:asset:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini"
          @click="handleImport" v-hasPermi="['asset:asset:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="assetList" tooltip-effect="light" height="calc(50vh)">
    <!--  <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" type="index" align="center" />
      <el-table-column label="资产状态" align="center" prop="assetStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_status" :value="scope.row.assetStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="资产编码" align="center" prop="assetCode" width="150px">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="goDetail(scope.row)">{{ scope.row.assetCode }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="资产大类" align="center" prop="assetType" :show-overflow-tooltip="true"/>
      <el-table-column label="资产中类" align="center" prop="assetCategory" />
      <el-table-column label="资产小类" align="center" prop="assetSubCategory" />
      <el-table-column label="资产名称" align="center" prop="assetName" />
      <el-table-column label="规格型号" align="center" prop="standard" :show-overflow-tooltip="true"/>
      <el-table-column label="资产保管人" align="center" prop="responsiblePersonName" />
      <el-table-column label="资产保管部门" align="center" prop="responsiblePersonDept" />
      <el-table-column label="所在位置" align="center" prop="currentLocation" :show-overflow-tooltip="true" />
      <el-table-column label="转固状态" align="center" prop="fixed">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_fixed" :value="scope.row.fixed"/>
        </template>
      </el-table-column>
      <el-table-column label="资产化日期" align="center" prop="capitalizationDate">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.capitalizationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="goDetail(scope.row)"
            v-hasPermi="['asset:asset:export']"
          >查看</el-button>
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
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text"><em>将文件拖到此处,或点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>{{$t('common_field.upload_format_excel')}}</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确认</el-button>
        <el-button @click="upload.open = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import {exportData, listAssets, importTemplate } from "@/api/assets/assets";
  import {getAssetTypeTree} from "@/api/assets/common";
  import { childTree } from '@/api/system/dept'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import { getToken } from '@/utils/auth'

  export default {
  name: "assets",
  dicts: ['asset_company', 'asset_fixed', 'asset_status'],
  components: { Treeselect },
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
      assetList: [],
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      deptOptions: [],
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assetCode: null,
        assetName: null,
        assetType: null,
        assetCategory: null,
        assetSubCategory: null,
        assetStatus: null,
        capitalizationDateRange: [],
        company: null,
        fixed: null,
        responsiblePersonDept: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      asset_type_tree: null,
      asset_types: [],
      asset_category_options: [],
      asset_sub_category_options: [],
    };
  },
  created() {
    this.getList();
    this.getAssetTree();
    this.getChildDeptTree();
  },
  methods: {
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = '导入';
      this.upload.open = true;
    },
    getList() {
      this.loading = true;
      listAssets(this.addDateRange(this.queryParams, this.queryParams.capitalizationDateRange,"customize","capitalizationStartDate","capitalizationEndDate"))
        .then(response => {
          this.assetList = response.rows;
          this.total = response.total;
          this.loading = false;
      });
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
    getAssetTree() {
      getAssetTypeTree().then(response => {
        const tree = response.data;
        this.asset_type_tree = tree;
        for (const item of tree) {
          const it = {
            "value": item.code,
            "label": item.description,
            "child": item.child,
            "raw": {
              "listClass": ""
            }
          }
          this.asset_types.push(it);
        }
      });
    },
    typeChange(value) {
      this.queryParams.assetCategory = null;
      this.queryParams.assetSubCategory = null;
      this.asset_category_options = [];
      this.asset_sub_category_options = [];
      this.getAssetCategory(value)
    },
    categoryChange(value) {
      this.queryParams.assetSubCategory = null;
      this.asset_sub_category_options = [];
      this.getAssetSubCategory(value)
    },
    getAssetCategory(code) {
      const tree = this.asset_types;
      for (const item of tree) {
        if (code == item.value) {
          for (const category of item.child) {
            const it = {
              "value": category.code,
              "label": category.description,
              "child": category.child,
            }
            this.asset_category_options.push(it);
          }
          break;
        }
      }
    },
    getAssetSubCategory(code) {
      const tree = this.asset_category_options;
      for (const item of tree) {
        if (code == item.value) {
          for (const subCategory of item.child) {
            const it = {
              "value": subCategory.code,
              "label": subCategory.description,
            }
            this.asset_sub_category_options.push(it);
          }
          break;
        }
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.addDateRange(this.queryParams, this.queryParams.capitalizationDateRange,"customize","capitalizationStartDate","capitalizationEndDate");
      this.$modal.confirm("提示", "确定", "取消", "是否确认导出所有符合条件数据项?").then(() => {
        this.exportLoading = true;
        return exportData(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.label,
        children: node.children
      }
    },
    getChildDeptTree() {
      childTree().then(response => {
        this.deptOptions = response.data
      })
    },
    goDetail(row) {
      this.$router.push('/asset/assetInfo/' + row.assetCode)
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
      this.$alert(response.msg, '结果', { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    importTemplate() {
      importTemplate().then(response => {
        this.$download.name(response.msg);
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

