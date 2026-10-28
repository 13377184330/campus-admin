/**
 * 液态玻璃性能保险（glassPerf）
 * --------------------------------------------------------------
 * backdrop-filter 的实时模糊对显卡有要求。为兼顾观感与流畅度：
 *  1. glass.scss 已把模糊收敛到“背后有内容”的表面（弹窗/抽屉/弹层/登录卡）；
 *  2. 这里再做运行时兜底：自动检测帧率，偏低时给 <html> 加 glass-off 类，
 *     一键关闭全部实时模糊（保留半透明与光幕，观感仍是玻璃色调）。
 *
 * 手动干预（浏览器控制台执行后刷新）：
 *   localStorage.setItem('glass:mode', 'off')  // 强制关闭模糊（低配电脑）
 *   localStorage.setItem('glass:mode', 'on')   // 强制开启模糊（不自动降级）
 *   localStorage.setItem('glass:mode', 'auto') // 默认，自动检测
 */

const MODE_KEY = 'glass:mode'
const LOW_FPS = 40          // 低于此帧率视为卡顿
const WINDOW_MS = 2000      // 统计窗口
const TRIGGER_WINDOWS = 3   // 连续 N 个窗口偏低才降级，避免首屏一次性卡顿误判
const SKIP_START_MS = 6000  // 跳过页面加载与首屏渲染的忙时，只看日常使用的真实帧率

let started = false

function applyGlassOff() {
  document.documentElement.classList.add('glass-off')
}

export function initGlassPerf() {
  if (started || typeof window === 'undefined') return
  started = true

  let mode = 'auto'
  try {
    mode = localStorage.getItem(MODE_KEY) || 'auto'
  } catch (e) {
    /* localStorage 不可用时按 auto 处理 */
  }

  if (mode === 'off') {
    applyGlassOff()
    return
  }
  if (mode === 'on') {
    return
  }

  // auto：帧率巡检
  let frames = 0
  let windowStart = 0
  let lowWindows = 0
  let done = false

  function tick(now) {
    if (done) return
    // 后台标签页 rAF 会被暂停/节流，不计入统计
    if (document.hidden || !windowStart) {
      frames = 0
      windowStart = now
      requestAnimationFrame(tick)
      return
    }
    frames++
    if (now - windowStart >= WINDOW_MS) {
      const fps = (frames * 1000) / (now - windowStart)
      if (fps < LOW_FPS) {
        lowWindows++
      } else {
        lowWindows = Math.max(0, lowWindows - 1)
      }
      frames = 0
      windowStart = now
      if (lowWindows >= TRIGGER_WINDOWS) {
        done = true
        applyGlassOff()
        console.info('[学员管理系统] 检测到当前电脑渲染较慢，已自动关闭玻璃实时模糊以保证流畅。如需强制开启，可在控制台执行：localStorage.setItem("glass:mode","on")')
        return
      }
    }
    requestAnimationFrame(tick)
  }

  setTimeout(() => {
    requestAnimationFrame(tick)
  }, SKIP_START_MS)
}
