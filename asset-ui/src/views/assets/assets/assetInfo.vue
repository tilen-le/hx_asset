<template>
  <div class="app-container">
    <div class="divHead divInfo">
      <span class="head_title">资产编码：{{assetCode}}</span>
      <div style="display: inline-block;margin-left: 50px;">
        <dict-tag :options="dict.type.asset_status" :value="info.assetStatus"/>
      </div>
      <div style="display: inline-block;margin-left: 25px;" v-if="info.transfer == '1'">
        <el-tag type="warning">资产转移中</el-tag>
      </div>


<!--
      1在库 资产管理员：【派发】【盘亏】
      4试用 资产管理员：【维修】【盘亏】财务会计：【转固】
      6在用 资产管理员：【维修】【闲置】【报废】【外卖】【盘亏】
      8维修 资产管理员：【已维修】【已退货】
      2闲置 资产管理员：【派发】【盘亏】【转移】
      7待外卖 财务会计：【已外卖】资产管理员：【盘亏】
      9待报废 财务会计：【已报废】资产管理员：【盘亏】
      10、3、11、5已外卖，已退回，已报废，盘亏（无按钮显示，不能再操作-->
      <div class="head_button">
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '1' || info.assetStatus == '2') && checkPermi(['asset:process:receiveAsset'])"
                   @click="pai_fa">派发</el-button>
        <el-button size="mini" type="primary"
                   v-if="info.assetStatus == '2' && info.transfer == '0' && checkPermi(['asset:process:transferAsset'])"
                   @click="zhuan_yi('0', '资产转移')">转移</el-button>
        <el-button size="mini" type="primary"
                   v-if="info.transfer == '1' && checkPermi(['asset:process:accountTransferAsset'])"
                   @click="zhuan_yi('1', '资产账务转移')">账务转移</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '4' || info.assetStatus == '6') && checkPermi(['asset:process:maintainAsset'])"
                   @click="confirm_handle('维修')">维修</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '6') && checkPermi(['asset:process:scrapAsset'])"
                   @click="confirm_handle('报废')">报废</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '6') && checkPermi(['asset:process:waiteTakeOutAsset'])"
                   @click="confirm_handle('外卖')">外卖</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '9') && checkPermi(['asset:process:scrapedAsset'])"
                   @click="status_handle('已报废')">已报废</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '7') && checkPermi(['asset:process:takeOutAsset'])"
                   @click="status_handle('已外卖')">已外卖</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '8') && checkPermi(['asset:process:returnAsset'])"
                   @click="status_handle('已退货')">已退货</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '1' || info.assetStatus == '4' || info.assetStatus == '6' || info.assetStatus == '2'
                        || info.assetStatus == '7' || info.assetStatus == '9') && checkPermi(['asset:process:inventoryLossAsset'])"
                   @click="status_handle('盘亏')">盘亏</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '6') && checkPermi(['asset:process:unusedAsset'])"
                   @click="confirm_handle('闲置')">闲置</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '4') && checkPermi(['asset:process:fixationAsset'])"
                   @click="zhuan_gu">转固</el-button>
        <el-button size="mini" type="primary"
                   v-if="(info.assetStatus == '8') && checkPermi(['asset:process:maintainedAsset'])"
                   @click="yi_wei_xiu">已维修</el-button>
      </div>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="pai_fa_open" width="550px" append-to-body>
      <el-form ref="form" label-width="110px" :model="form">
        <el-form-item label="领用人" prop="responsiblePersonCode" :rules="required_rule">
          <el-select popper-class="long_select" v-model="form.responsiblePersonCode" placeholder="请选择领用人" filterable style="width:100%"  @change="personChange">
            <el-option v-for="item in common_users" :key="item.dictValue" :label="item.dictLabel"
                       :value="item.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="领用部门" prop="responsiblePersonDept" :rules="required_rule">
          <treeselect v-model="form.responsiblePersonDept" :options="dept_list" :normalizer="normalizer"
                      :show-count="true" placeholder="选择领用部门"/>
        </el-form-item>
        <el-form-item label="领用人岗位" prop="responsiblePersonJob" :rules="required_rule">
          <el-input v-model="form.responsiblePersonJob"/>
        </el-form-item>
        <el-form-item label="所在位置" prop="currentLocation" :rules="required_rule">
          <el-input v-model="form.currentLocation"/>
        </el-form-item>
        <el-form-item label="资产名称" prop="assetName" v-if="info.fixed == '0'" :rules="required_rule">
          <el-input v-model="form.assetName"/>
        </el-form-item>
        <el-form-item label="规格型号" prop="standard" v-if="info.fixed == '0'" :rules="required_rule">
          <el-input v-model="form.standard"/>
        </el-form-item>
        <el-form-item label="计划转固验收日期" prop="fixedAcceptanceDate" v-if="info.fixed == '0'" :rules="required_rule">
          <el-date-picker clearable style="width:100%"
                          v-model="form.fixedAcceptanceDate"
                          type="date"
                          value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="资产出厂编号" prop="factoryNo" v-if="info.fixed == '0'" :rules="required_rule">
          <el-input v-model="form.factoryNo"/>
        </el-form-item>
        <el-form-item label="领用说明" prop="comment">
          <el-input v-model="form.comment"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="pai_fa_submit">确定</el-button>
        <el-button @click="pai_fa_open=false">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dialogTitle" :visible.sync="zhuan_yi_open" width="550px" append-to-body>
      <el-form ref="form" label-width="120px" :model="form">
        <el-form-item label="接收公司" prop="company" :rules="required_rule">
          <el-select v-model="form.company" placeholder="请选择所属公司" clearable style="width:100%" :disabled="zhuan_yi_type == '1'">
            <el-option v-for="dict in dict.type.asset_company" :key="dict.value" :label="dict.label" :value="dict.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="接收人" prop="responsiblePersonCode" :rules="required_rule">
          <el-select popper-class="long_select" v-model="form.responsiblePersonCode" placeholder="请选择领用人" filterable style="width:100%"
                     :disabled="zhuan_yi_type == '1'" @change="personChange">
            <el-option v-for="item in common_users" :key="item.dictValue" :label="item.dictLabel"
                       :value="item.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="接收部门" prop="responsiblePersonDept" :rules="required_rule">
          <treeselect v-model="form.responsiblePersonDept" :options="dept_list" :normalizer="normalizer"
                      :show-count="true" placeholder="选择接收部门" :disabled="zhuan_yi_type == '1'"/>
        </el-form-item>
        <el-form-item label="接收人岗位" prop="responsiblePersonJob" :rules="required_rule">
          <el-input v-model="form.responsiblePersonJob" :disabled="zhuan_yi_type == '1'"/>
        </el-form-item>
        <el-form-item label="所在位置" prop="currentLocation" :rules="required_rule">
          <el-input v-model="form.currentLocation" :disabled="zhuan_yi_type == '1'"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="zhuan_yi_submit">确定</el-button>
        <el-button @click="zhuan_yi_open=false">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dialogTitle" :visible.sync="zhuan_gu_open" width="550px" append-to-body>
      <el-form ref="form" label-width="130px" :model="form">
        <el-form-item label="资产类型" prop="company" :rules="required_rule">
          <el-select v-model="form.assetType" placeholder="请选择资产类型" clearable style="width:100%">
            <el-option v-for="dict in dict.type.sap_card_asset_category" :key="dict.value" :label="dict.label"
                       :value="dict.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="保质期到期时间" prop="maturityTime" :rules="required_rule">
          <el-date-picker clearable style="width:100%"
                          v-model="form.maturityTime"
                          type="date"
                          value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="责任成本中心编码" prop="dutyCostCenter">
          <el-input v-model="form.dutyCostCenter"/>
        </el-form-item>
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="form.supplierName"/>
        </el-form-item>
        <el-form-item label="归属项目" prop="project">
          <el-input v-model="form.project"/>
        </el-form-item>
        <el-form-item label="用途" prop="comment">
          <el-input v-model="form.comment"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="zhuan_gu_submit">确定</el-button>
        <el-button @click="zhuan_gu_open=false">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dialogTitle" :visible.sync="yi_wei_xiu_open" width="550px" append-to-body>
      <el-form ref="form" label-width="100px" :model="form">
        <el-form-item label="资产状态" prop="assetStatus" :rules="required_rule">
          <el-radio-group v-model="form.assetStatus">
            <div v-if="info.fixed == '0'">
              <el-radio
                v-for="dict in dict.type.asset_status"
                :key="dict.value"
                :label="dict.value"
                v-if="dict.label == '在库' || dict.label == '试用'"
              >{{dict.label}}
              </el-radio>
            </div>
            <div v-else>
              <el-radio
                v-for="dict in dict.type.asset_status"
                :key="dict.value"
                :label="dict.value"
                v-if="dict.label == '在用'"
              >{{dict.label}}
              </el-radio>
            </div>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="资产保管人">
          <el-input v-model="info.responsiblePersonName" :disabled="true"/>
        </el-form-item>
        <el-form-item label="资产保管部门">
          <el-input v-model="info.responsiblePersonDept" :disabled="true"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="yi_wei_xiu_submit">确定</el-button>
        <el-button @click="yi_wei_xiu_open=false">取消</el-button>
      </div>
    </el-dialog>

    <div class="divMiddle divInfo" style="overflow-y: auto;">
      <el-descriptions title="基础信息" size="medium" border :column="3">
        <el-descriptions-item label="资产大类">{{ info.assetType }}</el-descriptions-item>
        <el-descriptions-item label="资产中类">{{ info.assetCategory }}</el-descriptions-item>
        <el-descriptions-item label="资产小类">{{ info.assetSubCategory }}</el-descriptions-item>
        <el-descriptions-item label="资产名称">{{ info.assetName }}</el-descriptions-item>
        <el-descriptions-item label="规格型号">{{ info.standard }}</el-descriptions-item>
        <el-descriptions-item label="资产状态">
          <dict-tag :options="dict.type.asset_status" :value="info.assetStatus"/>
        </el-descriptions-item>
        <el-descriptions-item label="转固状态">
          <dict-tag :options="dict.type.asset_fixed" :value="info.fixed"/>
        </el-descriptions-item>
        <el-descriptions-item label="资产保管人">{{ info.responsiblePersonName }}</el-descriptions-item>
        <el-descriptions-item label="资产保管部门">{{ info.responsiblePersonDept }}</el-descriptions-item>
        <el-descriptions-item label="所在位置">{{ info.currentLocation }}</el-descriptions-item>
        <el-descriptions-item label="所属公司">
          <dict-tag :options="dict.type.asset_company" :value="info.company"/>
        </el-descriptions-item>
        <el-descriptions-item label="资产管理员">{{ info.assetManager }}</el-descriptions-item>
        <el-descriptions-item label="资产管理部门">{{ info.assetManagementDept }}</el-descriptions-item>
        <el-descriptions-item label="成本中心">{{ info.costCenterName }}</el-descriptions-item>
        <el-descriptions-item label="资产原值(含税)">{{ info.originalValue }}</el-descriptions-item>
        <el-descriptions-item label="资产净值">{{ info.netValue }}</el-descriptions-item>
        <el-descriptions-item label="资产币制">{{ info.monetaryUnit }}</el-descriptions-item>
        <el-descriptions-item label="资产化日期">{{ info.capitalizationDate }}</el-descriptions-item>
        <el-descriptions-item label="保修期">{{ info.warranty }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ info.providerName }}</el-descriptions-item>
        <el-descriptions-item label="出厂编码">{{ info.factoryNo }}</el-descriptions-item>
        <el-descriptions-item label="采购单号">{{ info.purchaseOrderNo }}</el-descriptions-item>
        <el-descriptions-item label="资产卡片编号">{{ info.sapCode }}</el-descriptions-item>
        <el-descriptions-item label="入库日期">{{ info.storageDate }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ info.comment }}</el-descriptions-item>
      </el-descriptions>
    </div>


    <div class="divBottom divInfo">
      <el-tabs v-model="activeName" @tab-click="tabClick">
        <el-tab-pane label="保管记录" name="belongTab" v-if="checkPermi(['asset:log:custodyLogList'])">
          <custodyLog :assetCode="assetCode" ref="belongTab"></custodyLog>
        </el-tab-pane>
        <el-tab-pane label="工单记录" name="orderTab" v-if="checkPermi(['asset:log:workLogList'])">
          <workLog :assetCode="assetCode" ref="orderTab"></workLog>
        </el-tab-pane>
        <el-tab-pane label="操作日志" name="operateTab" v-if="checkPermi(['asset:log:operationLogList'])">
          <operationLog :assetCode="assetCode" ref="operateTab"></operationLog>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>
  import custodyLog from "./custodyLog";
  import workLog from "./workLog";
  import operationLog from "./operationLog";
  import {getDicts} from "@/api/system/dict/data";
  import {getInfo} from "@/api/assets/assets";
  import {checkPermi} from '@/utils/permission'
  import {
    accountTransferAsset,
    fixationAsset,
    getTransferInfo,
    inventoryLossAsset,
    maintainAsset,
    maintainedAsset,
    receiveAsset,
    returnAsset,
    scrapAsset,
    scrapedAsset,
    takeOutAsset,
    transferAsset,
    unusedAsset,
    waiteTakeOutAsset
  } from "@/api/assets/process";
  import {childTree} from '@/api/system/dept';
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'assetInfo',
    dicts: ['asset_status', 'sap_card_asset_category', 'asset_company', 'asset_fixed'],
    components: {Treeselect, custodyLog, workLog, operationLog},
    data() {
      return {
        baseUrl: process.env.VUE_APP_BASE_API,
        // 遮罩层
        loading: true,
        // 详情信息
        info: {},
        form: {},
        dialogTitle: '',
        pai_fa_open: false,
        zhuan_yi_open: false,
        zhuan_yi_type: '0',
        zhuan_gu_open: false,
        yi_wei_xiu_open: false,
        required_rule: [{required: true, message: "此项必填", trigger: 'blur'}],
        common_users: [],
        dept_list: [],
        assetCode: '',
        activeName: 'belongTab',
        tabRefresh: {
          belongTab: true,
          orderTab: false,
          operateTab: false
        }
      }
    },
    created() {
      this.loading = true
      const assetCode = this.$route.params && this.$route.params.assetCode;
      if (assetCode) {
        this.assetCode = assetCode;
        getInfo(this.assetCode).then((response) => {
          this.info = response.data
          this.loading = false
        })
      }
      setTimeout(() => {
        if (this.$auth.hasPermi("asset:log:custodyLogList")) {
          this.getTabContent('belongTab');
        } else if (this.$auth.hasPermi("asset:log:workLogList")) {
          this.getTabContent('orderTab');
        } else if (this.$auth.hasPermi("asset:log:operationLogList")) {
          this.getTabContent('operateTab');
        } else {}
      }, 500);

    },
    methods: {
      checkPermi,
      tabClick(tab, event) {
        this.getTabContent(tab.name);
      },
      getCommonUsers() {
        const userList = this.common_users
        if (userList.length == 0) {
          getDicts("common_users").then(res => {
            this.common_users = res.data
          })
        }
      },
      getInfo() {
        setTimeout(() => {
          this.loading = true
          getInfo(this.assetCode).then((response) => {
            this.info = response.data
            this.loading = false
            this.getTabContent(this.activeName);
          })
        }, 500);
      },
      getTabContent(tabName) {
        if (tabName == 'belongTab') {
          this.$refs.belongTab.getList();
        } else if (tabName == 'orderTab') {
          this.$refs.orderTab.getList();
        } else if (tabName == 'operateTab') {
          this.$refs.operateTab.getList();
        } else {
        }
      },
      clearForm() {
        this.form = {
          "assetCode": this.assetCode
        }
      },
      pai_fa() {
        this.clearForm();
        this.dialogTitle = '资产派发';
        this.pai_fa_open = true;
        this.getCommonUsers();
        this.getChildDeptTree();
      },
      pai_fa_submit() {
        receiveAsset(this.form).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.getInfo();
          this.pai_fa_open = false;
        });
      },
      zhuan_yi(type, title) {
        this.zhuan_yi_type = type;
        this.clearForm();
        this.dialogTitle = title;
        this.zhuan_yi_open = true;
        this.getCommonUsers();
        this.getChildDeptTree();
        if ('1' == type) {
          //获取转移信息
          getTransferInfo(this.assetCode).then((response) => {
            this.form = response.data
          })
        }
      },
      zhuan_yi_submit() {
        let type = this.zhuan_yi_type;
        if ('0' == type) {
          transferAsset(this.form).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.getInfo();
            this.zhuan_yi_open = false;
          });
        } else {
          accountTransferAsset(this.form).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.getInfo();
            this.zhuan_yi_open = false;
          });
        }
      },
      zhuan_gu() {
        this.clearForm();
        this.dialogTitle = '资产转固';
        this.zhuan_gu_open = true;
      },
      zhuan_gu_submit() {
        fixationAsset(this.form).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.getInfo();
          this.zhuan_gu_open = false;
        });
      },
      yi_wei_xiu() {
        this.clearForm();
        this.dialogTitle = '资产已维修';
        this.yi_wei_xiu_open = true;
      },
      yi_wei_xiu_submit() {
        maintainedAsset(this.form).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.getInfo();
          this.yi_wei_xiu_open = false;
        });
      },
      confirm_handle(type) {
        const content = "确定要" + type + "资产吗？";
        this.$modal.confirm("提示", "确定", "取消", content).then(() => {
          if (type === '维修') {
            this.clearForm();
            return maintainAsset(this.form);
          } else if (type === '报废') {
            this.clearForm();
            return scrapAsset(this.form);
          } else if (type === '外卖') {
            this.clearForm();
            return waiteTakeOutAsset(this.form);
          } else if (type === '闲置') {
            this.clearForm();
            this.form.clearInfo = '0';
            return unusedAsset(this.form);
          } else {
            this.$modal.msgError("页面按钮配置类型错误");
          }
        }).then(response => {
          this.getInfo();
          this.$modal.msgSuccess(type + "成功");
        }).catch(() => {
        });

      },
      status_handle(type) {
        const content = "确定更改资产状态为" + type + "？";
        this.$modal.confirm("提示", "确定", "取消", content).then(() => {
          if (type === '已报废') {
            this.clearForm();
            return scrapedAsset(this.form);
          } else if (type === '已外卖') {
            this.clearForm();
            return takeOutAsset(this.form);
          } else if (type === '已退货') {
            this.clearForm();
            return returnAsset(this.form);
          } else if (type === '盘亏') {
            this.clearForm();
            return inventoryLossAsset(this.form);
          } else {
            this.$modal.msgError("页面按钮配置类型错误");
          }
        }).then(response => {
          this.getInfo();
          this.$modal.msgSuccess(type + "修改成功");
        }).catch(() => {
        });
      },
      getChildDeptTree() {
        const dept_list = this.dept_list
        if (dept_list.length == 0) {
          childTree().then(response => {
            this.dept_list = response.data
          })
        }
      },
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.label,
          children: node.children
        }
      },
      personChange(value) {
        this.common_users.forEach(user => {
          if (user.dictValue == value) {
            this.form.responsiblePersonDept = user.comment;
            return;
          }
        });
      },
    }
  }
</script>

<style>
  .el-tabs .el-tabs__content {
    /*height: calc(38vh - 70px);*/
    height: 290px;
    overflow-y: auto;
  }
</style>

<style scoped>
  .app-container {
    padding: 5px;
  }
  .el-descriptions__header {
    margin-bottom: 10px;
  }

  .divInfo {
    margin: 5px;
    padding: 5px 10px 5px 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  }

  .divHead {
    /*height: 5vh;*/
    display: flex;
    align-items: center;
  }

/*  .divMiddle {
    height: 41vh;
  }
*/

  .divBottom {
    height: 360px;
  }

  .head_title {
    font-size: 20px;
  }

  .head_button {
    position: absolute;
    right: 50px;
  }


  .long_select {
    max-width: 100px;
  }

</style>
