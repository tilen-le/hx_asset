<template>
  <div class="app-main">
    <!--    <div>{{ $t('homepage.content') }}</div>-->
    <div class="backlog">
      <span class="head-text">待办</span>
      <el-tabs v-model="activeName" style="">
        <el-tab-pane v-if="checkPermi(['asset:log:custodyLogList'])" label="资产转移" name="assetTab">
          <Assetsbacklog ref="assetTab"></Assetsbacklog>
        </el-tab-pane>
        <!--        <el-tab-pane label="xxxxx" name="orderTab" v-if="checkPermi(['asset:log:workLogList'])">-->
        <!--          <div>暂无内容</div>-->
        <!--        </el-tab-pane>-->
        <!--        <el-tab-pane label="xxxx" name="operateTab" v-if="checkPermi(['asset:log:operationLogList'])">-->
        <!--          <div>暂无内容</div>-->
        <!--        </el-tab-pane>-->
      </el-tabs>
    </div>

    <div class="two"></div>

    <div class="three"></div>

    <div class="four"></div>

  </div>
</template>

<script>
import Assetsbacklog from "./assets/assets/assetsbacklog.vue";
import {checkPermi} from '@/utils/permission'

export default {
  name: "Index",
  components: {Assetsbacklog},
  data() {
    return {
      activeName: 'assetTab',
      tabRefresh: {
        assetTab: true,
        orderTab: false,
        operateTab: false
      },
    };
  },
  created() {
    this.loading = true;
    setTimeout(() => {
      if (this.$auth.hasPermi("asset:log:custodyLogList")) {
        this.getTabContent('assetTab');
        this.loading = false;
      } else if (this.$auth.hasPermi("asset:log:workLogList")) {
        this.getTabContent('orderTab');
      } else if (this.$auth.hasPermi("asset:log:operationLogList")) {
        this.getTabContent('operateTab');
      } else {
      }

    }, 500);

  },
  methods: {
    checkPermi,
    tabClick(tab, event) {
      this.getTabContent(tab.name);
    },
    getTabContent(tabName) {
      if (tabName == 'assetTab') {
        this.$refs.assetTab.getList();
      } else if (tabName == 'orderTab') {
        this.$refs.orderTab.getList();
      } else if (tabName == 'operateTab') {
        this.$refs.operateTab.getList();
      } else {
      }
    },
  },
};
</script>
<style scoped>
.backlog {
  height: 500px;
  width: 49%;
  float: left;
  margin-left: 18px;
  margin-top: 18px;
  box-shadow: #1e1e1e;
  background-color: #fbfbfc;
}

.two {
  height: 500px;
  width: 49%;
  float: right;
  margin-top: 18px;
  background-color: #fbfbfc;
}

.three {
  height: 500px;
  width: 49%;
  margin-left: 18px;
  margin-top: 18px;
  float: bottom;
  float: left;
  background-color: #fbfbfc;
}

.four {
  height: 500px;
  width: 49%;
  margin-top: 18px;
  float: bottom;
  float: right;
  background-color: #fbfbfc;
}

.app-main {
  background-color: #e8e8e8;
}

.head-text {
  position: absolute;
  padding-top: 20px;
  padding-left: 10px;
  font-size: xx-large;
  font-family: "微软雅黑 Light";
  font-weight: bold;
}
</style>

