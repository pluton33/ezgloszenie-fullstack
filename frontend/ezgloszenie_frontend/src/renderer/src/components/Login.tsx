import React from 'react'
import { useState } from 'react';
import { LockIcon, MailIcon, SquareArrowRightEnter } from 'lucide-react';

function Login(): React.JSX.Element {



    return (
        <div className='login-content'>
            <div className='login-text' style={{ marginTop: '15vw' }} >
                <MailIcon className='login-icon' size={16} />
                <input className='login-text-input' name="username" />
            </div>
            <div className='login-text' style={{ marginTop: '20px' }}>
                <LockIcon className='login-icon' size={16} />
                <input className='login-text-input' name="password" />
            </div>



            <button className='login-btn'>
                <span className='login-btn-text'>Zaloguj Się</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>

        </div>
    )
}

export default Login