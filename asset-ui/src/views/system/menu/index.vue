<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('menu.name')" prop="menuName">
        <el-input
          v-model="queryParams.menuName"
          :placeholder="$t('common_field.fill') + $t('menu.name')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('common_field.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common_field.status')" clearable size="small">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('common_field.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('common_field.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:menu:add']"
        >{{$t('common_field.add')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >{{ $t('common_field.expand_collapse') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="menuName" :label="$t('menu.name')" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="icon" :label="$t('menu.icon')" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" :label="$t('common_field.sort')" width="60"></el-table-column>
      <el-table-column prop="perms" :label="$t('menu.perms')" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="component" :label="$t('menu.component')" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="status" :label="$t('common_field.status')" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common_field.create_time')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common_field.operate')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >{{ $t('common_field.update') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:menu:add']"
          >{{$t('common_field.add')}}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:menu:remove']"
          >{{$t('common_field.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" :label-width="$t('label_large_width')">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('menu.parent')">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                :placeholder="$t('common_field.select') + $t('menu.parent')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('common_field.type')" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">{{ $t('menu.dir') }}</el-radio>
                <el-radio label="C">{{ $t('menu.menu') }}</el-radio>
                <el-radio label="F">{{ $t('menu.button') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType != 'F'" :label="$t('menu.icon')">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.icon" :placeholder="$t('common_field.select') + $t('menu.icon')" readonly>
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    class="el-input__icon"
                    style="height: 32px;width: 16px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('menu.name')" prop="menuName">
              <el-input v-model="form.menuName" :placeholder="$t('common_field.fill') + $t('menu.name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('menu.en_name')" prop="menuEnName">
              <el-input v-model="form.menuEnName" :placeholder="$t('common_field.fill') + $t('menu.en_name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common_field.sort')" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'">
              <span slot="label">
                <el-tooltip :content="$t('menu.link_tip')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.if_link') }}
              </span>
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0"> {{ $t('common_field.yes') }}</el-radio>
                <el-radio label="1"> {{ $t('common_field.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" prop="path">
              <span slot="label">
                <el-tooltip :content="$t('menu.router_tip')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.router') }}
              </span>
              <el-input v-model="form.path" :placeholder="$t('common_field.fill') + $t('menu.router')" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="component">
              <span slot="label">
                <el-tooltip :content="$t('menu.component_tip')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.component') }}
              </span>
              <el-input v-model="form.component" :placeholder="$t('common_field.fill') + $t('menu.component')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'M'">
              <el-input v-model="form.perms" :placeholder="$t('common_field.fill') + $t('menu.perms')" maxlength="100" />
              <span slot="label">
                <el-tooltip :content="$t('menu.permission_tip')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.permission') }}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType == 'C'">
              <el-input v-model="form.query" :placeholder="$t('common_field.fill') + $t('menu.param')" maxlength="255" />
              <span slot="label">
                <el-tooltip :content="$t('menu.param_tip')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.param') }}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType == 'C'">
              <span slot="label">
                <el-tooltip :content="$t('menu.cache_tip')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.if_cache') }}
              </span>
              <el-radio-group v-model="form.isCache">
                <el-radio label="0"> {{ $t('common_field.yes') }}</el-radio>
                <el-radio label="1"> {{ $t('common_field.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'">
              <span slot="label">
                <el-tooltip :content="$t('menu.show_tip')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.if_show') }}
              </span>
              <el-radio-group v-model="form.visible">
                <el-radio
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'">
              <span slot="label">
                <el-tooltip :content="$t('menu.status_tip')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{ $t('menu.status') }}
              </span>
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common_field.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('common_field.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMenu, getMenu, delMenu, addMenu, updateMenu } from "@/api/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/IconSelect";

export default {
  name: "Menu",
  dicts: ['sys_show_hide', 'sys_normal_disable'],
  components: { Treeselect, IconSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部折叠
      isExpandAll: false,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        menuName: undefined,
        visible: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        menuName: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" }
        ],
        menuEnName: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" }
        ],
        path: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name;
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, "menuId");
        this.loading = false;
      });
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      };
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = [];
        const menu = { menuId: 0, menuName: '主类目', children: [] };
        menu.children = this.handleTree(response.data, "menuId");
        this.menuOptions.push(menu);
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        menuEnName: undefined,
        icon: undefined,
        menuType: "M",
        orderNum: undefined,
        isFrame: "1",
        isCache: "0",
        visible: "0",
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title =  this.$t('menu.insert');
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getMenu(row.menuId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title =  this.$t('menu.update');
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('common_field.update_success'));
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('common_field.insert_success'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm(this.$t('common_field.tip'), this.$t('common_field.confirm'), this.$t('common_field.cancel'),this.$t('common_field.delete_confirm')).then(function() {
        return delMenu(row.menuId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('common_field.delete_success'));
      }).catch(() => {});
    }
  }
};
</script>
