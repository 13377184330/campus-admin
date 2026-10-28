<template>
  <div class="login">
    <!-- 液态玻璃折射滤镜：canvas 生成 SDF 位移图，由 feDisplacementMap 作用于背景 -->
    <svg class="liquid-glass-svg" aria-hidden="true">
      <defs>
        <filter id="liquid-glass-login" x="0%" y="0%" width="100%" height="100%">
          <feImage ref="feMapRef" result="lg-map" />
          <feDisplacementMap
            ref="feDispRef"
            in="SourceGraphic"
            in2="lg-map"
            xChannelSelector="R"
            yChannelSelector="G"
            scale="0"
          />
        </filter>
      </defs>
    </svg>
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ title }}</h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width:100%;"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'

const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: ""
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
const feMapRef = ref(null)
const feDispRef = ref(null)
let liquidResizeTimer = null

// ---------- 液态玻璃折射：位移图生成（参考 shuding/liquid-glass） ----------
function smoothStep(a, b, t) {
  t = Math.max(0, Math.min(1, (t - a) / (b - a)))
  return t * t * (3 - 2 * t)
}

function vectorLength(x, y) {
  return Math.sqrt(x * x + y * y)
}

/** 圆角矩形有向距离场：负值在内部，正值在外部 */
function roundedRectSDF(x, y, width, height, radius) {
  const qx = Math.abs(x) - width + radius
  const qy = Math.abs(y) - height + radius
  return Math.min(Math.max(qx, qy), 0) + vectorLength(Math.max(qx, 0), Math.max(qy, 0)) - radius
}

/** 按卡片尺寸生成边缘折射位移图：中心不变形，仅边缘“膨胀”折射。
    位移图是平滑渐变，用 1/2 分辨率生成即可（质量不变，耗时降为 1/4） */
function buildLiquidGlassMap() {
  const formEl = proxy.$refs.loginRef && proxy.$refs.loginRef.$el
  if (!formEl || !feMapRef.value || !feDispRef.value) return
  const w = Math.round(formEl.offsetWidth)
  const h = Math.round(formEl.offsetHeight)
  if (!w || !h) return

  const mapW = Math.max(1, Math.round(w / 2))
  const mapH = Math.max(1, Math.round(h / 2))
  const canvas = document.createElement("canvas")
  canvas.width = mapW
  canvas.height = mapH
  const ctx = canvas.getContext("2d")
  if (!ctx) return

  const aspect = w / h
  const data = new Uint8ClampedArray(mapW * mapH * 4)
  const rawValues = []
  let maxScale = 0

  for (let y = 0; y < mapH; y++) {
    for (let x = 0; x < mapW; x++) {
      const ix = x / mapW - 0.5
      const iy = y / mapH - 0.5
      const distanceToEdge = roundedRectSDF(ix, iy, 0.3, 0.3 / aspect, 0.6)
      const displacement = smoothStep(0.8, 0, distanceToEdge - 0.15)
      const scaled = smoothStep(0, 1, displacement)
      const dx = (ix * scaled + 0.5) * mapW - x
      const dy = (iy * scaled + 0.5) * mapH - y
      if (Math.abs(dx) > maxScale) maxScale = Math.abs(dx)
      if (Math.abs(dy) > maxScale) maxScale = Math.abs(dy)
      rawValues.push(dx, dy)
    }
  }
  maxScale *= 0.75
  if (maxScale < 1) return

  let index = 0
  for (let i = 0; i < data.length; i += 4) {
    data[i] = ((rawValues[index++] / maxScale) + 0.5) * 255
    data[i + 1] = ((rawValues[index++] / maxScale) + 0.5) * 255
    data[i + 2] = 0
    data[i + 3] = 255
  }
  ctx.putImageData(new ImageData(data, mapW, mapH), 0, 0)

  const href = canvas.toDataURL()
  feMapRef.value.setAttribute("width", String(w))
  feMapRef.value.setAttribute("height", String(h))
  feMapRef.value.setAttribute("href", href)
  feMapRef.value.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", href)
  // 位移图按 1/2 分辨率生成：通道值不变，但位移量以“屏幕像素”计，scale 需乘回 2
  feDispRef.value.setAttribute("scale", String(maxScale * 2))
}

function scheduleLiquidGlassMap() {
  if (liquidResizeTimer) clearTimeout(liquidResizeTimer)
  liquidResizeTimer = setTimeout(() => nextTick(buildLiquidGlassMap), 150)
}

onMounted(() => {
  nextTick(buildLiquidGlassMap)
  window.addEventListener("resize", scheduleLiquidGlassMap)
})

onBeforeUnmount(() => {
  window.removeEventListener("resize", scheduleLiquidGlassMap)
  if (liquidResizeTimer) clearTimeout(liquidResizeTimer)
})

// 验证码开关
const captchaEnabled = ref(true)
// 验证码开关会影响卡片高度，需要重算位移图
watch(captchaEnabled, () => scheduleLiquidGlassMap())
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang='scss' scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.liquid-glass-svg {
  position: absolute;
  width: 0;
  height: 0;
  pointer-events: none;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #1f3b34;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 3px;
}

.login-form {
  position: relative;
  overflow: hidden;
  border-radius: 20px;
  // 基础磨砂（不支持 SVG 滤镜的浏览器回退到这一行）
  background: rgba(255, 255, 255, 0.52);
  backdrop-filter: blur(4px) saturate(1.3);
  -webkit-backdrop-filter: blur(4px) saturate(1.3);
  // 真折射：边缘位移 + 轻磨砂（模糊过大会抹掉背景细节使折射不可见，
  // 过小则背景亮斑会透出来干扰表单控件状态，4px 为实测平衡点）
  backdrop-filter: url(#liquid-glass-login) blur(4px) saturate(1.3) brightness(1.04);
  border: 1px solid rgba(255, 255, 255, 0.65);
  box-shadow:
    0 24px 64px rgba(23, 52, 46, 0.35),
    inset 0 1px 0 rgba(255, 255, 255, 0.95),
    inset 0 -1px 0 rgba(255, 255, 255, 0.3),
    inset 0 0 0 1px rgba(255, 255, 255, 0.25);
  width: 400px;
  padding: 32px 28px 8px 28px;
  z-index: 1;

  // 顶部斜向高光，模拟玻璃反光（克制，避免遮住边缘折射）
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 32%;
    background: linear-gradient(120deg, rgba(255, 255, 255, 0.35) 0%, rgba(255, 255, 255, 0.1) 45%, transparent 70%);
    pointer-events: none;
  }

  .el-checkbox {
    color: #46554f;
  }

  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}

html.dark .login {
  background-image: linear-gradient(rgba(0, 0, 0, 0.55), rgba(0, 0, 0, 0.55)), url("../assets/images/login-background.jpg");
  .login-form {
    background: var(--el-bg-color-overlay) !important;
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.5);
  }
}
</style>
