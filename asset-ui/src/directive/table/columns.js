/**
 * 生成columns用于表格显隐列
 * 控制表格显隐列
 */
export default {
  bind: function (el, binding, vnode) {
    var columns = [];
    var array = vnode.componentOptions.children;
    for (
      var i = array[0].componentOptions.propsData.type == "selection" ? 1 : 0;
      i < array.length;
      i++
    ) {
      const element = array[i];
      columns.push({
        key: i,
        label: element.componentOptions.propsData.label,
        prop: element.componentOptions.propsData.prop, //用于资产卡片
        className: element.elm.__vue__.columnId,
        visible: true,
      });
    }
    vnode.context.$data.columns = columns;
  },
  update(el, binding) {
    var columns = binding.value;
    for (let i = 0; i < columns.length; i++) {
      var doms = el.querySelectorAll("." + columns[i].className);
      for (let j = 0; j < doms.length; j++) {
        doms[j].style.display = columns[i].visible ? "table-cell" : "none";
      }
    }
  },
};
