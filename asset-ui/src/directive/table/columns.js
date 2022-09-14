import Vue from "vue";
export default {
  bind: function (el, binding, vnode) {
    var columns = [];
    Vue.nextTick(() => {
      var thList = el.querySelectorAll("th .cell");
      for (var i = 0; i < thList.length; i++) {
        if (
          thList[i].innerHTML.indexOf("el-checkbox") < 0 &&
          thList[i].innerHTML != "操作"
        ) {
          columns.push({
            key: i,
            label: thList[i].innerHTML,
            className: thList[i].parentElement.className.match(
              /el-table_[0-9]*_column_[0-9]*/
            )[0],
            visible: true,
          });
        }
      }
      vnode.context.$data.columns = columns;
    });
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
