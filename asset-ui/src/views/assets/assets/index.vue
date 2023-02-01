<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产编码" prop="assetName">
        <el-input v-model="queryParams.assetName" placeholder="请输入资产编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="资产类型" prop="assetType">
        <el-select v-model="queryParams.assetType" placeholder="请选择资产类型"  clearable multiple size="small">
          <el-option
            v-for="dict in dict.type.asset_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产分类" prop="assetCategory">
        <el-select v-model="queryParams.assetCategory" placeholder="请选择资产分类" clearable multiple size="small">
          <el-option
            v-for="dict in dict.type.asset_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资产名称" prop="assetName">
        <el-input v-model="queryParams.assetName" placeholder="请输入资产名称" clearable multiple size="small"
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
      <el-form-item label="保管部门" prop="responsiblePersonDept">
        <el-select v-model="queryParams.responsiblePersonDept" placeholder="请选择保管部门" clearable multiple size="small">
          <el-option
            v-for="dict in dict.type.common_dept"
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
      <el-form-item label="资产化日期">
        <div class="block">
          <el-date-picker size="small" v-model="capitalizationDateRange"
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
    </el-row>

    <el-table v-loading="loading" :data="assetList" @selection-change="handleSelectionChange" tooltip-effect="light">
    <!--  <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" type="index" align="center" />
      <el-table-column label="资产状态" align="center" prop="assetStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_status" :value="scope.row.assetStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="资产编码" align="center" prop="assetCode">
        <template slot-scope="scope">
          <el-link :underline="false" type="primary" @click="getDetail(scope.row)">{{ scope.row.assetCode }}</el-link>
        </template>
      </el-table-column>
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
      <el-table-column label="资产大类" align="center" prop="assetCategory">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_category" :value="scope.row.assetCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="资产名称" align="center" prop="assetName" />
      <el-table-column label="规格型号" align="center" prop="standard" />
      <el-table-column label="保管部门" align="center" prop="responsiblePersonDept" />
      <el-table-column label="保管人姓名" align="center" prop="responsiblePersonName" />
      <el-table-column label="所在位置" align="center" prop="currentLocation" />
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
            icon="el-icon-search"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['assets:manager:edit']"
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

  </div>
</template>

<script>

  import {listAssets} from "@/api/assets/assets";
  import {getDicts} from "@/api/system/dict/data";

  export default {
  name: "assets",
  dicts: ['asset_type', 'asset_category', 'asset_company', 'asset_location'
          , 'asset_fixed', 'asset_status'],
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
      capitalizationDateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assetCategory: null,
        assetCode: null,
        assetName: null,
        assetStatus: null,
        assetSubCategory: null,
        assetType: null,
        capitalizationStartDate: null,
        capitalizationEndDate: null,
        company: null,
        fixed: null,
        responsiblePersonDept: null
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
    /** 查询成熟度流程列表 */
    getList() {
      this.loading = true;
      listAssets(this.queryParams).then(response => {
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

