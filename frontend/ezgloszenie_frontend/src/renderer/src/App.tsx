import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar'
import Login from './components/Login'
import Register from './components/Register'
import Hero from './components/Hero'
import MojeZgloszenia from './components/MojeZgloszenia';

function App(): React.JSX.Element {

  return (
    <>
      <HashRouter>

        <Navbar />
        <Routes>
          <Route path="/home" element = {<Hero />} />
          <Route path="/MojeZgloszenia" element = {<MojeZgloszenia />} />
          <Route path="/" element={<Navigate to= "/MojeZgloszenia" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </HashRouter>
    </>
  )
}

export default App
