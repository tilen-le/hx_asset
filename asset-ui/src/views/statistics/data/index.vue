<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="组织范围" prop="dept">
        <dept-tree-select v-model="queryParams.dept" style="width:300px"></dept-tree-select>
      </el-form-item>
      <el-form-item label="统计类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择统计类型" @change="refreshData">
          <el-option value="年">年</el-option>
          <el-option value="月">月</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="起止日期">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期" value-format="yyyy-MM-dd" @change="refreshData">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button @click="getAssetCount">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row class="statistics-cards">
      <div v-if="cardData.length ==0" style="text-align:center;width:100%">暂无数据</div>
      <el-card v-for="item in cardData" :key="item.name">
        {{item.name}}<br />{{item.value}}
      </el-card>
    </el-row>

    <el-row>按类别</el-row>
    <el-row>
      <el-col :span="8">
        <line-chart :chartData="categoryNumCount" height="250px"></line-chart>
      </el-col>
      <el-col :span="8">
        <line-chart :chartData="netWorthCategoryCount" height="250px"></line-chart>
      </el-col>
      <el-col :span="8">
        <line-chart :chartData="totalValueCategoryCount" height="250px"></line-chart>
      </el-col>
    </el-row>
    <el-row>按部门</el-row>
    <el-row>
      <el-col :span="8">
        <line-chart :chartData="deptCount" height="250px"></line-chart>
      </el-col>
      <el-col :span="8">
        <line-chart :chartData="deptNetWorth" height="250px"></line-chart>
      </el-col>
      <el-col :span="8">
        <line-chart :chartData="deptTotalValue" height="250px"></line-chart>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8">
        <line-chart :chartData="pieOptions" height="250px"></line-chart>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// import echarts from 'echarts'
import DeptTreeSelect from "@/components/DeptTreeSelect/index"
import LineChart from "@/views/dashboard/LineChart.vue"
import { assetCount, assetCountByCategory, assetCountByDept } from "@/api/statistics/data.js"
export default {
  components: { DeptTreeSelect, LineChart },
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // dept: null,
        type: '月',// 年/月
      },
      dateRange: [],
      cardData: [],
      chart: null,
      pieOptions: {},
      categoryNumCount: {},
      netWorthCategoryCount: {},
      totalValueCategoryCount: {},
      deptCount: {},
      deptNetWorth: {},
      deptTotalValue: {}
    }
  },
  mounted() { this.getAssetCount() },
  methods: {
    refreshData() { },
    getAssetCount() {
      assetCount(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.cardData = response.data.main
        this.pieOptions = this.getPieOptions(response.data.pie)
      })
      assetCountByCategory(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.categoryNumCount = this.getOptions('数量', response.data.categoryNumCount)
        this.netWorthCategoryCount = this.getOptions('净值', response.data.netWorthCategoryCount)
        this.totalValueCategoryCount = this.getOptions('原值', response.data.totalValueCategoryCount)
      })
      assetCountByDept(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        var deptname = [];
        var totalNum = [];
        var totalValue = [];
        var totalNetWorth = [];
        response.data.forEach(item => {
          item.forEach(i => {
            switch (i.name) {
              case "deptname": deptname.push(i.value);
                break;
              case "totalNum": totalNum.push(i.value);
                break;
              case "totalNetWorth": totalNetWorth.push(i.value);
                break;
              case "totalValue": totalValue.push(i.value);
                break;
              default:
                throw new Exception("数据错误")
            }
          })
        });
        this.deptCount = this.getOptions2("数量", deptname, totalNum);
        this.deptNetWorth = this.getOptions2("净值", deptname, totalNetWorth);
        this.deptTotalValue = this.getOptions2("原值", deptname, totalValue);
      })
    },
    getOptions(title, data) {
      var xData = []
      var yData = []
      for (let i = 0; i < data.length; i++) {
        const item = data[i];
        xData.push(item.name)
        yData.push(item.value)
      }
      console.log(xData)
      var option = {
        title: { text: title },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: xData,
          axisLabel: {
            rotate: 30
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: yData,
            type: 'bar'
          }
        ]
      };
      return option
    },
    getOptions2(title, xData, yData) {
      var option = {
        title: { text: title },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: xData,
          axisLabel: {
            rotate: 30
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: yData,
            type: 'bar'
          }
        ]
      };
      return option
    },
    getPieOptions(data) {
      return {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: data,
          }
        ]
      };
    }
  }
}
</script>

<style>
.statistics-cards {
  display: flex;
  padding: 0 60px;
}

.statistics-cards .el-card {
  width: 13%;
  margin: 0 3px;
  text-align: center;
  background: rgb(64, 158, 255);
  color: white;
}
</style>
