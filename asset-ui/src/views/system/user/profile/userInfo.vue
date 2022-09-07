<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$t('user.nickname')" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item :label="$t('common_field.phone')" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item :label="$t('common_field.mail')" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item :label="$t('user.sex')">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">{{ $t('common_field.male') }}</el-radio>
        <el-radio label="1">{{ $t('common_field.female') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $t('common_field.save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $t('common_field.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" }
        ],
        email: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" },
          {
            type: "email",
            message: this.$t('common_field.format'),
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: this.$t('common_field.format'),
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.$modal.msgSuccess(this.$t('common_field.update_success'));
          });
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>
