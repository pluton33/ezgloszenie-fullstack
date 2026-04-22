import '../assets/base.css';
import React from 'react'
import { useState } from 'react';
import logoUrl from '../assets/policja-logo.svg'
import { SunIcon, MoonIcon, CircleUserRound, X, Minus, Square } from 'lucide-react'

function Navbar(): React.JSX.Element {
    const [isDark, setIsDark] = useState(false);

    const toggleTheme = (): void => {
        if (isDark) {
            document.documentElement.removeAttribute('data-theme');
            setIsDark(false);

        } else {
            document.documentElement.setAttribute('data-theme', 'dark');
            setIsDark(true);

        }
    };

    const closeWindow = (): void => {
        window.electron.ipcRenderer.send('close-window');
    }
    const minimizeWindow = (): void => {
        window.electron.ipcRenderer.send('minimize-window');
    }
    const maximizeWindow = (): void => {
        window.electron.ipcRenderer.send('maximize-window');
    }

    return (
        <div>
            <div className='topbar'>
                {/* Logo policji */}
                <div className='logo-strony'>
                    <img src={logoUrl} className='topbar-logo' alt="Logo" />
                    {/* Napis ezgloszenie */}
                    <span className="nazwa">e-Zgłoszenie</span>
                </div>
                {/* Przycisk i ikona zaurwki */}
                {/*<button onClick={toggleTheme} className="buttonzaruwka">
                    {isDark ? (<SunIcon className='zaruwka' />) : (<MoonIcon className='zaruwka' />)}
                </button>*/}
                <div className="window-controls">
                    <button id="minimize-btn" onClick={minimizeWindow} className='min-btn'><Minus size={18} /></button>
                    <button id="maximize-btn" onClick={maximizeWindow} className='max-btn'><Square className='-textbtnsquare' size={14} /></button>
                    <button id="close-btn" onClick={closeWindow} className="close-btn"><X size={18} /></button>
                </div>
            </div>
        </div>
    )

}

export default Navbar