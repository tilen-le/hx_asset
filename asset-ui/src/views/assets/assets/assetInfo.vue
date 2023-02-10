<template>
  <div class="app-container">
    <div class="divHead divInfo">

        <span class="head_title">资产编码：{{assetCode}}</span>
        <div style="display: inline-block;margin-left: 50px;"><dict-tag :options="dict.type.asset_status" :value="form.assetStatus"/></div>
      <div class="head_button">
        <el-button size="small" type="primary"  @click="transfer_dialog">操作1</el-button>
        <el-button size="small" type="primary" @click="transfer_dialog">操作2</el-button>
      </div>


    </div>

    <div class="divMiddle divInfo">
      <el-descriptions title="基础信息">
        <el-descriptions-item label="资产大类">{{ form.assetType }}</el-descriptions-item>
        <el-descriptions-item label="资产中类">{{ form.assetCategory }}</el-descriptions-item>
        <el-descriptions-item label="资产小类">{{ form.assetSubCategory }}</el-descriptions-item>
        <el-descriptions-item label="资产名称">{{ form.assetName }}</el-descriptions-item>
        <el-descriptions-item label="规格型号">{{ form.standard }}</el-descriptions-item>
        <el-descriptions-item label="资产状态"></el-descriptions-item>
        <el-descriptions-item label="资产保管人"></el-descriptions-item>
        <el-descriptions-item label="资产保管部门"></el-descriptions-item>
        <el-descriptions-item label="所在位置"></el-descriptions-item>
        <el-descriptions-item label="所属公司"></el-descriptions-item>
        <el-descriptions-item label="资产管理员"></el-descriptions-item>
        <el-descriptions-item label="资产管理部门"></el-descriptions-item>
        <el-descriptions-item label="成本中心"></el-descriptions-item>
        <el-descriptions-item label="资产原值(含税)"></el-descriptions-item>
        <el-descriptions-item label="资产净值"></el-descriptions-item>
        <el-descriptions-item label="资产币制"></el-descriptions-item>
        <el-descriptions-item label="资产化日期"></el-descriptions-item>
        <el-descriptions-item label="保修期"></el-descriptions-item>
        <el-descriptions-item label="供应商"></el-descriptions-item>
        <el-descriptions-item label="出厂编码"></el-descriptions-item>
        <el-descriptions-item label="采购单号"></el-descriptions-item>
        <el-descriptions-item label="入库日期"></el-descriptions-item>
        <el-descriptions-item label="备注"></el-descriptions-item>


      </el-descriptions>
    </div>


    <div class="divBottom divInfo">
      <el-tabs v-model="activeName" type="card" @tab-click="tabClick">
        <el-tab-pane label="保管记录" name="belongTab">

        </el-tab-pane>
        <el-tab-pane label="工单记录" name="orderTab">

        </el-tab-pane>
        <el-tab-pane label="操作日志" name="third">

        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>


import {getInfo} from "@/api/assets/assets";

export default {
  name: 'assetInfo',
  dicts: ['asset_status'],
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      // 遮罩层
      loading: true,
      // 详情信息
      form: {},
      required_rule: [{required: true, message: "此项必填", trigger: 'blur'}],
      interrupt_person: false,
      user_list: [],
      assetCode: '',
      activeName: 'belongTab'
    }
  },
  created() {
    this.loading = true
    const assetCode = this.$route.params && this.$route.params.assetCode;
    if (assetCode) {
      this.assetCode = assetCode;
      getInfo(this.assetCode).then((response) => {
        this.form = response.data
        this.loading = false
      })
    }
  },
  methods: {
    tabClick(tab, event) {
      console.log(tab, event);
    }
  }
}
</script>


<style scoped>
  .divInfo{
    margin: 5px;
    padding: 5px 10px 5px 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .divHead {
    height: 6vh;
    display: flex;
    align-items: center;
  }


  .head_title {
    font-size: 20px;
  }
  .head_button {
    position: absolute;
    right: 50px;
  }




  .divMiddle {
    height: 40vh;
  }
  .divBottom {
    height: 38vh;
  }




</style>
