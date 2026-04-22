import React from 'react';
import logoUrl from '../assets/policja-logo.svg';
import { LightbulbIcon } from 'lucide-react';

function Navbar(): React.JSX.Element {
    return (
        <div className='topbar'>
            {/* Logo policji */}
            <img src={logoUrl} className='topbar-logo' alt="Logo" />
            {/* Napis ezgloszenie */}
            <span className="nazwa">eZgłoszenie</span>
            {/* Przycisk i ikona zaurwki */}
            <button className="buttonzaruwka"><LightbulbIcon className='zaruwka' /></button>
        </div>
    )
}

export default Navbar