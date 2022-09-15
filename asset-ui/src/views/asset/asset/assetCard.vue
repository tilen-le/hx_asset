<template>
  <div>
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
              <input v-else v-model="form[assetColumns[3*(index-1)+n-1].prop]" class="assetCardOnEdit" />
            </td>
          </template>
        </template>
      </tr>
    </table>
    <el-row style="text-align:center;">
      <el-button type="success" icon="el-icon-edit" size="mini" @click="onEdit=true">修改</el-button>
      <el-button type="primary" icon="el-icon-check" size="mini">保存</el-button>
      <el-button icon="el-icon-close" size="mini" @click="onEdit=false">取消</el-button>
    </el-row>
  </div>
</template>

<script>
//可改项
var EditableProps = ["assetName", "responsiblePersonCode", "assetStatus", "factoryNo", "location", "usageScenario", "comment"];
import { getAsset } from "@/api/asset/asset";
export default {
  dicts: ['asset_counting_status'],
  data() {
    return {
      assetColumns: [],
      form: {},
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
    console.log(this.assetColumns)
    if (this.assetColumns.length == 0) {
      this.$store.commit('SET_Redirect', true);
      this.$router.push({ path: "/asset/asset" })
    }
    var assetCode = this.$route.query.assetCode
    getAsset(assetCode).then(response => {
      this.form = response.data
    });
  },
  methods: {
    isOnEdit(index) {
      return EditableProps.indexOf(this.assetColumns[index].prop) > -1 && this.onEdit
    }
  }
}
</script>

<style scoped>
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
