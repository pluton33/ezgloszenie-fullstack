import '../assets/base.css';
import React from 'react'
import { useState } from 'react';
import logoUrl from '../assets/policja-logo.svg'
import { LightbulbIcon, LightbulbOffIcon } from 'lucide-react'

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

    return (
        <div className='topbar'>
            {/* Logo policji */}
            <img src={logoUrl} className='topbar-logo' alt="Logo" />
            {/* Napis ezgloszenie */}
            <span className="nazwa">e-Zgłoszenie</span>
            {/* Przycisk i ikona zaurwki */}
            <button onClick={toggleTheme} className="buttonzaruwka">
                {isDark ? (<LightbulbOffIcon className='zaruwka' />) : (<LightbulbIcon className='zaruwka' />)}
            </button>
        </div>
    )

}

export default Navbar