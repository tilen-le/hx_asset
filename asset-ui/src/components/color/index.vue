<template>
  <el-select ref="colorSelect" placeholder="" v-model="myColor" style="width: 100%" @change="handleChange" class="color-select">
    <el-option
      class="color"
      v-for="item in colorList"
      :key="item.value"
      label=" "
      :value="item.value"
      v-html="'<div style=background-color:'+ item.label+';width:' +'100%'+';height:'+'90%'+'></div>'">
    </el-option>
  </el-select>
</template>

<script>
  import { updatePriority } from '@/api/livoltek/support'

  export default {
    name: 'colorSelect',
    //允许一个自定义组件在使用 v-model 时定制 prop 和 event。默认情况下，一个组件上的 v-model 会把 value 用作 prop 且把 input 用作 event，但是一些输入类型比如单选框和复选框按钮可能想使用 value prop 来达到不同的目的。使用 model 选项可以回避这些情况产生的冲突。
    model: {
      prop: 'color',
      event: 'update'
    },
    props: {
      //颜色数组
      colorList: {
        type: Array,
        default: () => {
          return ['#FFC0CB', '#DB7093', '#FF1493', '#DC143C']
        }
      },
      //父组件绑定的值
      color: {
        type: String,
        default: undefined
      },
      id: {
        type: String,
        default: undefined
      }
    },
    data() {
      return {
        myColor: undefined
      }
    },
    methods: {
      //设置颜色选择框中颜色
      setSelectColor(color) {
        let colorLabel
        for (let i = 0; i < this.colorList.length; i++) {
          if (color == this.colorList[i].value) {
            colorLabel = this.colorList[i].label
            break
          }
        }
        //通过操作dom节点改变样式
        this.$nextTick(() => {
          let dom = this.$refs.colorSelect
          if (dom) {
            dom = dom.$el.children[0]
            let inputDom = dom.querySelectorAll('.el-input__inner')
            let icon = dom.querySelectorAll('.el-input__icon')
            inputDom[0].style['background-color'] = colorLabel
            icon[0].style['color'] = 'black'
          }
        })
      },
      handleChange(val) {
        const form = { id: this.id, remark: val }
        updatePriority(form).then(response => {
          this.setSelectColor(val)
          //触发update事件更新父组件绑定值
          this.$emit('update', val)
        })
      }
    },
    created() {
      if (this.color && this.color.length > 0) {
        this.myColor = this.color
        this.setSelectColor(this.color)
      }
    },
    watch: {
      'color': function(val) {
        this.setSelectColor(val)
      }
    }
  }
</script>

<style>
  .color {
    padding: 0 2px !important;
  }
  .color-select .el-input__suffix-inner {
    display: none;
  }

</style>
