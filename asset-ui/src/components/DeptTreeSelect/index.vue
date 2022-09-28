<template>
  <treeselect v-model="inValue" :options="deptOptions" :show-count="true" placeholder="请选择盘点组织"
    @click.native="getTreeSelect" />
</template>

<script>
import { treeselect } from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "deptTreeSelect",
  props: {
    value: {},
  },
  components: { Treeselect },
  data() {
    return {
      inValue: this.value,
      // 部门树选项
      deptOptions: [],
    }
  },
  created() {
    if(this.value){
      this.getTreeSelect()
    }
  },
  watch: {
    val() {
      this.inValue = this.value
    },
    inValue() {
      this.$emit('input', this.inValue)
    }
  },
  methods: {
    /** 查询部门下拉树结构 */
    getTreeSelect() {
      if (this.deptOptions.length == 0) {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      }
    },
  }

}
</script>

<style>

</style>
