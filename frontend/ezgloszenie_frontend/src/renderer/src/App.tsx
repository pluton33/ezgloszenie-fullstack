import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar'
import Login from './components/Login'
import Register from './components/Register'

function App(): React.JSX.Element {

  return (
    <>
      <HashRouter>

        <Navbar />
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </HashRouter>
    </>
  )
}

export default App
