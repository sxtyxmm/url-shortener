import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      // Proxy short code redirects directly to backend
      '^/[a-zA-Z0-9_-]+$': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    },
    cors: true,
    host: true,
  },
  build: {
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
      },
    },
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom'],
        },
      },
    },
    target: 'esnext',
    reportCompressedSize: false,
  },
  optimizeDeps: {
    include: ['react', 'react-dom'],
  },
})