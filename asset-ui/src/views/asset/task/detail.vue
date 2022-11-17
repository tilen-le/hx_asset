<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item v-for="item, index in queryProperty" :key="item.fieldKey" :label="item.fieldLabel"
        :prop="'domains.' + index + '.value'">
        <template v-if="!item.timeFormat && !item.dictType">
          <el-input v-model="queryParams.domains[index].value" :placeholder="'请输入' + item.fieldLabel" clearable
            size="small" @keyup.enter.native="handleQuery" />
        </template>
        <template v-if="item.dictType">
          <el-select v-model="queryParams.domains[index].value" clearable>
            <el-option v-for="dict in dict.type[item.dictType]" :key="dict.value" :label="dict.label"
              :value="dict.value" />
          </el-select>
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
    <div style="display:flex">
      <el-card :class="countingStatus === '' ? 'cardClickClass' : 'cardNoClickClass'" class="taskAmountCard"
        @click.native="statusSearch('')">
        固定资产总数
        <div class="card_text">{{ countResult.total }}</div>
      </el-card>
      <el-card :class="countingStatus === '1' ? 'cardClickClass' : 'cardNoClickClass'" class="taskAmountCard"
        @click.native="statusSearch('1')">
        已盘点资产总数
        <div class="card_text">{{ countResult.counted }}</div>
      </el-card>
      <el-card :class="countingStatus === '0' ? 'cardClickClass' : 'cardNoClickClass'" class="taskAmountCard"
        @click.native="statusSearch('0')">
        待盘点资产总数
        <div class="card_text">{{ countResult.notCounted }}</div>
      </el-card>
      <el-card :class="countingStatus === '2' ? 'cardClickClass' : 'cardNoClickClass'" class="taskAmountCard"
        @click.native="statusSearch('2')">
        盘点异常设备数目
        <div class="card_text">{{ countResult.abnormal }}</div>
      </el-card>
    </div>

    <el-table v-loading="loading" :data="taskList" tooltip-effect="light">
      <el-table-column v-for="item in tableProperty" :key="item.fieldKey" :label="item.fieldLabel" :prop="item.fieldKey"
        :width="item.width" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <!--<i class="el-icon-time"></i>-->
          <slot :name="item.fieldKey" :data="scope.row[item.fieldKey]" :row="scope.row" :index="scope.$index">
            <template v-if="!item.timeFormat && !item.dictType && item.fieldKey != 'assetCode'">
              <span>{{ scope.row[item.fieldKey] }}</span>
            </template>
            <template v-if="item.timeFormat">
              <span>{{ formatDate(scope.row[item.fieldKey], item.timeFormat) }}</span>
            </template>
            <template v-if="item.dictType">
              <dict-tag :options="dict.type[item.dictType]" :value="scope.row[item.fieldKey]" />
            </template>
            <template v-if="item.fieldKey == 'assetCode'">
              <el-popover trigger="hover" placement="top" popper-class="assetPopover">
                <div style="width:calc(30vw)" v-loading="loadAsset">
                  <el-descriptions title="资产详情" :column="2" size="mini" border>
                    <el-descriptions-item label="资产名称">{{ assetData.assetName }}</el-descriptions-item>
                    <el-descriptions-item label="公司名称">{{ assetData.companyName }}</el-descriptions-item>
                    <el-descriptions-item label="存放地点">{{ assetData.location }}</el-descriptions-item>
                    <el-descriptions-item label="管理部门">{{ assetData.manageDept }}</el-descriptions-item>
                    <el-descriptions-item label="保管人">{{ assetData.responsiblePersonName }}</el-descriptions-item>
                    <el-descriptions-item label="保管部门">{{ assetData.responsiblePersonDept }}</el-descriptions-item>
                    <el-descriptions-item label="使用场景">{{ assetData.usageScenario }}</el-descriptions-item>
                    <el-descriptions-item label="规格型号">{{ assetData.standard }}</el-descriptions-item>
                  </el-descriptions>
                </div>
                <span slot="reference" style="color:rgb(140,197,255)" @mouseover="getAsset(scope.row.assetCode)">
                  {{ scope.row.assetCode }}
                </span>
              </el-popover>
            </template>
          </slot>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

  </div>
</template>

<script>
import { getAsset } from "@/api/asset/asset"
import { countRecord, listRecord, exportRecord } from "@/api/task/record";
import { listField } from "@/api/process/field";
export default {
  name: "taskDetail",
  dicts: [],
  data() {
    return {
      property: [],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 盘点任务表格数据
      taskList: [],
      // 查询参数
      queryParams: {
        domains: [],
        pageNum: 1,
        pageSize: 10,
      },
      countResult: {},
      countingStatus: null,
      // 资产详情
      loadAsset: true,
      assetData: {}
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
    statusSearch(status) {
      this.countingStatus = status;
      this.getList();
    },
    getQueryParams() {
      var queryParams = {};
      this.queryParams.domains.forEach(item => {
        if (item.value != "") {
          queryParams[item.prop] = item.value
        }
      })
      queryParams.processType = 100
      queryParams.pageNum = this.queryParams.pageNum
      queryParams.pageSize = this.queryParams.pageSize
      if (this.countingStatus)
        queryParams.countingStatus = this.countingStatus
      return queryParams;
    },
    /** 查询盘点任务列表 */
    getList() {
      var queryParams = this.getQueryParams()
      queryParams.taskCode = this.$route.params && this.$route.params.id
      this.loading = true;
      listRecord(queryParams).then(response => {
        this.taskList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      const taskCode = queryParams.taskCode
      countRecord({ taskCode: taskCode }).then(response => {
        this.countResult = response.data;
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
      this.countingStatus = null;
      this.handleQuery();
    },
    getAsset(assetCode) {
      this.loadAsset = true;
      getAsset(assetCode).then(response => {
        this.assetData = response.data;
        this.loadAsset = false;
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = getQueryParams();
      this.$modal.confirm("提示", "确认", "取消", "是否确认导出所有符合条件数据项？").then(() => {
        this.exportLoading = true;
        return exportRecord(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {
        this.exportLoading = false;
      });
    }
  }
};
</script>
<style >
.el-form-item__label {
  white-space: nowrap;
}

.assetPopover .el-descriptions-item__label {
  font-weight: bold !important;
  white-space: nowrap;
}
</style>
<style scoped>
.el-form-item{
  margin-bottom:0;
}
.taskAmountCard {
  width: 170px;
  height: 85px;
  margin: 8px;
  text-align: center;
  cursor: pointer;
  font-weight: bold;
}
.taskAmountCard:first-child {
  margin-left: 0;
}

.card_text {
  color: blue;
  margin-top: 8px;
  font-size: 15pt;
}

.cardClickClass {
  background: #88eeff;
}

.cardNoClickClass {
  background-color: rgb(236, 245, 255);
}
</style>
