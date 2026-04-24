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

const router = useRouter()
const authStore = useAuthStore()

const captchaBase64 = ref('')

const form = reactive({
  username: 'admin',
  password: '123456',
  captchaCode: '',
  captchaUuid: ''
})

const refreshCaptcha = async () => {
  try {
    const data = await request.get('/api/user/captcha/generate')
    form.captchaUuid = data.uuid
    captchaBase64.value = data.captcha
  } catch (error) {
    ElMessage.error('验证码加载失败')
  }
}

const submit = async () => {
  if (!form.captchaCode) {
    ElMessage.error('请输入验证码')
    return
  }
  
  try {
    const data = await request.post('/api/admin/auth/login', form)
    authStore.setAuth(data.token, data.nickname || '管理员')
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    if (error.code === 400) {
      refreshCaptcha()
      form.captchaCode = ''
    }
    ElMessage.error(error.message || '登录失败')
  }
}

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
