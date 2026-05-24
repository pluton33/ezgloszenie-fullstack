import '../assets/base.css';
import React from 'react'
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom'; 
import logoUrl from '../assets/policja-logo.svg'
import { SunIcon, MoonIcon, CircleUserRound, X, Minus, Square, ChevronLeft, ChevronRight, Home, User, LogOut } from 'lucide-react'

function Navbar(): React.JSX.Element {
    const [isDark, setIsDark] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const navigate = useNavigate();
    const location = useLocation(); 

    const isAuthPage = location.pathname === '/login' || location.pathname === '/register' || location.pathname === '/';
    // TRYB CIEMNY O TUTAJ -------------------------------------------------------------------- 
    const toggleTheme = (): void => {
        if (isDark) {
            document.documentElement.removeAttribute('data-theme');
            setIsDark(false);
        } else {
            document.documentElement.setAttribute('data-theme', 'dark');
            setIsDark(true);
        }
    };

    const toggleDropdown = (): void => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const handleLogout = (): void => {
        setIsDropdownOpen(false);
        navigate('/login'); 
    };

    const closeWindow = (): void => window.electron.ipcRenderer.send('close-window');
    const minimizeWindow = (): void => window.electron.ipcRenderer.send('minimize-window');
    const maximizeWindow = (): void => window.electron.ipcRenderer.send('maximize-window');

    return (
        <div>
            <div className='topbar'>
                
                {/* GRUPA LEWA */}
                <div className='topbar-left-section' style={{ display: 'flex', alignItems: 'center' }}>
                    <div className='logo-strony'>
                        <img src={logoUrl} className='topbar-logo' alt="Logo" />
                        <span className="nazwa">e-Zgłoszenie</span>
                    </div>

                    {!isAuthPage && (
                        <div className="nav-history-controls">
                            <button className="history-btn" onClick={() => navigate(-1)} title="Cofnij">
                                <ChevronLeft size={22} />
                            </button>
                            <button className="history-btn" onClick={() => navigate(1)} title="Dalej">
                                <ChevronRight size={22} />
                            </button>
                            <button className="history-btn home-btn" onClick={() => navigate('/home')} title="Strona główna">
                                <Home size={18} />
                            </button>
                        </div>
                    )}
                </div>

                {/* GRUPA PRAWA */}
                <div className='topbar-right-section' style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    
                    {!isAuthPage && (
                        <div className="profile-container">
                            <button className="profile-btn" onClick={toggleDropdown} title="Konto użytkownika">
                                <CircleUserRound size={40} strokeWidth={1.5} />
                            </button>
                            
                            {/* Rozwijana lista z Twoimi 3 elementami */}
                            {isDropdownOpen && (
                                <div className="profile-dropdown">
                                    <button className="dropdown-item" onClick={() => { setIsDropdownOpen(false); navigate('/profil'); }}>
                                        <User size={16} />
                                        <span>Mój profil</span>
                                    </button>
                                    
                                    {/* Przycisk trybu ciemnego/jasnego - nie zamyka menu po kliknięciu, żeby użytkownik widział efekt */}
                                    <button className="dropdown-item" onClick={toggleTheme}>
                                        {isDark ? <SunIcon size={16} /> : <MoonIcon size={16} />}
                                        <span>{isDark ? 'Tryb jasny' : 'Tryb ciemny'}</span>
                                    </button>

                                    <hr className="dropdown-divider" />
                                    
                                    <button className="dropdown-item logout" onClick={handleLogout}>
                                        <LogOut size={16} />
                                        <span>Wyloguj się</span>
                                    </button>
                                </div>
                            )}
                        </div>
                    )}

                    <div className="window-controls">
                        <button id="minimize-btn" onClick={minimizeWindow} className='min-btn'><Minus size={18} /></button>
                        <button id="maximize-btn" onClick={maximizeWindow} className='max-btn'><Square className='-textbtnsquare' size={14} /></button>
                        <button id="close-btn" onClick={closeWindow} className="close-btn"><X size={18} /></button>
                    </div>
                </div>

            </div>
        </div>
    )
}

export default Navbar