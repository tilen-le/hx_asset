<template>
  <div class="app-container" ref="container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <!-- <el-form-item label="组织范围" prop="dept">
        <dept-tree-select v-model="queryParams.dept" style="width:300px"></dept-tree-select>
      </el-form-item> -->
      <el-form-item label="统计类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择统计类型" size="mini" @change="refreshData">
          <el-option value="年">年</el-option>
          <el-option value="月">月</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="起止日期">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期" value-format="yyyy-MM-dd" size="mini" @change="refreshData">
        </el-date-picker>
      </el-form-item>
    </el-form>
    <el-container>
      <el-header height="200">
        <div ref="chart" style="height:200px"></div>
      </el-header>
      <el-main :height="tableHeight+30">
        <el-table :height="tableHeight" v-loading="loading" :data="countList">
          <el-table-column label="日期" align="center" prop="date" />
          <el-table-column label="盘点任务名称" align="center" prop="taskName" />
          <el-table-column label="已盘点" align="center" prop="isCount" />
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import echarts from 'echarts'
import DeptTreeSelect from "@/components/DeptTreeSelect/index"
import { inventoryCount, inventoryCountList } from "@/api/statistics/counting"
export default {
  components: { DeptTreeSelect },
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // dept: null,
        type: '月',// 年/月
      },
      dateRange: [],
      loading: false,
      //图表数据配置
      chart: null,
      option: {
        tooltip: {
          trigger: 'axis'
        },
        color: ['#91cc75', '#ee6666'],
        legend: {
          data: ['已盘点', '异常']
        },
        grid: {
          left: '3%',
          right: '3%',
          bottom: '0',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '已盘点',
            type: 'line',
            data: []
          },
          {
            name: '异常',
            type: 'line',
            data: []
          }
        ],

        graphic: {
          type: 'text',     // 类型：文本
          left: 'center',
          top: 'middle',
          silent: true,     // 不响应事件
          invisible: true,   // 有数据就隐藏
          style: {
            fill: '#9d9d9d',
            fontWeight: 'bold',
            text: '暂无数据',
            fontFamily: 'Microsoft YaHei',
            fontSize: '25px'
          }
        }
      },
      //表格数据
      tableHeight: 0,
      countList: [],
      total: 0
    }
  },
  mounted() {
    var firstDay = new Date();
    firstDay.setDate(1);
    firstDay.setMonth(0);
    this.dateRange = [this.parseTime(firstDay, '{y}-{m}-{d}'), this.parseTime(new Date(), '{y}-{m}-{d}')]
    this.$nextTick(() => {
      this.tableHeight = document.body.offsetHeight - 480;
      this.initChart()
      this.refreshData()
      // this.chart.showLoading({
      //   text: '暂无数据',
      //   showSpinner: false,    // 隐藏加载中的转圈动图
      //   textColor: '#9d9d9d',
      //   maskColor: 'rgba(255, 255, 255, 0.8)',
      //   fontSize: '25px',
      //   fontWeight: 'bold',
      //   fontFamily: 'Microsoft YaHei'
      // });
    })
    var _this = this
    window.onresize = function () {
      _this.tableHeight = document.body.offsetHeight - 480;
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart)
    },
    refreshData() {
      this.getinventoryCount();
      this.getList();
    },
    //获取图表数据
    getinventoryCount() {
      inventoryCount(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.option.xAxis.data = response.data.x
        // this.option.title={}
        this.option.series[0].data = response.data.isCount
        this.option.series[1].data = response.data.isExcept
        this.chart.setOption(this.option)
        // this.chart.hideLoading()
      })
    },
    //获取表格数据
    getList() {
      this.loading = true;
      inventoryCountList(this.addDateRange(this.queryParams, this.dateRange, "start_end")).then(response => {
        this.countList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
}
</script>

<style>

</style>
