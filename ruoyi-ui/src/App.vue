<template>
  <router-view />
</template>

<script setup>
import useSettingsStore from '@/store/modules/settings'
import { handleThemeStyle } from '@/utils/theme'
import { initGlassPerf } from '@/utils/glassPerf'

onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
    // 液态玻璃性能保险：低配电脑自动关闭实时模糊
    initGlassPerf()
  })
})
</script>
