import { resolve } from 'path'
import { defineConfig } from 'electron-vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  main: {},
  preload: {},
  renderer: {
    resolve: {
      alias: {
        // '@' points directly to React files directory
        '@': resolve('src/renderer/src')
      }
    },
    plugins: [react()]
  }
})