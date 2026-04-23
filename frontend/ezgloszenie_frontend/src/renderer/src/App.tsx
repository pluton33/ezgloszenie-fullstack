import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar'
import Login from './components/Login'
import Register from './components/Register'
import Zgloszenia from './components/Zgloszenia'

function App(): React.JSX.Element {

  return (
    <>
      <HashRouter>

        <Navbar />
        <Routes>
          <Route path="/" element={<Navigate to="/zgloszenia" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/zgloszenia" element={<Zgloszenia />} />
        </Routes>
      </HashRouter>
    </>
  )
}

export default App
