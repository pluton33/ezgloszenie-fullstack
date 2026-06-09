import React from 'react';
import { ClipboardList, BookOpen, ShieldAlert } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import '../assets/hero.css';

function Hero() {
  const navigate = useNavigate();
  return (
    <div className="main-panel">
      <div className="hero-welcome">
        <h1>Witaj, Użytkowniku!</h1>
        <p>Wybierz akcję, aby rozpocząć lub sprawdź status swoich spraw.</p>
      </div>
      <div className="action-cards-container">
        <button className="action-card primary" onClick={() => navigate('/nowe-zgloszenie')}>
          <ClipboardList size={40} className="card-icon" />
          <h2>Nowe Zgłoszenie</h2>
          <p>Zgłoś incydent, przestępstwo lub wykroczenie online.</p>
        </button>
        <button className="action-card secondary" onClick={() => navigate('/moje-zgloszenia')}>
          <ShieldAlert size={40} className="card-icon" />
          <h2>Moje Zgłoszenia</h2>
          <p>Przeglądaj historię i sprawdzaj status swoich spraw.</p>
        </button>
        <button className="action-card secondary" onClick={() => navigate('/pomoc')}>
          <BookOpen size={40} className="card-icon" />
          <h2>Pomoc i instrukcje</h2>
          <p>Zobacz, jak prawidłowo wypełnić formularz i poznaj swoje prawa.</p>
        </button>
      </div>
    </div>
  );
}

export default Hero;
