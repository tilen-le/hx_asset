<template>
  <div class="homepage">
    <div class="backlog">
      <div style="border-left:5px solid #0D6DEA; height: 40px;line-height: 50px;position: relative;margin-top: 10px">
        <span class="head-text">待办</span>
      </div>
      <el-tabs v-model="activeName" class="backlogContent" type="card">
        <el-tab-pane label="资产转移" name="transferTabTodo">
          <Assetsbacklog ref="transferTabTodo"></Assetsbacklog>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div class="two">
      <el-empty description="正在设计中。。。"></el-empty>
    </div>

    <div class="three">
      <el-empty description="正在设计中。。。"></el-empty>
    </div>

    <div class="four">
      <el-empty description="正在设计中。。。"></el-empty>
    </div>

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
      activeName: 'transferTabTodo'
    };
  },
  created() {
    this.loading = true;
    setTimeout(() => {
      this.getTabContent('transferTabTodo');
    }, 500);

  },
  methods: {
    checkPermi,
    tabClick(tab, event) {
      this.getTabContent(tab.name);
    },
    getTabContent(tabName) {
      if (tabName == 'transferTabTodo') {
        this.$refs.transferTabTodo.getList();
      }
    },
  },
};
</script>
<style scoped>
.backlog {
  height: 400px;
  width: 49%;
  float: left;
  margin-left: 18px;
  margin-top: 18px;
  box-shadow: #1e1e1e;
  background-color: #fbfbfc;
}

.backlogContent {
  position: relative;
  height: 400px;
  width: 95%;
  margin-left: 20px;
  margin-top: 20px;
}

.two {
  height: 400px;
  width: 49%;
  float: right;
  margin-top: 18px;
  background-color: #fbfbfc;
}

.three {
  height: 400px;
  width: 49%;
  margin-left: 18px;
  margin-top: 18px;
  float: bottom;
  float: left;
  background-color: #fbfbfc;
}

.four {
  height: 400px;
  width: 49%;
  margin-top: 18px;
  float: bottom;
  float: right;
  background-color: #fbfbfc;
}

.homepage {
  background-color: #e8e8e8;
}

.head-text {
  /*position: absolute;*/
  /*padding-top: 20px;*/
  padding-left: 10px;
  font-size: xx-large;
  font-family: "微软雅黑 Light";
  font-weight: bold;
  font-size: 20px;
}

</style>

