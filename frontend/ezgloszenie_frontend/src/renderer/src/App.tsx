import React from 'react';
import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Register from './components/Register';
import Hero from './components/Hero';
import Pomoc from './components/Pomoc';
import MojeZgloszenia from './components/MojeZgloszenia';
import NoweZgloszenie from './components/NoweZgloszenie';

function App(): React.JSX.Element {
  return (
    <>
      <HashRouter>
        <Navbar />
        <Routes>
          <Route path="/"                element={<Navigate to="/login" replace />} />
          <Route path="/login"           element={<Login />} />
          <Route path="/register"        element={<Register />} />
          <Route path="/home"            element={<Hero />} />
          <Route path="/pomoc"           element={<Pomoc />} />
          <Route path="/moje-zgloszenia" element={<MojeZgloszenia />} />
          <Route path="/nowe-zgloszenie" element={<NoweZgloszenie />} />
        </Routes>
      </HashRouter>
    </>
  );
}

export default App;
