import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  ClipboardList, MapPin, Clock, AlertTriangle,
  CheckCircle, ChevronRight, ChevronLeft,
  ShieldAlert, FileText, UserX,
  LockKeyhole, Hammer, House, Car, Siren, Pill, Laptop
} from 'lucide-react';
import '../assets/nowezgloszenie.css';

const API_BASE_URL = 'http://localhost:8080';

const KATEGORIE = [
  { value: 'Kradzież / Rozbój', icon: LockKeyhole },
  { value: 'Uszkodzenie mienia', icon: Hammer },
  { value: 'Przemoc domowa', icon: House },
  { value: 'Wypadek drogowy', icon: Car },
  { value: 'Zakłócenie porządku', icon: Siren },
  { value: 'Narkotyki', icon: Pill },
  { value: 'Cyberprzestępstwo', icon: Laptop },
  { value: 'Inne', icon: ClipboardList },
];

function NoweZgloszenie(): React.JSX.Element {
  const navigate = useNavigate();
  const [krok, setKrok] = useState(1);
  const [wyslano, setWyslano] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [form, setForm] = useState({
    kategoria: '',
    tytul: '',
    opis: '',
    dataZdarzenia: '',
    godzinaZdarzenia: '',
    miejsce: '',
    anonimowe: false,
  });
  const [bledy, setBledy] = useState<Record<string, string>>({});

  const set = (name: string, value: any) => {
    setForm(p => ({ ...p, [name]: value }));
    if (bledy[name]) setBledy(p => ({ ...p, [name]: '' }));
    if (submitError) setSubmitError(null);
  };

  const waliduj1 = () => {
    const e: Record<string, string> = {};
    if (!form.kategoria) e.kategoria = 'Wybierz kategorię.';
    if (!form.tytul.trim()) e.tytul = 'Podaj tytuł zgłoszenia.';
    if (form.opis.trim().length < 20) e.opis = 'Opis musi mieć min. 20 znaków.';
    setBledy(e);
    return !Object.keys(e).length;
  };

  const waliduj2 = () => {
    const e: Record<string, string> = {};
    if (!form.dataZdarzenia) e.dataZdarzenia = 'Podaj datę zdarzenia.';
    if (!form.miejsce.trim()) e.miejsce = 'Podaj miejsce zdarzenia.';
    setBledy(e);
    return !Object.keys(e).length;
  };

  const dalej = () => {
    if (krok === 1 && waliduj1()) setKrok(2);
    else if (krok === 2 && waliduj2()) setKrok(3);
  };

  const wstecz = () => setKrok(k => k - 1);

  const resetForm = () => {
    setKrok(1);
    setForm({
      kategoria: '',
      tytul: '',
      opis: '',
      dataZdarzenia: '',
      godzinaZdarzenia: '',
      miejsce: '',
      anonimowe: false,
    });
    setBledy({});
    setSubmitError(null);
  };

  const handleSubmit = async () => {
    if (!waliduj1() || !waliduj2()) {
      setKrok(!waliduj1() ? 1 : 2);
      return;
    }

    setIsSubmitting(true);
    setSubmitError(null);

    try {
      const response = await fetch(`${API_BASE_URL}/addReport`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          id: null,
          title: form.tytul.trim(),
          description: `${form.opis.trim()}\n\nMiejsce: ${form.miejsce.trim()}\nData zdarzenia: ${form.dataZdarzenia}${form.godzinaZdarzenia ? ` ${form.godzinaZdarzenia}` : ''}\nAnonimowe: ${form.anonimowe ? 'Tak' : 'Nie'}`,
          status: null,
          category: {
            id: 1,
            name: form.kategoria,
          }
        }),
      });

      if (response.status === 401) {
        setSubmitError('Musisz się zalogować, aby wysłać zgłoszenie.');
        return;
      }

      if (!response.ok) {
        setSubmitError(`Nie udało się wysłać zgłoszenia. Kod błędu: ${response.status}.`);
        return;
      }

      setWyslano(true);
    } catch {
      setSubmitError('Nie udało się połączyć z serwerem.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (wyslano) return (
    <div className="nz-page">
      <div className="nz-sukces-wrapper">
        <div className="action-card primary nz-sukces-card">
          <CheckCircle size={56} style={{ marginBottom: 16 }} />
          <h2>Zgłoszenie wysłane!</h2>
          <p>Twoje zgłoszenie trafiło do systemu. Możesz śledzić jego status w sekcji Moje Zgłoszenia.</p>
        </div>
        <div className="nz-sukces-btns">
          <button className="action-card secondary nz-btn-kafelek" onClick={() => navigate('/moje-zgloszenia')}>
            <ShieldAlert size={32} style={{ marginBottom: 10 }} />
            <span>Moje Zgłoszenia</span>
          </button>
          <button className="action-card secondary nz-btn-kafelek" onClick={() => { setWyslano(false); resetForm(); }}>
            <FileText size={32} style={{ marginBottom: 10 }} />
            <span>Nowe Zgłoszenie</span>
          </button>
        </div>
      </div>
    </div>
  );

  return (
    <div className="nz-page">
      <div className="hero-welcome" style={{ marginBottom: 24 }}>
        <h1>Nowe Zgłoszenie</h1>
        <p>Wypełnij formularz krok po kroku — zajmie to tylko chwilę.</p>
      </div>

      <div className="nz-steps">
        {['Kategoria i opis', 'Miejsce i czas', 'Podsumowanie'].map((l, i) => (
          <React.Fragment key={i}>
            <div className={`nz-step${krok === i + 1 ? ' active' : ''}${krok > i + 1 ? ' done' : ''}`}>
              <div className="nz-step-num">
                {krok > i + 1 ? <CheckCircle size={14} /> : i + 1}
              </div>
              <span>{l}</span>
            </div>
            {i < 2 && <div className={`nz-step-line${krok > i + 1 ? ' done' : ''}`} />}
          </React.Fragment>
        ))}
      </div>

      {krok === 1 && (
        <div className="nz-section">
          <p className="nz-label-top">Wybierz kategorię zdarzenia *</p>
          <div className="action-cards-container nz-kategorie">
            {KATEGORIE.map(({ value, icon: Icon }) => (
              <button
                key={value}
                className={`action-card nz-kat-card${form.kategoria === value ? ' primary' : ' secondary'}`}
                onClick={() => set('kategoria', value)}
                type="button"
              >
                <Icon size={30} className="nz-kat-icon" />
                <span className="nz-kat-label">{value}</span>
              </button>
            ))}
          </div>
          {bledy.kategoria && <span className="nz-error">{bledy.kategoria}</span>}

          <div className="nz-pola">
            <div className="nz-field">
              <label htmlFor="tytul">Tytuł zgłoszenia *</label>
              <input
                id="tytul"
                name="tytul"
                type="text"
                placeholder="Krótki tytuł opisujący zdarzenie"
                value={form.tytul}
                onChange={e => set('tytul', e.target.value)}
                className={bledy.tytul ? 'error' : ''}
                maxLength={100}
              />
              {bledy.tytul && <span className="nz-error">{bledy.tytul}</span>}
            </div>

            <div className="nz-field">
              <label htmlFor="opis">Szczegółowy opis *</label>
              <textarea
                id="opis"
                name="opis"
                rows={5}
                placeholder="Opisz dokładnie co się wydarzyło. Pamiętaj — fałszywe zeznania podlegają odpowiedzialności karnej (art. 233 KK)."
                value={form.opis}
                onChange={e => set('opis', e.target.value)}
                className={bledy.opis ? 'error' : ''}
              />
              <span className="nz-char">{form.opis.length} / min. 20 znaków</span>
              {bledy.opis && <span className="nz-error">{bledy.opis}</span>}
            </div>

            <div className="nz-anon-row">
              <input
                id="anon"
                type="checkbox"
                checked={form.anonimowe}
                onChange={e => set('anonimowe', e.target.checked)}
              />
              <label htmlFor="anon"><UserX size={15} /> Złóż zgłoszenie anonimowo</label>
            </div>
          </div>
        </div>
      )}

      {krok === 2 && (
        <div className="nz-section">
          <div className="action-cards-container nz-czas-grid">
            <div className={`action-card secondary nz-info-kafelek${bledy.dataZdarzenia ? ' error-card' : ''}`}>
              <MapPin size={28} style={{ marginBottom: 8 }} />
              <h2>Data zdarzenia *</h2>
              <input
                type="date"
                value={form.dataZdarzenia}
                onChange={e => set('dataZdarzenia', e.target.value)}
                max={new Date().toISOString().split('T')[0]}
                className="nz-input-inline"
              />
              {bledy.dataZdarzenia && <span className="nz-error">{bledy.dataZdarzenia}</span>}
            </div>

            <div className="action-card secondary nz-info-kafelek">
              <Clock size={28} style={{ marginBottom: 8 }} />
              <h2>Godzina <span style={{ fontWeight: 400, fontSize: '0.85rem' }}>(opcjonalnie)</span></h2>
              <input
                type="time"
                value={form.godzinaZdarzenia}
                onChange={e => set('godzinaZdarzenia', e.target.value)}
                className="nz-input-inline"
              />
            </div>
          </div>

          <div className="nz-pola" style={{ marginTop: 20 }}>
            <div className="nz-field">
              <label htmlFor="miejsce">Dokładne miejsce zdarzenia *</label>
              <input
                id="miejsce"
                type="text"
                placeholder="Np. ul. Marszałkowska 10, Warszawa"
                value={form.miejsce}
                onChange={e => set('miejsce', e.target.value)}
                className={bledy.miejsce ? 'error' : ''}
              />
              {bledy.miejsce && <span className="nz-error">{bledy.miejsce}</span>}
            </div>
          </div>

          <div className="nz-infobox">
            <AlertTriangle size={16} />
            <p>Podanie dokładnego adresu znacznie przyspieszy interwencję funkcjonariuszy.</p>
          </div>
        </div>
      )}

      {krok === 3 && (
        <div className="nz-section">
          <div className="action-cards-container nz-podsumowanie-grid">
            <div className="action-card secondary nz-sum-card">
              <span className="nz-sum-label">Kategoria</span>
              <strong>{form.kategoria}</strong>
            </div>
            <div className="action-card secondary nz-sum-card">
              <span className="nz-sum-label">Tytuł</span>
              <strong>{form.tytul}</strong>
            </div>
            <div className="action-card secondary nz-sum-card nz-sum-wide">
              <span className="nz-sum-label">Opis zdarzenia</span>
              <p style={{ margin: 0, textAlign: 'left', lineHeight: 1.6, whiteSpace: 'pre-wrap' }}>{form.opis}</p>
            </div>
            <div className="action-card secondary nz-sum-card">
              <span className="nz-sum-label">Data i czas</span>
              <strong>{form.dataZdarzenia}{form.godzinaZdarzenia ? `, ${form.godzinaZdarzenia}` : ''}</strong>
            </div>
            <div className="action-card secondary nz-sum-card">
              <span className="nz-sum-label">Miejsce</span>
              <strong>{form.miejsce}</strong>
            </div>
            <div className="action-card secondary nz-sum-card">
              <span className="nz-sum-label">Anonimowe</span>
              <strong>{form.anonimowe ? 'Tak' : 'Nie'}</strong>
            </div>
          </div>

          {submitError && <div style={{ marginTop: '16px', color: '#c62828' }}>{submitError}</div>}

          <div className="nz-infobox nz-infobox-warn">
            <AlertTriangle size={16} />
            <p>Składanie fałszywych zeznań jest przestępstwem — art. 233 Kodeksu Karnego.</p>
          </div>
        </div>
      )}

      <div className="nz-nav">
        {krok > 1 && <button className="action-card secondary nz-nav-btn" onClick={wstecz}><ChevronLeft size={18} /> Wstecz</button>}
        {krok < 3 && <button className="action-card primary nz-nav-btn" onClick={dalej}>Dalej <ChevronRight size={18} /></button>}
        {krok === 3 && <button className="action-card primary nz-nav-btn nz-wyslij" onClick={handleSubmit} disabled={isSubmitting}><CheckCircle size={18} /> {isSubmitting ? 'Wysyłanie...' : 'Wyślij zgłoszenie'}</button>}
      </div>
    </div>
  );
}

export default NoweZgloszenie;