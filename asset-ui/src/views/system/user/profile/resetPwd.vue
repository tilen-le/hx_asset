<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$t('user.old_password')" prop="oldPassword">
      <el-input v-model="user.oldPassword" :placeholder="$t('common_field.fill') + $t('user.old_password')" type="password" show-password/>
    </el-form-item>
    <el-form-item :label="$t('user.new_password')" prop="newPassword">
      <el-input v-model="user.newPassword" :placeholder="$t('common_field.fill') + $t('user.new_password')" type="password" show-password/>
    </el-form-item>
    <el-form-item :label="$t('user.confirm_password')" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" :placeholder="$t('common_field.fill') + $t('user.confirm_password')" type="password" show-password/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $t('common_field.save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $t('common_field.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from "@/api/system/user";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error(this.$t('user.error_password')));
      } else {
        callback();
      }
    };
    return {
      test: "1test",
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" },
          { min: 6, max: 20, message: this.$t('user.password_format'), trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: this.$t('common_field.not_null'), trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserPwd(this.user.oldPassword, this.user.newPassword).then(
            response => {
              this.$modal.msgSuccess(this.$t('common_field.update_success'));
            }
          );
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
