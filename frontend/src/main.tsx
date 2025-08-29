import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import { AuthProvider } from './context/AuthContext.tsx'
import { GamificationProvider } from './context/GamificationContext.tsx'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <AuthProvider>
      <GamificationProvider>
        <App/>
      </GamificationProvider>
    </AuthProvider>
  </React.StrictMode>,
)