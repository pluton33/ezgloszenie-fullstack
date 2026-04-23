import React from 'react';
import { ClipboardList, BookOpen, ShieldAlert } from 'lucide-react';
import '../assets/base.css';

function Hero() {
    return (
        <main className="main-panel">
            
            {/* sekcja powitalna */}
            <div className="hero-welcome">
                <h1>Witaj, user!</h1>
                <p>Wybierz akcję, aby rozpocząć lub sprawdź status swoich spraw.</p>
            </div>

            {/* kontener z 3 btn */}
            <div className="action-cards-container">
                {/* nowe zgloszenie */}
                <button className="action-card primary">
                    <ShieldAlert className="card-icon" size={42} strokeWidth={1.5} />
                    <h2>Nowe Zgłoszenie</h2>
                    <p>Zgłoś incydent, przestępstwo lub wykroczenie online.</p>
                </button>

                {/* moje zgloszenia */}
                <button className="action-card secondary">
                    <ClipboardList className="card-icon" size={36} strokeWidth={1.5} />
                    <h2>Moje Zgłoszenia</h2>
                    <p>Przeglądaj historię i sprawdzaj status swoich spraw.</p>
                </button>

                {/* pomoc i instrukcje */}
                <button className="action-card secondary">
                    <BookOpen className="card-icon" size={36} strokeWidth={1.5} />
                    <h2>Pomoc i instrukcje</h2>
                    <p>Zobacz, jak prawidłowo wypełnić formularz i poznaj swoje prawa.</p>
                </button>
            </div>
        </main>
    );
}

export default Hero;