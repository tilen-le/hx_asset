<template>
  <div>
    <el-row class="assetOptRow">
      <el-button v-show="!onEdit" type="success" icon="el-icon-edit" size="mini" @click="handleUpdate"
        v-hasPermi="['asset:asset:edit']">修改</el-button>
      <el-button v-show="onEdit" type="primary" icon="el-icon-check" size="mini" @click="handleSubmit"
        v-hasPermi="['asset:asset:edit']">保存</el-button>
      <el-button v-show="onEdit" icon="el-icon-close" size="mini" @click="handleCancel"
        v-hasPermi="['asset:asset:edit']">取消</el-button>
    </el-row>
    <table class="tab-box" border="1">
      <tr v-for="index in columns" :key="index">
        <template v-for="n in 3">
          <el-col v-if="3 * (index - 1) + n - 1 < assetColumns.length"
            :key="'th' + assetColumns[3 * (index - 1) + n - 1].prop" :span="8" style="display: flex">
            <td class="asset-th">
              {{ assetColumns[3 * (index - 1) + n - 1].label }}
            </td>
            <td :key="'td' + assetColumns[3 * (index - 1) + n - 1].prop">
              <span v-if="!isOnEdit(3 * (index - 1) + n - 1)">
                {{ form[assetColumns[3 * (index - 1) + n - 1].prop] }}
              </span>
              <template v-else>
                <el-input v-if="
                  assetColumns[3 * (index - 1) + n - 1].prop !=
                  'responsiblePersonName'
                " v-model="form[assetColumns[3 * (index - 1) + n - 1].prop]" style="background-color: inherit" />
                <el-select v-else v-model="form.responsiblePersonName" @change="handleChange" placeholder="请选择盘点人"
                  filterable style="width: 80%">
                  <el-option v-for="item in userOptions" :key="item.userName" :label="item.nickName"
                    :value="item.userName" />
                </el-select>
              </template>
            </td>
          </el-col>
        </template>
      </tr>
    </table>
  </div>
</template>

<script>
//可改项
var EditableProps = [
  "assetName",
  "responsiblePersonName",
  "assetStatus",
  "factoryNo",
  "location",
  "usageScenario",
  "comment",
];
import { getAsset, getLifeCycle, updateAsset } from "@/api/asset/asset";
import { allUser } from "@/api/system/user";
export default {
  dicts: ["asset_counting_status","ding_asset_process_type"],
  data() {
    return {
      assetColumns: [
        {
          label: "固定资产名称",
          prop: "assetName",
        },
        {
          label: "平台资产编号",
          prop: "assetCode",
        },
        {
          label: "财务资产编号",
          prop: "financialAssetCode",
        },
        {
          label: "保管人",
          prop: "responsiblePersonName",
        },
        {
          label: "保管人工号",
          prop: "responsiblePersonCode",
        },
        {
          label: "保管部门",
          prop: "responsiblePersonDept",
        },
        {
          label: "资产分类描述",
          prop: "category",
        },
        {
          label: "资产状态描述",
          prop: "assetStatus",
        },
        {
          label: "出厂编号",
          prop: "factoryNo",
        },
        {
          label: "规格型号",
          prop: "standard",
        },
        {
          label: "单位",
          prop: "measure",
        },
        {
          label: "采购人",
          prop: "buyer",
        },
        {
          label: "采购日期",
          prop: "buyDate",
        },
        {
          label: "资产总价值",
          prop: "totalValue",
        },
        {
          label: "净值",
          prop: "netWorth",
        },
        {
          label: "保修期（月）",
          prop: "warranty",
        },
        {
          label: "预计寿命（月）",
          prop: "canUseMonths",
        },
        {
          label: "预计寿命（年）",
          prop: "canUseYears",
        },
        {
          label: "资本化日期",
          prop: "capitalizationDate",
        },
        {
          label: "资产原值币制",
          prop: "monetaryUnit",
        },
        {
          label: "公司代码",
          prop: "companyCode",
        },
        {
          label: "公司代码描述",
          prop: "companyName",
        },
        {
          label: "存放地点",
          prop: "location",
        },
        {
          label: "供应商",
          prop: "provider",
        },
        {
          label: "数量",
          prop: "amount",
        },
        {
          label: "品牌",
          prop: "brand",
        },
        {
          label: "成本中心",
          prop: "costCenter",
        },
        {
          label: "成本中心描述",
          prop: "costCenterName",
        },
        {
          label: "管理部门描述",
          prop: "manageDept",
        },
        {
          label: "合同单号",
          prop: "contractNo",
        },
        {
          label: "申请人",
          prop: "proposer",
        },
        {
          label: "资产使用场景",
          prop: "usageScenario",
        },
        {
          label: "备注",
          prop: "comment",
        },
      ],
      form: {},
      userOptions: [],
      onEdit: false,
    };
  },
  computed: {
    columns() {
      return Math.ceil(this.assetColumns.length / 3);
    },
  },
  created() {
    this.refreshData();
    this.getLifeCycle();
  },
  methods: {
    getLifeCycle() {
      var assetCode = this.$route.query.assetCode;
      getLifeCycle(assetCode).then((response) => {
        console.log("getLifeCycle::::::")
        console.log(response);
      })
    },
    refreshData() {
      var assetCode = this.$route.query.assetCode;
      getAsset(assetCode).then((response) => {
        this.form = response.data;
      });
    },
    isOnEdit(index) {
      return (
        EditableProps.indexOf(this.assetColumns[index].prop) > -1 && this.onEdit
      );
    },
    getUsers() {
      if (this.userOptions.length == 0) {
        allUser().then((response) => {
          this.userOptions = response.data;
        });
      }
    },
    handleChange(value) {
      this.form.responsiblePersonCode = value;
    },
    handleUpdate() {
      this.onEdit = true;
      this.getUsers();
    },
    handleSubmit() {
      updateAsset(this.form).then((response) => {
        this.$modal.msgSuccess("修改成功");
        this.onEdit = false;
        this.refreshData();
      });
    },
    handleCancel() {
      this.refreshData();
      this.onEdit = false;
    },
  },
};
</script>

<style scoped>
.assetOptRow {
  width: 95%;
  margin: 10px auto;
  text-align: right;
}

.tab-box {
  width: 95%;
  margin: 20px auto;
  border-collapse: collapse;
  border-color: rgb(179, 216, 255);
}

.tab-box tr {
  display: flex;
}

.tab-box tr:nth-child(even) {
  background: #f1f1f1;
}

.tab-box td {
  flex: 1;
  /*让每个单元格宽度一样*/
  min-height: 35px;
  padding-left: 10px;
  display: flex;
  /*flex布局*/
  align-items: center;
  /*让单元格文字垂直居中*/
  color: #606266;
  overflow: auto;
}

.tab-box .asset-th {
  width: 200px;
  background-color: rgb(198, 226, 255);
}
</style>
