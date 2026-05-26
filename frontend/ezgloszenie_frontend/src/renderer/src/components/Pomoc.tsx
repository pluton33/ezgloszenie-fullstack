import React, { useState } from 'react';
import { HelpCircle, ShieldAlert, FileText, Phone, ChevronDown, ChevronUp } from 'lucide-react';
import '../assets/pomoc.css';

const faqData = [
  {
    id: 1,
    icon: <FileText size={20} />,
    pytanie: 'Jak prawidłowo wypełnić nowe zgłoszenie?',
    odpowiedz: 'Przejdź do zakładki "Nowe Zgłoszenie". Wypełnij wszystkie wymagane pola oznaczone gwiazdką (*), w tym dokładny opis zdarzenia oraz czas jego wystąpienia. Upewnij się, że podane informacje są zgodne z prawdą, ponieważ fałszywe zeznania podlegają odpowiedzialności karnej.'
  },
  {
    id: 2,
    icon: <HelpCircle size={20} />,
    pytanie: 'Co oznaczają poszczególne statusy zgłoszeń?',
    odpowiedz: '• Oczekujące: Zgłoszenie zostało wysłane i czeka na przypisanie do funkcjonariusza.\n• Przyjęte w systemie: Sprawa jest w toku, trwają czynności wyjaśniające.\n• Zamknięte: Postępowanie zostało zakończone.'
  },
  {
    id: 3,
    icon: <ShieldAlert size={20} />,
    pytanie: 'Kto ma dostęp do moich danych osobowych?',
    odpowiedz: 'Administratorem Twoich danych jest Komenda Główna Policji. Dostęp do nich mają wyłącznie upoważnieni funkcjonariusze prowadzący Twoją sprawę. System e-Zgłoszenie spełnia najwyższe standardy kryptograficzne.'
  }
];

function Pomoc(): React.JSX.Element {
  // Stan do trzymania ID otwartego pytania (null = wszystkie zamknięte)
  const [otwartePytanie, setOtwartePytanie] = useState<number | null>(null);

  const togglePytanie = (id: number) => {
    // Jeśli klikniemy w otwarte, to zamykamy. W przeciwnym razie otwieramy kliknięte.
    setOtwartePytanie(otwartePytanie === id ? null : id);
  };

  return (
    <div className="pomoc-container">
      {/* Nagłówek sekcji */}
      <div className="pomoc-header">
        <h1 className="pomoc-title">Pomoc i instrukcje</h1>
        <p className="pomoc-subtitle">
          Znajdź odpowiedzi na najczęściej zadawane pytania lub dowiedz się, jak prawidłowo korzystać z systemu.
        </p>
      </div>

      <div className="pomoc-content">
        {/* Lewa kolumna: FAQ */}
        <div className="faq-section">
          <h2 className="section-title">Baza Wiedzy (FAQ)</h2>
          
          <div className="faq-list">
            {faqData.map((item) => (
              <div 
                key={item.id} 
                className={`faq-item ${otwartePytanie === item.id ? 'active' : ''}`}
              >
                <button className="faq-question" onClick={() => togglePytanie(item.id)}>
                  <div className="faq-question-left">
                    <span className="faq-icon">{item.icon}</span>
                    <span className="faq-text">{item.pytanie}</span>
                  </div>
                  <div className="faq-chevron">
                    {otwartePytanie === item.id ? <ChevronUp size={20} /> : <ChevronDown size={20} />}
                  </div>
                </button>
                
                {otwartePytanie === item.id && (
                  <div className="faq-answer">
                    {/* Zamiana znaków nowej linii (\n) na tagi <br/> w React */}
                    {item.odpowiedz.split('\n').map((linia, index) => (
                      <React.Fragment key={index}>
                        {linia}
                        <br />
                      </React.Fragment>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Prawa kolumna: Kontakt techniczny */}
        <div className="contact-section">
          <div className="contact-card">
            <div className="contact-icon-wrapper">
              <Phone size={32} className="contact-icon" />
            </div>
            <h3>Wsparcie Techniczne</h3>
            <p>Masz problem z działaniem aplikacji e-Zgłoszenie?</p>
            <div className="contact-details">
              <strong>Infolinia:</strong> 112<br/>
              <strong>Email:</strong> pomoc@ezgloszenie.gov.pl
            </div>
            <div className="contact-warning">
              Pamiętaj, że wsparcie techniczne nie udziela informacji o postępach w Twoich sprawach.
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Pomoc;