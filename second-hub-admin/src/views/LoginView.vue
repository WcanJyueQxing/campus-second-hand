<template>
  <div class="login-wrap">
    <el-card class="login-card">
      <h2>后台登录</h2>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="验证码">
          <div class="captcha-container">
            <el-input v-model="form.captchaCode" placeholder="请输入验证码" style="width: 63%" />
            <div class="captcha-image" @click="refreshCaptcha">
              <img :src="captchaBase64" alt="验证码" />
            </div>
          </div>
        </el-form-item>
        <el-button type="primary" style="width: 100%" @click="submit">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { useAuthStore } from '../stores/auth'

/**
 * 登录视图组件
 * 提供管理员登录功能，包含用户名、密码和验证码验证
 */
const router = useRouter()
const authStore = useAuthStore()

// 验证码图片的 Base64 数据
const captchaBase64 = ref('')

/**
 * 登录表单数据
 * @property {string} username - 用户名
 * @property {string} password - 密码
 * @property {string} captchaCode - 验证码
 * @property {string} captchaUuid - 验证码 UUID
 */
const form = reactive({
  username: 'admin',
  password: '123456',
  captchaCode: '',
  captchaUuid: ''
})

/**
 * 刷新验证码
 * 调用后端接口获取新的验证码图片
 */
const refreshCaptcha = async () => {
  try {
    const data = await request.get('/api/user/captcha/generate')
    form.captchaUuid = data.uuid
    captchaBase64.value = data.captcha
  } catch (error) {
    ElMessage.error('验证码加载失败')
  }
}

/**
 * 提交登录请求
 * 验证表单后调用登录接口，成功后跳转到首页
 */
const submit = async () => {
  // 验证验证码是否填写
  if (!form.captchaCode) {
    ElMessage.error('请输入验证码')
    return
  }
  
  try {
    // 调用管理员登录接口
    const data = await request.post('/api/admin/auth/login', form)
    
    // 保存登录状态
    authStore.setAuth(data.token, data.nickname || '管理员')
    
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    // 登录失败时刷新验证码
    if (error.code === 400) {
      refreshCaptcha()
      form.captchaCode = ''
    }
    ElMessage.error(error.message || '登录失败')
  }
}

// 组件挂载时加载验证码
onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-wrap {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(120deg, #f7f8fa, #eaf2ff);
}

.login-card {
  width: 420px;
}

h2 {
  text-align: center;
}

.captcha-container {
  display: flex;
  align-items: center;
}

.captcha-image {
  width: 35%;
  height: 40px;
  margin-left: 2%;
  cursor: pointer;
}

.captcha-image img {
  width: 100%;
  height: 100%;
  border-radius: 4px;
}
</style>