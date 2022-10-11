<template>
  <div class="app-container">
    平台资产编码：{{assetCode}}
    <br />
    <span>该资产目前状态：{{assetStatus}}，此前已进行维修{{maintainCount}}次，改造{{transformCount}}次，外卖{{sellOutCount}}次</span>
    <el-timeline :reverse="true">
      <el-timeline-item v-for="item in timelineData" :key="item.id" :timestamp="item.createTime" placement="top">
        <el-row>
          <el-col :span="4">
            {{selectDictLabel(dict.type.ding_asset_process_type,item.processType)}}&nbsp;
          </el-col>
          <el-col :span="4">
            提交人：{{item.userName}}
          </el-col>
          <el-col :span="6">
            <el-button size="mini">详情</el-button>
            <el-button size="mini">附件</el-button>
          </el-col>
        </el-row>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script>
import { getLifeCycle } from "@/api/asset/asset";
export default {
  dicts: ["ding_asset_process_type"],
  data() {
    return {
      assetCode: "",
      assetStatus: "",
      maintainCount: 0,
      transformCount: 0,
      sellOutCount: 0,
      timelineData: []
    }
  },
  created() {
    this.assetCode = this.$route.query.assetCode
    if (this.assetCode) {
      this.handleQuery()
    }
  },
  methods: {
    handleQuery() {
      getLifeCycle(this.assetCode).then((response) => {
        this.assetStatus = response.data.assetStatus
        this.maintainCount = response.data.maintainCount
        this.transformCount = response.data.transformCount
        this.sellOutCount = response.data.sellOutCount
        this.timelineData = response.data.assetProcessList
      })
    }
  }
}
</script>

<style>

</style>
