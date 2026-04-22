import React from 'react';
import logoUrl from '../assets/policja-logo.svg';
import { LightbulbIcon } from 'lucide-react';

function Navbar(): React.JSX.Element {
    return (
        <div className='topbar'>
            <img src={logoUrl} className='topbar-logo' alt="Logo" />
            <LightbulbIcon className='zaruwka' />
        </div>
    )

}

export default Navbar