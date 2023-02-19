<template>
  <div>
<!--    height="calc(38vh - 130px)"-->
    <el-table v-loading="loading" :data="operationLogList" tooltip-effect="light" height="230px">
      <el-table-column label="序号" type="index" align="center"/>

      <el-table-column label="操作人" align="center" prop="createBy" />
      <el-table-column label="操作类型" align="center" prop="processType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.asset_process" :value="scope.row.processType" />
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="searchInfo(scope.row)"
          >查看详情</el-button>
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

    <el-dialog title="详情" :visible.sync="dialog_open" width="1200px;" append-to-body>
      <el-descriptions title="表单信息" size="medium" border :column="3" v-if="processInfo != null" style="margin-bottom: 25px;">

        <el-descriptions-item v-for="(item,index) in processInfo.variableList" :label="item.fieldLabel">
          {{item.fieldValue}}
        </el-descriptions-item>





      </el-descriptions>
      <el-descriptions title="资产信息" size="small" border :column="3" >
        <el-descriptions-item label="资产大类"></el-descriptions-item>
        <el-descriptions-item label="资产中类">{{ historyInfo.assetCategory }}</el-descriptions-item>
        <el-descriptions-item label="资产小类">{{ historyInfo.assetSubCategory }}</el-descriptions-item>
        <el-descriptions-item label="资产名称">{{ historyInfo.assetName }}</el-descriptions-item>
        <el-descriptions-item label="规格型号">{{ historyInfo.standard }}</el-descriptions-item>
        <el-descriptions-item label="资产状态">
          <dict-tag :options="dict.type.asset_status" :value="historyInfo.assetStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="转固状态">
          <dict-tag :options="dict.type.asset_fixed" :value="historyInfo.fixed"/>
        </el-descriptions-item>
        <el-descriptions-item label="资产保管人">{{ historyInfo.responsiblePersonName }}</el-descriptions-item>
        <el-descriptions-item label="资产保管部门">{{ historyInfo.responsiblePersonDept }}</el-descriptions-item>
        <el-descriptions-item label="所在位置">{{ historyInfo.currentLocation }}</el-descriptions-item>
        <el-descriptions-item label="所属公司">
          <dict-tag :options="dict.type.asset_company" :value="historyInfo.company"/>
        </el-descriptions-item>
        <el-descriptions-item label="资产管理员">{{ historyInfo.assetManager }}</el-descriptions-item>
        <el-descriptions-item label="资产管理部门">{{ historyInfo.assetManagementDept }}</el-descriptions-item>
        <el-descriptions-item label="成本中心">{{ historyInfo.costCenterName }}</el-descriptions-item>
        <el-descriptions-item label="资产原值(含税)">{{ historyInfo.originalValue }}</el-descriptions-item>
        <el-descriptions-item label="资产净值">{{ historyInfo.netValue }}</el-descriptions-item>
        <el-descriptions-item label="资产币制">{{ historyInfo.monetaryUnit }}</el-descriptions-item>
        <el-descriptions-item label="资产化日期">{{ historyInfo.capitalizationDate }}</el-descriptions-item>
        <el-descriptions-item label="保修期">{{ historyInfo.warranty }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ historyInfo.providerName }}</el-descriptions-item>
        <el-descriptions-item label="出厂编码">{{ historyInfo.factoryNo }}</el-descriptions-item>
        <el-descriptions-item label="采购单号">{{ historyInfo.purchaseOrderNo }}</el-descriptions-item>
        <el-descriptions-item label="资产卡片编号">{{ historyInfo.sapCode }}</el-descriptions-item>
        <el-descriptions-item label="入库日期">{{ historyInfo.storageDate }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ historyInfo.comment }}</el-descriptions-item>

      </el-descriptions>
    </el-dialog>

  </div>

</template>

<script>
  import {operationLogList, getLogDetail} from "@/api/assets/assets";

  export default {
    props: {
      assetCode: {
        type: String
      }
    },
    dicts: ['asset_process','asset_status', 'asset_company',  'asset_fixed'],
    data() {
      return {
        // 遮罩层
        loading: false,
        total: 0,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assetCode: '',
        },
        operationLogList: [],
        dialog_open: false,
        historyInfo: {},
        processInfo: {},
        detail: {}
      };
    },
/*    created() {
      this.loading = true
      this.queryParams.assetCode = this.assetCode
      this.getList();
    },*/
    methods: {
      getList() {
        this.loading = true;
        this.queryParams.assetCode = this.assetCode
        operationLogList(this.queryParams).then(response => {
          this.operationLogList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      searchInfo(row) {
        let id = row.id
        getLogDetail(id).then((response) => {
          this.detail = response.data
          this.historyInfo = this.detail.updateLog
          this.processInfo = this.detail.processLog
          this.dialog_open = true;
        })
        // this.$modal.msgWarning(row.id + "功能尚未设计开发...");
      }
    }
  };
</script>
