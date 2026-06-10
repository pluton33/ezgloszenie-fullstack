import '../assets/main.css';
import React, { useEffect, useRef, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import logoUrl from '../assets/policja-logo.svg';
import {
  SunIcon,
  MoonIcon,
  CircleUserRound,
  ChevronLeft,
  ChevronRight,
  Home,
  User,
  LogOut,
  Minus,
  Square,
  X
} from 'lucide-react';

function Navbar(): React.JSX.Element {
  const [isDark, setIsDark] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const profileRef = useRef<HTMLDivElement | null>(null);

  const isAuthPage =
    location.pathname === '/login' ||
    location.pathname === '/register' ||
    location.pathname === '/';

  useEffect(() => {
    const currentTheme = document.documentElement.getAttribute('data-theme');
    setIsDark(currentTheme === 'dark');
  }, []);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        isDropdownOpen &&
        profileRef.current &&
        !profileRef.current.contains(event.target as Node)
      ) {
        setIsDropdownOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isDropdownOpen]);

  const toggleTheme = (): void => {
    const next = !isDark;
    setIsDark(next);
    document.documentElement.setAttribute('data-theme', next ? 'dark' : 'light');
  };

  const toggleDropdown = (): void => {
    setIsDropdownOpen(prev => !prev);
  };

  const handleLogout = (): void => {
    localStorage.removeItem('userEmail');
    setIsDropdownOpen(false);
    navigate('/login');
  };

  const closeWindow = (): void => (window as any).electron?.ipcRenderer?.send('close-window');
  const minimizeWindow = (): void => (window as any).electron?.ipcRenderer?.send('minimize-window');
  const maximizeWindow = (): void => (window as any).electron?.ipcRenderer?.send('maximize-window');

  return (
    <div>
      <div className='topbar'>
        <div className='topbar-left-section' style={{ display: 'flex', alignItems: 'center' }}>
          <div className='logo-strony'>
            <img src={logoUrl} className='topbar-logo' alt='Logo' />
            <span className='nazwa'>e-Zgłoszenie</span>
          </div>

          {!isAuthPage && (
            <div className='nav-history-controls'>
              <button className='history-btn' onClick={() => navigate(-1)} title='Cofnij' type='button'>
                <ChevronLeft size={22} />
              </button>
              <button className='history-btn' onClick={() => navigate(1)} title='Dalej' type='button'>
                <ChevronRight size={22} />
              </button>
              <button className='history-btn home-btn' onClick={() => navigate('/home')} title='Strona główna' type='button'>
                <Home size={18} />
              </button>
            </div>
          )}
        </div>

        <div className='topbar-right-section' style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
          {!isAuthPage && (
            <div className='profile-container' ref={profileRef}>
              <button className='profile-btn' onClick={toggleDropdown} title='Konto użytkownika' type='button'>
                <CircleUserRound size={40} strokeWidth={1.5} />
              </button>

              {isDropdownOpen && (
                <div className='profile-dropdown'>
                  <button
                    className='dropdown-item'
                    onClick={() => {
                      setIsDropdownOpen(false);
                      navigate('/profil');
                    }}
                    type='button'
                  >
                    <User size={16} />
                    <span>Mój profil</span>
                  </button>

                  <button className='dropdown-item' onClick={toggleTheme} type='button'>
                    {isDark ? <SunIcon size={16} /> : <MoonIcon size={16} />}
                    <span>{isDark ? 'Tryb jasny' : 'Tryb ciemny'}</span>
                  </button>

                  <hr className='dropdown-divider' />

                  <button className='dropdown-item logout' onClick={handleLogout} type='button'>
                    <LogOut size={16} />
                    <span>Wyloguj się</span>
                  </button>
                </div>
              )}
            </div>
          )}

          <div className='window-controls'>
            <button id='minimize-btn' onClick={minimizeWindow} className='min-btn' type='button'>
              <Minus size={18} />
            </button>
            <button id='maximize-btn' onClick={maximizeWindow} className='max-btn' type='button'>
              <Square className='-textbtnsquare' size={14} />
            </button>
            <button id='close-btn' onClick={closeWindow} className='close-btn' type='button'>
              <X size={18} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Navbar;