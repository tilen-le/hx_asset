<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="组织范围" prop="dept">
        <dept-tree-select v-model="queryParams.dept" style="width:300px"></dept-tree-select>
      </el-form-item>
      <el-form-item label="统计类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择统计类型">
          <el-option value="年">年</el-option>
          <el-option value="月">月</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="起止日期">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期" value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getAssetCount">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row class="statistics-cards">
      <div v-if="cardData.length ==0" style="text-align:center;width:100%">暂无数据</div>
      <el-card v-for="item in cardData" :key="item.name">
        {{item.name}}<br />{{numFixed(item.value,0) }}
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
        <line-chart :chartData="process_time" height="250px"></line-chart>
      </el-col>
      <el-col :span="8">
        <line-chart :chartData="process_category" height="250px"></line-chart>
      </el-col>
      <el-col :span="8">
        <line-chart :chartData="process_dept" height="250px"></line-chart>
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
import { numFixed } from "@/utils"
import {
  assetCount, assetCountByCategory, assetCountByDept, assetProcessTypeTimeNumCount,
  assetProcessTypeCategoryNumCount, assetProcessTypeDeptNumCount
} from "@/api/statistics/data"
export default {
  components: { DeptTreeSelect, LineChart },
  data() {
    return {
      queryParams: {
        dept: 20003060,//默认利沃得
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
      deptTotalValue: {},
      process_time: {},
      process_category: {},
      process_dept: {}
    }
  },
  mounted() {
    var firstDay = new Date();
    firstDay.setDate(1);
    firstDay.setMonth(0);
    this.dateRange = [this.parseTime(firstDay, '{y}-{m}-{d}'), this.parseTime(new Date(), '{y}-{m}-{d}')]
    this.getAssetCount()
  },
  methods: {
    numFixed: numFixed,
    //  获取各图表数据
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
        this.deptCount = this.getOptions("数量", response.data.numDeptCount);
        this.deptNetWorth = this.getOptions("净值", response.data.totalNetWorth);
        this.deptTotalValue = this.getOptions("原值", response.data.totalValueDeptCount);
      })
      assetProcessTypeTimeNumCount(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.process_time = this.getMultiLineOptions('title', response.data)
      })
      assetProcessTypeCategoryNumCount(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.process_category = this.getMultiLineOptions('title', response.data)
      })
      assetProcessTypeDeptNumCount(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.process_dept = this.getMultiLineOptions('title', response.data)
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
      var option = {
        title: { text: title },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: xData,
          axisLabel: {
            rotate: 10
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
    // getOptions2(title, xData, yData) {
    //   var option = {
    //     title: { text: title },
    //     tooltip: {
    //       trigger: 'axis'
    //     },
    //     grid: {
    //       left: '3%',
    //       right: '4%',
    //       bottom: '3%',
    //       containLabel: true
    //     },
    //     xAxis: {
    //       type: 'category',
    //       data: xData,
    //       axisLabel: {
    //         rotate: 10
    //       }
    //     },
    //     yAxis: {
    //       type: 'value'
    //     },
    //     series: [
    //       {
    //         data: yData,
    //         type: 'bar'
    //       }
    //     ]
    //   };
    //   return option
    // },
    getMultiLineOptions(title, data) {
      var option = {
        title: {
          //text: 'Stacked Line'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: []
      };
      for (let i = 0; i < data.length; i++) {
        const item = data[i];
        if (item.name == 'x') {
          option.xAxis.data = item.value;
        } else {
          option.series.push({
            name: item.name,
            type: 'line',
            data: item.value
          })
        }
      }
      return option;
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
