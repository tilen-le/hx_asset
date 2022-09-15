<template>
  <div>
    <el-row class="assetOptRow">
      <el-button v-show="!onEdit" type="success" icon="el-icon-edit" size="mini" @click="handleUpdate">修改</el-button>
      <el-button v-show="onEdit" type="primary" icon="el-icon-check" size="mini" @click="handleSubmit">保存</el-button>
      <el-button v-show="onEdit" icon="el-icon-close" size="mini" @click="handleCancel">取消</el-button>
    </el-row>
    <table class="tab-box" border="1">
      <tr v-for="index in columns" :key="index">
        <template v-for="n in 3">
          <template v-if="3*(index-1)+n-1<assetColumns.length">
            <td class='asset-th' :key="'th'+assetColumns[3*(index-1)+n-1].prop">
              {{assetColumns[3*(index-1)+n-1].label}}
            </td>
            <td :key="'td'+assetColumns[3*(index-1)+n-1].prop">
              <span v-if="!isOnEdit(3*(index-1)+n-1)">
                {{form[assetColumns[3*(index-1)+n-1].prop]}}
              </span>
              <template v-else>
                <input v-if="assetColumns[3*(index-1)+n-1].prop!='responsiblePersonName'"
                  v-model="form[assetColumns[3*(index-1)+n-1].prop]" size="mini" />
                <el-select v-else v-model="form.responsiblePersonName" @change="handleChange" placeholder="请选择盘点人"
                  filterable style="width: 80%;">
                  <el-option v-for="item in userOptions" :key="item.userName" :label="item.nickName"
                    :value="item.userName" />
                </el-select>
              </template>
            </td>
          </template>
        </template>
      </tr>
    </table>
  </div>
</template>

<script>
//可改项
var EditableProps = ["assetName", "responsiblePersonName", "assetStatus", "factoryNo", "location", "usageScenario", "comment"];
import { getAsset, updateAsset } from "@/api/asset/asset";
import { allUser } from '@/api/system/user'
export default {
  dicts: ['asset_counting_status'],
  data() {
    return {
      assetColumns: [],
      form: {},
      userOptions: [],
      onEdit: false,
    }
  },
  computed: {
    columns() {
      return Math.ceil(this.assetColumns.length / 3)
    }
  },
  created() {
    this.assetColumns = this.$store.state.assetCard.assetColumns
    for (let i = 0; i < this.assetColumns.length; i++) {
      const element = this.assetColumns[i];
      if (element.prop == 'inventoryStatus') {
        this.assetColumns.splice(i, 1)
      }
    }
    console.log(this.assetColumns)
    if (this.assetColumns.length == 0) {
      this.$store.commit('SET_Redirect', true);
      this.$router.push({ path: "/asset/asset" })
    }
    this.refreshData()
  },
  methods: {
    refreshData() {
      var assetCode = this.$route.query.assetCode
      getAsset(assetCode).then(response => {
        this.form = response.data
      });
    },
    isOnEdit(index) {
      return EditableProps.indexOf(this.assetColumns[index].prop) > -1 && this.onEdit
    },
    getUsers() {
      if (this.userOptions.length == 0) {
        allUser().then(response => {
          this.userOptions = response.data;
          console.log('this.userOptions::')
          console.log(this.userOptions)
        });
      }
    },
    handleChange(value) {
      console.log('handleChange:::::', value)
      this.form.responsiblePersonCode = value
    },
    handleUpdate() {
      this.onEdit = true;
      this.getUsers()
    },
    handleSubmit() {
      updateAsset(this.form).then(response => {
        this.$modal.msgSuccess("修改成功");
        this.onEdit = false;
        this.refreshData();
      });
    },
    handleCancel() {
      this.refreshData()
      this.onEdit = false;
    }
  }
}
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
}

.tab-box tr {
  display: flex;
}

.tab-box td {
  flex: 1;
  /*让每个单元格宽度一样*/
  min-height: 30px;
  padding-left: 10px;
  display: flex;
  /*flex布局*/
  align-items: center;
  /*让单元格文字垂直居中*/
  color: #606266;
}

.tab-box .asset-th {
  background-color: rgb(198, 226, 255);
}
</style>
